import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.*;

public class Starter extends BasicGame {

    private long lastInputTime = 0;

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

        boolean inputHappened = false;

        if (input.isKeyPressed(Input.KEY_D)) {
            protagonist.setDirection(Actor.DirectionType.EAST);
            inputHappened = true;
        } else if (input.isKeyPressed(Input.KEY_S)) {
            protagonist.setDirection(Actor.DirectionType.SOUTH);
            inputHappened = true;
            lastInputTime = timestamp;
        } else if (input.isKeyPressed(Input.KEY_A)) {
            inputHappened = true;
            protagonist.setDirection(Actor.DirectionType.WEST);
            lastInputTime = timestamp;
        } else if (input.isKeyPressed(Input.KEY_W)) {
            inputHappened = true;
            protagonist.setDirection(Actor.DirectionType.NORTH);
            lastInputTime = timestamp;
        }

        if (inputHappened) {
            if (map.protagonistCanGoStraight(protagonist)) {
                protagonist.goToEnvisionedPosition();
            }
            lastInputTime = timestamp;
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
                } else {
                    RenderingUtils.renderDefaultTile(gc, g, i, j);
                }
            }
        }

        for (Piece piece : map.pieces) {

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
