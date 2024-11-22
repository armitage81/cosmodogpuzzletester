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

    public static void renderWall(GameContainer gc, Graphics g, int positionX, int positionY, Wall wall) {
        wall.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderSmoothWall(GameContainer gc, Graphics g, int positionX, int positionY, Map map, SmoothWall smoothWall) {

        smoothWall.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);

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
    }

    public static void renderGlass(GameContainer gc, Graphics g, int positionX, int positionY, Glass glass) {
        glass.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderSmoke(GameContainer gc, Graphics g, int positionX, int positionY, Smoke smoke) {
        long timestamp = System.currentTimeMillis() / 250;
        renderDefaultTile(gc, g, positionX, positionY);
        smoke.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);

    }

    public static void renderExit(GameContainer gc, Graphics g, int positionX, int positionY, Exit exit) {
        renderDefaultTile(gc, g, positionX, positionY);
        exit.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderDoor(GameContainer gc, Graphics g, int positionX, int positionY, Door door) {
        door.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderSwitch(GameContainer gc, Graphics g, int positionX, int positionY, Switch aSwitch) {
        aSwitch.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderJammer(GameContainer gc, Graphics g, int positionX, int positionY, Jammer jammer) {
        jammer.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderReflector(GameContainer gc, Graphics g, int positionX, int positionY, Reflector reflector) {
        reflector.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static void renderConveyor(GameContainer gc, Graphics g, int positionX, int positionY, Conveyor conveyor) {
        conveyor.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }


    public static void renderProtagonist(GameContainer gc, Graphics g, int positionX, int positionY, Protagonist protagonist) {
        long timestamp = System.currentTimeMillis() / 250;
        boolean blinking = timestamp % 2 == 0;

        if (blinking) {
            protagonist.getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }

    }

    public static void renderRay(GameContainer gc, Graphics g, Map map, Protagonist protagonist) {

        long timestamp = System.currentTimeMillis() / 250;
        Color color = timestamp % 2 == 0 ? Color.red : Color.blue;
        g.setColor(color);

        Ray ray = Ray.create(map);

        /*
        When drawing the ray, following must be considered.
        - The ray always starts at the position adjacent to the player in the facing direction, with one exception:
        - The exception being when the player is facing an adjacent non-transparent tile, in which case the ray covers no positions.
        - In the simplest case, the ray is a straight line covering one or multiple positions which ends at an non-transparent tile.
        - The ray can be bent though. When the ray hits a reflector, the ray is bent in the direction of the reflector.
        - A bent ray can hit itself (like in the Snake game), in which case the ray ends.
        - A bent ray can also hit the protagonist, in which case the ray also ends.
        - A ray that hits either itself or protagonist does not have a target position.
        - A ray that ends before a non-transparent tile has a target position, which is this tile's position.
        - When drawing a ray, we look at all it's positions and draw the ray's segment for each of those positions.
        - Each position refers to the previous position and to the next position.
        - For the very first position, the previous position is the protagonist's position.
        - For the very last position, the next position is the target position (if it exists)
        - or the position adjacent to the last position in the direction of the last direction.
        - Each segment of the ray is drawn in two steps.
        - The first half is drawn depending on the direction of the previous position.
        - The second half is drawn depending on the direction of the next position.
        - Each half of the segment can be a straight line or an L-shape.
        - If the target position does exist, it is drawn at the end.

         */

        List<Position> positions = ray.getRayPositions();
        for (int i = 0; i < positions.size(); i++) {

            Position lookBehindPosition;
            if (i == 0) {
                lookBehindPosition = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);
            } else {
                lookBehindPosition = positions.get(i - 1);
            }

            Position position = positions.get(i);
            DirectionType startDirection = DirectionType.direction(lookBehindPosition, position);

            Optional<Position> lookAheadPosition;

            if (i < positions.size() - 1) {
                lookAheadPosition = Optional.of(positions.get(i + 1));
            } else {
                if (ray.getTargetPosition().isPresent()) {
                    lookAheadPosition = ray.getTargetPosition();
                } else {
                    lookAheadPosition = Optional.of(DirectionType.facedAdjacentPosition(position, startDirection));
                }
            }

            DirectionType endDirection = lookAheadPosition.isPresent() ? DirectionType.direction(position, lookAheadPosition.get()) : startDirection;

            g.setLineWidth(3);

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
