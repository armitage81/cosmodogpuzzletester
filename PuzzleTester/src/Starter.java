import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.*;
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
    public void update(GameContainer gc, int i) throws SlickException {}

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {

        RenderingUtils.translateToOffset(gc, g);

        for (int i = 0 ; i < Constants.FIELD_WIDTH ; i++) {
            for (int j = 0 ; j < Constants.FIELD_HEIGHT ; j++) {
                Tile.TileType tileType = map.tileAtPosition(i, j).type;
                if (tileType == Tile.TileType.WALL) {
                    RenderingUtils.renderWall(gc, g, i, j);
                } else if (tileType == Tile.TileType.OBSTACLE) {
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
