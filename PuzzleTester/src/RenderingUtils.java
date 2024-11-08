import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.List;

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

    public static void renderSmoothWall(GameContainer gc, Graphics g, int positionX, int positionY, SmoothWall smoothWall) {
        g.setColor(new Color(Color.lightGray));
        g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        if (smoothWall.hasPortal()) {
            long time = System.currentTimeMillis() / 250;
            if (time % 2 == 0) {
                g.setColor(new Color(Color.orange));
                g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
            }
        }
        g.setColor(new Color(Color.black));
        g.drawRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
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

    public static void renderSwitch(GameContainer gc, Graphics g, int positionX, int positionY, Switch aSwitch) {
        g.setColor(new Color(Color.yellow));
        g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        g.setColor(new Color(Color.lightGray));
        g.drawRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderProtagonist(GameContainer gc, Graphics g, int positionX, int positionY, Protagonist protagonist) {
        long timestamp = System.currentTimeMillis() / 250;
        boolean blinking = timestamp % 2 == 0;

        if (blinking) {
            g.setColor(new Color(Color.blue));
            g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
            g.setColor(new Color(Color.lightGray));
            g.drawRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }

    }

    public static void renderRay(GameContainer gc, Graphics g, Map map, Protagonist protagonist) {
        List<Position> positions = map.rayPositions(protagonist);
        for (Position position : positions) {
            g.setColor(new Color(1, 1, 0, 0.33f));
            g.fillRect(position.getX() * Constants.TILE_SIZE, position.getY() * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }

        Position targetPosition = map.rayTargetPosition(protagonist);
        g.setColor(new Color(1, 0, 0, 0.33f));
        g.fillRect(targetPosition.getX() * Constants.TILE_SIZE, targetPosition.getY() * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

}
