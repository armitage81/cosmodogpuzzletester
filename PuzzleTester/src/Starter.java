import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.newdawn.slick.*;

import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.exit;

public class Starter extends BasicGame {

    private List<Map> maps = new ArrayList<>();

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
            if (map.protagonistCanGoStraight(protagonist)) {
                Position originalPosition = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);
                map.goToEnvisionedPosition(protagonist);
                Position position = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);

                if (!position.equals(originalPosition)) {
                    Optional<Piece> piece = map.pieceAtPosition(position);
                    if (piece.isPresent() && piece.get() instanceof Pressable) {
                        ((Pressable)piece.get()).press(map, protagonist);
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
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            maps.removeFirst();
            if (maps.isEmpty()) {
                System.exit(0);
            }
            map = maps.getFirst();
        }

        if (input.isKeyPressed(Input.KEY_SPACE)) {
            Ray ray = Ray.create(map);
            Optional<Position> rayTargetPosition = ray.getTargetPosition();
            if (rayTargetPosition.isPresent()) {
                Tile tile = map.tileAtPosition(rayTargetPosition.get());
                if (tile instanceof SmoothWall smoothWall) {
                    DirectionType directionFacingPlayer = DirectionType.reverse(ray.getLastDirection());
                    if (!map.portalExists(rayTargetPosition.get(), directionFacingPlayer)) {
                        Portal portal = new Portal(rayTargetPosition.get(), directionFacingPlayer);
                        map.createPortal(portal);
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

        for (int i = 0 ; i < Constants.FIELD_WIDTH ; i++) {
            for (int j = 0 ; j < Constants.FIELD_HEIGHT ; j++) {
                Tile tile = map.tileAtPosition(i, j);
                if (tile instanceof Wall) {
                    RenderingUtils.renderWall(gc, g, i, j, (Wall)tile);
                } else if (tile instanceof Glass) {
                    RenderingUtils.renderGlass(gc, g, i, j, (Glass) tile);
                } else if (tile instanceof SmoothWall) {
                    RenderingUtils.renderSmoothWall(gc, g, i, j, map, (SmoothWall) tile);
                } else if (tile instanceof Exit) {
                    RenderingUtils.renderExit(gc, g, i, j, (Exit) tile);
                } else if (tile instanceof Smoke) {
                    RenderingUtils.renderSmoke(gc, g, i, j, (Smoke) tile);
                }else {
                    RenderingUtils.renderDefaultTile(gc, g, i, j);
                }
            }
        }

        for (Piece piece : map.getPieces()) {

            if (piece instanceof Door) {
                RenderingUtils.renderDoor(gc, g, piece.positionX, piece.positionY, (Door)piece);
            } else if (piece instanceof Switch) {
                RenderingUtils.renderSwitch(gc, g, piece.positionX, piece.positionY, (Switch)piece);
            } else if (piece instanceof Jammer) {
                RenderingUtils.renderJammer(gc, g, piece.positionX, piece.positionY, (Jammer)piece);
            } else if (piece instanceof Reflector) {
                RenderingUtils.renderReflector(gc, g, piece.positionX, piece.positionY, (Reflector) piece);
            } else if (piece instanceof Conveyor) {
                RenderingUtils.renderConveyor(gc, g, piece.positionX, piece.positionY, (Conveyor) piece);
            }

        }

        RenderingUtils.renderProtagonist(gc, g, map.getProtagonist().positionX, map.getProtagonist().positionY, map.getProtagonist());
        RenderingUtils.renderRay(gc, g, map, map.getProtagonist());


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
