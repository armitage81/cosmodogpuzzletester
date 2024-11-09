import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.*;

public class Starter extends BasicGame {

    private Map map;

    public Starter(String name) {
        super(name);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        TiledMapReader mapReader = new XmlTiledMapReader();
        try {
            CustomTiledMap tiledMap = mapReader.readTiledMap("maps/map1.tmx");
            map = TiledMapToModelMap.instance().apply(tiledMap);
            System.out.println(map.tileAtPosition(0, 19));
        } catch (TiledMapIoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {

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
                protagonist.goToEnvisionedPosition();
                Position position = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);

                if (!position.equals(originalPosition)) {
                    Optional<Piece> piece = map.pieceAtPosition(position);
                    if (piece.isPresent() && piece.get() instanceof Pressable) {
                        ((Pressable)piece.get()).press(protagonist);
                    }
                }
            }
        }

        if (input.isKeyPressed(Input.KEY_ENTER)) {
            Position rayTargetPosition = map.rayTargetPosition(protagonist);
            Tile tile = map.tileAtPosition(rayTargetPosition);
            if (tile instanceof SmoothWall smoothWall) {
                DirectionType directionFacingPlayer = DirectionType.reverse(protagonist.getDirection());
                if (!map.portalExists(rayTargetPosition, directionFacingPlayer)) {
                    Portal portal = new Portal(rayTargetPosition, directionFacingPlayer);
                    map.createPortal(portal);
                }
            }
        }

        input.clearKeyPressedRecord();

    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {

        RenderingUtils.translateToOffset(gc, g);

        for (int i = 0 ; i < Constants.FIELD_WIDTH ; i++) {
            for (int j = 0 ; j < Constants.FIELD_HEIGHT ; j++) {
                Tile tile = map.tileAtPosition(i, j);
                if (tile instanceof Wall) {
                    RenderingUtils.renderWall(gc, g, i, j);
                } else if (tile instanceof Obstacle) {
                    RenderingUtils.renderObstacle(gc, g, i, j);
                } else if (tile instanceof SmoothWall) {
                    RenderingUtils.renderSmoothWall(gc, g, i, j, map);
                } else {
                    RenderingUtils.renderDefaultTile(gc, g, i, j);
                }
            }
        }

        for (Piece piece : map.getPieces()) {

            if (piece instanceof Door) {
                RenderingUtils.renderDoor(gc, g, piece.positionX, piece.positionY, (Door)piece);
            } else if (piece instanceof Switch) {
                RenderingUtils.renderSwitch(gc, g, piece.positionX, piece.positionY, (Switch)piece);
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
            appgc.setDisplayMode(1080, 720, false);
            appgc.start();
        }
        catch (SlickException ex) {
            Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
