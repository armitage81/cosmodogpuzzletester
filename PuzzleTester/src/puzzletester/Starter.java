package puzzletester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import puzzletester.model.elements.*;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.Portal;
import puzzletester.model.Ray;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.Piece;
import puzzletester.model.elements.Tile;
import puzzletester.model.elements.actors.Crate;
import puzzletester.model.elements.actors.Plasma;
import puzzletester.model.elements.actors.Protagonist;
import puzzletester.model.elements.dynamicpieces.Emp;
import puzzletester.model.elements.tiles.Exit;
import puzzletester.model.elements.tiles.Hull;
import puzzletester.interfaces.Activatable;
import puzzletester.interfaces.ActivatableHolder;
import puzzletester.interfaces.PresenceDetector;
import puzzletester.interfaces.Pressable;
import org.newdawn.slick.*;
import tiled.*;

public class Starter extends BasicGame {

    private final List<Map> maps = new ArrayList<>();

    public Starter(String name) throws SlickException, IOException, TiledMapIoException {
        super(name);
        TiledMapReader mapReader = new XmlTiledMapReader();

        Path mapDir = Path.of("maps/maps.conf");

        List<String> mapFileNames = Files.readAllLines(mapDir);

        for (String mapFileName : mapFileNames) {
            Path mapPath = mapDir.getParent().resolve(mapFileName);
            CustomTiledMap tiledMap = mapReader.readTiledMap(mapPath.toString());
            Map map = TiledMapToModelMap.instance().apply(tiledMap);
            maps.add(map);
        }

    }

