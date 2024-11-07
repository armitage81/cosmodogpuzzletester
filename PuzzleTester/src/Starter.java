import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Tile;
import org.newdawn.slick.*;

public class Starter extends BasicGame {

    private CustomTiledMap map;

    public Starter(String name) {
        super(name);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        TiledMapReader mapReader = new XmlTiledMapReader();
        try {
            map = mapReader.readTiledMap("maps/map1.tmx");
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
                int tileId = map.getTileId(Position.fromCoordinates(i, j),0);
                if (tileId == 1) {
                    RenderingUtils.renderWall(gc, g, i, j);
                } else if (tileId == 3) {
                    RenderingUtils.renderObstacle(gc, g, i, j);
                } else {
                    RenderingUtils.renderDefaultTile(gc, g, i, j);
                }


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
