package puzzletester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

    public Starter(String name) throws SlickException {
        super(name);
        TiledMapReader mapReader = new XmlTiledMapReader();

        Path mapDir = Path.of("maps");
        try (Stream<Path> stream = Files.list(mapDir).filter(e -> e.getFileName().toString().endsWith(".tmx"))) {

            List<Path> mapPaths = stream.toList();
            for (Path mapPath : mapPaths) {
                CustomTiledMap tiledMap = mapReader.readTiledMap(mapPath.toString());
                Map map = TiledMapToModelMap.instance().apply(tiledMap);
                maps.add(map);
            }

        } catch (IOException | TiledMapIoException e) {
            throw new RuntimeException(e);
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
        }

        if (movementAttempt) {
                Position originalPosition = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);
                map.moveProtagonist();
                Position position = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);

                if (!position.equals(originalPosition)) {

                    //Go through all presence detectors and update their activatables.
                    List<Actor> actors = new ArrayList<>(map.getPieces().stream().filter(p -> p instanceof Crate).map(p -> (Actor) p).toList());
                    actors.add(protagonist);

                    List<Piece> presenceDetectors = map.getPieces().stream().filter(p -> p instanceof PresenceDetector).toList();

                    for (Piece presenceDetectorAsPiece : presenceDetectors) {
                        boolean presenceDetected = actors.stream().anyMatch(p -> p.positionX == presenceDetectorAsPiece.positionX && p.positionY == presenceDetectorAsPiece.positionY);
                        ActivatableHolder activatableHolder = (ActivatableHolder) presenceDetectorAsPiece;
                        if (presenceDetected) {
                            activatableHolder.getActivatables().forEach(Activatable::activate);
                        } else {
                            activatableHolder.getActivatables().forEach(Activatable::deactivate);
                        }
                    }

                    Optional<Pressable> pressable = map.piecesAtPosition(position)
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
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            maps.removeFirst();
            if (maps.isEmpty()) {
                System.exit(0);
            }
            map = maps.getFirst();
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
        Map map = maps.getFirst();

        RenderingUtils.translateToOffset(gc, g);

        for (int i = 0; i < Constants.FIELD_WIDTH ; i++) {
            for (int j = 0 ; j < Constants.FIELD_HEIGHT ; j++) {
                Tile tile = map.tileAtPosition(i, j);
                tile.render(gc, g, map);
            }
        }

        for (Piece piece : map.getPieces()) {
            piece.render(gc, g, map);
        }

        map.getProtagonist().render(gc, g, map);

        Ray ray = Ray.create(map);
        ray.render(gc, g, map);


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
        catch (SlickException ex) {
            Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
