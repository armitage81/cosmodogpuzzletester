import jdk.jfr.consumer.RecordedObject;
import model.Door;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class RenderingUtils {

    public static void translateToOffset(GameContainer gc, Graphics g) {
        float offsetX = (gc.getWidth() - Constants.FIELD_WIDTH * Constants.TILE_SIZE) / 2.0f;
        float offsetY = (gc.getHeight() - Constants.FIELD_HEIGHT * Constants.TILE_SIZE) / 2.0f;
        g.translate(offsetX, offsetY);
    }

    public static void resetTranslation(GameContainer gc, Graphics g) {
        g.resetTransform();
    }

    public static void renderDefaultTile(GameContainer gc, Graphics g, int positionX, int positionY) {
        g.setColor(new Color(Color.white));
        g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        g.setColor(new Color(Color.lightGray));
        g.drawRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderWall(GameContainer gc, Graphics g, int positionX, int positionY) {
        g.setColor(new Color(Color.darkGray));
        g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        g.setColor(new Color(Color.black));
        g.drawRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        g.drawLine(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, (positionX + 1) * Constants.TILE_SIZE, (positionY + 1) * Constants.TILE_SIZE);
        g.drawLine((positionX + 1) * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, positionX * Constants.TILE_SIZE, (positionY + 1) * Constants.TILE_SIZE);
    }

    public static void renderObstacle(GameContainer gc, Graphics g, int positionX, int positionY) {
        g.setColor(new Color(Color.cyan));
        g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        g.setColor(new Color(Color.lightGray));
        g.drawRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderDoor(GameContainer gc, Graphics g, int positionX, int positionY, Door door) {
        g.setColor(new Color(door.open ? Color.green : Color.red));
        g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        g.setColor(new Color(Color.lightGray));
        g.drawRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

}
