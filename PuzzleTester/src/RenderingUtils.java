import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

import java.util.List;
import java.util.Optional;

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

    public static void renderSmoothWall(GameContainer gc, Graphics g, int positionX, int positionY, Map map) {
        g.setColor(new Color(Color.lightGray));
        g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);

        long time = System.currentTimeMillis() / 250;
        if (time % 2 == 0) {
            g.setColor(new Color(Color.orange));
            Position position = Position.fromCoordinates(positionX, positionY);
            if (map.portalExists(position, DirectionType.NORTH)) {
                g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE / 2f);
            }
            if (map.portalExists(position, DirectionType.SOUTH)) {
                g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f, Constants.TILE_SIZE, Constants.TILE_SIZE / 2f);
            }
            if (map.portalExists(position, DirectionType.WEST)) {
                g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE / 2f, Constants.TILE_SIZE);
            }
            if (map.portalExists(position, DirectionType.EAST)) {
                g.fillRect(positionX * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE / 2f, Constants.TILE_SIZE);
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

    public static void renderSmoke(GameContainer gc, Graphics g, int positionX, int positionY) {

        long timestamp = System.currentTimeMillis() / 250;

        g.setColor(new Color(timestamp % 2 == 0 ? Color.lightGray : Color.darkGray));
        g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderExit(GameContainer gc, Graphics g, int positionX, int positionY) {
        renderDefaultTile(gc, g, positionX, positionY);
        g.setColor(new Color(Color.red));
        g.setLineWidth(3);
        g.drawLine(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, (positionX + 1) * Constants.TILE_SIZE, (positionY + 1) * Constants.TILE_SIZE);
        g.drawLine((positionX + 1) * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, positionX * Constants.TILE_SIZE, (positionY + 1) * Constants.TILE_SIZE);
        g.setLineWidth(1);
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

    public static void renderJammer(GameContainer gc, Graphics g, int positionX, int positionY, Jammer jammer) {
        g.setColor(new Color(Color.red));
        g.fillOval(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        g.setColor(new Color(Color.white));
        g.fillRect(positionX * Constants.TILE_SIZE + 6, positionY * Constants.TILE_SIZE + 12, Constants.TILE_SIZE - 12, Constants.TILE_SIZE - 24);
    }

    public static void renderReflector(GameContainer gc, Graphics g, int positionX, int positionY, Reflector reflector) {
        g.setColor(new Color(Color.black));

        Position tl = Position.fromCoordinates(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE);
        Position tr = Position.fromCoordinates(positionX * Constants.TILE_SIZE + Constants.TILE_SIZE, positionY * Constants.TILE_SIZE);
        Position bl = Position.fromCoordinates(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE + Constants.TILE_SIZE);
        Position br = Position.fromCoordinates(positionX * Constants.TILE_SIZE + Constants.TILE_SIZE, positionY * Constants.TILE_SIZE + Constants.TILE_SIZE);

        if (reflector.getReflectionType() == ReflectionType.NORTH_WEST) {
            g.drawLine(tr.getX(), tr.getY(), br.getX(), br.getY());
            g.drawLine(br.getX(), br.getY(), bl.getX(), bl.getY());
            g.drawLine(bl.getX(), bl.getY(), tr.getX(), tr.getY());
        }

        if (reflector.getReflectionType() == ReflectionType.NORTH_EAST) {
            g.drawLine(tl.getX(), tl.getY(), bl.getX(), bl.getY());
            g.drawLine(bl.getX(), bl.getY(), br.getX(), br.getY());
            g.drawLine(br.getX(), br.getY(), tl.getX(), tl.getY());
        }

        if (reflector.getReflectionType() == ReflectionType.SOUTH_EAST) {
            g.drawLine(tl.getX(), tl.getY(), tr.getX(), tr.getY());
            g.drawLine(tr.getX(), tr.getY(), bl.getX(), bl.getY());
            g.drawLine(bl.getX(), bl.getY(), tl.getX(), tl.getY());
        }

        if (reflector.getReflectionType() == ReflectionType.SOUTH_WEST) {
            g.drawLine(tl.getX(), tl.getY(), tr.getX(), tr.getY());
            g.drawLine(tr.getX(), tr.getY(), br.getX(), br.getY());
            g.drawLine(br.getX(), br.getY(), tl.getX(), tl.getY());
        }

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

        Ray ray = Ray.create(map);

        List<Position> positions = ray.getRayPositions();
        for (int i = 0; i < positions.size(); i++) {

            Position lookBehindPosition = i == 0 ? Position.fromCoordinates(protagonist.positionX, protagonist.positionY) : positions.get(i - 1);
            Position position = positions.get(i);
            Optional<Position> lookAheadPosition = i == positions.size() - 1 ? Optional.empty() : Optional.of(positions.get(i + 1));

            DirectionType startDirection = DirectionType.direction(lookBehindPosition, position);
            DirectionType endDirection = lookAheadPosition.isPresent() ? DirectionType.direction(position, lookAheadPosition.get()) : startDirection;

            g.setLineWidth(3);
            g.setColor(Color.black);

            float x1;
            float y1;
            float x2;
            float y2;

            if (startDirection == DirectionType.NORTH || endDirection == DirectionType.SOUTH) {
                x1 = position.getX() * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f;
                x2 = x1;
                y1 = position.getY() * Constants.TILE_SIZE + Constants.TILE_SIZE;
                y2 = position.getY() * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f;
                g.drawLine(x1, y1, x2, y2);
            }

            if (startDirection == DirectionType.SOUTH || endDirection == DirectionType.NORTH) {
                x1 = position.getX() * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f;
                x2 = x1;
                y1 = position.getY() * Constants.TILE_SIZE;
                y2 = position.getY() * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f;
                g.drawLine(x1, y1, x2, y2);
            }

            if (startDirection == DirectionType.EAST || endDirection == DirectionType.WEST) {
                x1 = position.getX() * Constants.TILE_SIZE;
                x2 = position.getX() * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f;
                y1 = position.getY() * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f;
                y2 = y1;
                g.drawLine(x1, y1, x2, y2);
            }

            if (startDirection == DirectionType.WEST || endDirection == DirectionType.EAST) {
                x1 = position.getX() * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f;
                x2 = position.getX() * Constants.TILE_SIZE + Constants.TILE_SIZE;
                y1 = position.getY() * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f;
                y2 = y1;
                g.drawLine(x1, y1, x2, y2);
            }

            g.setLineWidth(1);

        }

        Optional<Position> targetPosition = ray.getTargetPosition();
        if (targetPosition.isPresent()) {
            g.setColor(new Color(1, 0, 0, 0.33f));
            g.fillRect(targetPosition.get().getX() * Constants.TILE_SIZE, targetPosition.get().getY() * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }

    }

}