    @Override
    public void init(GameContainer gc) throws SlickException {

    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        Map map = maps.getFirst();

        long timestamp = System.currentTimeMillis();

        Protagonist protagonist = map.getProtagonist();

        Input input = gc.getInput();

        boolean movementAttempt = false;
        boolean waiting = false;

        if (input.isKeyPressed(Input.KEY_RIGHT)) {
            protagonist.setDirection(DirectionType.EAST);
            movementAttempt = true;
        } else if (input.isKeyPressed(Input.KEY_DOWN)) {
            protagonist.setDirection(DirectionType.SOUTH);
            movementAttempt = true;
        } else if (input.isKeyPressed(Input.KEY_LEFT)) {
            movementAttempt = true;
            protagonist.setDirection(DirectionType.WEST);
        } else if (input.isKeyPressed(Input.KEY_UP)) {
            movementAttempt = true;
            protagonist.setDirection(DirectionType.NORTH);
        } else if (input.isKeyPressed(Input.KEY_ENTER)) {
            waiting = true;
        }

        boolean turnPassed = false;

        if (movementAttempt) {
            Position originalPosition = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);
            map.moveProtagonist();
            Position position = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);
            if (!position.equals(originalPosition)) {
                turnPassed = true;
            }
        }

        if (waiting) {
            turnPassed = true;
        }

        if (turnPassed) {

            map.updatePlasma();

            //Go through all presence detectors and update their activatables.
            List<Actor> actors = new ArrayList<>(map.getPieces()
                    .stream()
                    .filter(p -> p instanceof MoveableActor || p instanceof Plasma)
                    .map(p -> (Actor) p)
                    .toList());
            actors.add(protagonist);

            List<Piece> presenceDetectors = map.getPieces().stream().filter(p -> p instanceof PresenceDetector).toList();

            //Need to iterate twice. Example: Two sensors control the same door. Presence jumps from sensor B to sensor A.
            //If A would be handled first it would activate the door. Then B would be handled and it would deactivate
            //the door, ignoring the effect of sensor A. Hence, we first deactivate everything, where the presence is lost
            //and then activate everything where the presence is detected.

            for (Piece presenceDetectorAsPiece : presenceDetectors) {
                Optional<Actor> optPresence = actors
                        .stream()
                        .filter(p -> p.positionX == presenceDetectorAsPiece.positionX && p.positionY == presenceDetectorAsPiece.positionY)
                        .findFirst();
                PresenceDetector presenceDetector = (PresenceDetector) presenceDetectorAsPiece;
                if (optPresence.isEmpty()) {
                    presenceDetector.presenceLost(map);
                }
            }

            for (Piece presenceDetectorAsPiece : presenceDetectors) {
                Optional<Actor> optPresence = actors
                        .stream()
                        .filter(p -> p.positionX == presenceDetectorAsPiece.positionX && p.positionY == presenceDetectorAsPiece.positionY)
                        .findFirst();
                PresenceDetector presenceDetector = (PresenceDetector) presenceDetectorAsPiece;
                if (optPresence.isPresent()) {
                    presenceDetector.presenceDetected(map, optPresence.get());
                }
            }

            Optional<Pressable> pressable = map.piecesAtPosition(protagonist.getPosition())
                    .stream()
                    .filter(p -> p instanceof  Pressable)
                    .map(p -> (Pressable)p)
                    .findFirst();
            if (pressable.isPresent()) {
                pressable.get().press(map, protagonist);
            }
            Tile tile = map.tileAtPosition(protagonist.positionX, protagonist.positionY);
            if (tile instanceof Exit) {
                maps.removeFirst();
                if (maps.isEmpty()) {
                    System.exit(0);
                }
                map = maps.getFirst();
            }
        }

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            int mapsToSkip = 1;

            if (input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT)) {
                mapsToSkip = 10;
            }
            for (int k = 0; k < mapsToSkip; k++) {
                maps.removeFirst();
                if (maps.isEmpty()) {
                    System.exit(0);
                }
                map = maps.getFirst();
            }

        }

        if (input.isKeyPressed(Input.KEY_SPACE)) {

            boolean onEmpField = map.piecesAtPosition(protagonist.positionX, protagonist.positionY)
                    .stream()
                    .anyMatch(p -> p instanceof Emp);

            if (!onEmpField) {
                Ray ray = Ray.create(map);
                Optional<Position> rayTargetPosition = ray.getTargetPosition();
                if (rayTargetPosition.isPresent()) {
                    Tile tile = map.tileAtPosition(rayTargetPosition.get());
                    if (tile instanceof Hull hull) {
                        DirectionType directionFacingPlayer = DirectionType.reverse(ray.getLastDirection());
                        if (!map.portalExists(rayTargetPosition.get(), directionFacingPlayer)) {
                            Portal portal = new Portal(rayTargetPosition.get(), directionFacingPlayer);
                            map.createPortal(portal);
                        }
                    }
                }
            }
        }
        input.clearKeyPressedRecord();

    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {

        long timestamp = System.currentTimeMillis();

        Map map = maps.getFirst();
        RenderingUtils.translateToOffset(gc, g);
        for (int i = 0; i < Constants.FIELD_WIDTH ; i++) {
            for (int j = 0 ; j < Constants.FIELD_HEIGHT ; j++) {
                Tile tile = map.tileAtPosition(i, j);
                tile.render(gc, g, map);
            }
        }

        java.util.Map<Position, List<Piece>> piecesPerPosition = new HashMap<>();

        for (Piece piece : map.getPieces()) {
            Position piecePosition = Position.fromCoordinates(piece.positionX, piece.positionY);
            List<Piece> piecesAtPosition = piecesPerPosition.computeIfAbsent(piecePosition, k -> new ArrayList<>());
            piecesAtPosition.add(piece);
        }

        for (Position position : piecesPerPosition.keySet()) {
            List<Piece> piecesAtPosition = piecesPerPosition.get(position);
            long timestampPhase = timestamp / 500;
            int pieceNumberToRender = (int) (timestampPhase % piecesAtPosition.size());
            piecesAtPosition.get(pieceNumberToRender).render(gc, g, map);
        }

        map.getProtagonist().render(gc, g, map);

        Ray ray = Ray.create(map);
        ray.render(gc, g, map);

        g.setColor(Color.black);
        g.fillRect(10, 10, 600, 20);
        g.setColor(Color.red);
        g.drawString("Arrow keys to move, Enter to wait, Space to shoot.", 10, 10);

        RenderingUtils.resetTranslation(gc, g);
    }

    public static void main(String[] args) {
        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Starter("Application"));
            appgc.setDisplayMode(1600, 900, false);
            appgc.start();
        }
        catch (SlickException | IOException | TiledMapIoException ex) {
            Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
