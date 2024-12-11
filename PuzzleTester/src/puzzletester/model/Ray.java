package puzzletester.model;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import puzzletester.Constants;
import puzzletester.model.elements.dynamicpieces.Emp;
import puzzletester.model.elements.actors.Protagonist;
import puzzletester.model.elements.dynamicpieces.Reflector;
import tiled.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Ray {

    private List<Position> rayPositions = new ArrayList<>();
    private Optional<Position> targetPosition = Optional.empty();
    private DirectionType lastDirection;

    public Ray(DirectionType initialDirection) {
        this.lastDirection = initialDirection;
    }

    public void addPosition(Position position) {
        rayPositions.add(position);
    }

    public boolean containsPosition(Position position) {
        return rayPositions.contains(position);
    }

    public List<Position> getRayPositions() {
        return rayPositions;
    }

    public DirectionType getLastDirection() {
        return lastDirection;
    }

    public Optional<Position> getTargetPosition() {
        return targetPosition;
    }

    public static Ray create(Map map) {

        Protagonist protagonist = map.getProtagonist();
        Position protagonistsPosition = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);
        DirectionType directionType = protagonist.getDirection();

        Ray ray = new Ray(protagonist.getDirection());

        boolean finished = false;
        Position lookAheadPosition = DirectionType.facedAdjacentPosition(protagonistsPosition, directionType);
        do {
            if (ray.containsPosition(lookAheadPosition)) {
                finished = true;
            } else if (lookAheadPosition.equals(protagonistsPosition)) {
                finished = true;
            } else {
                if (map.positionPenetrable(lookAheadPosition)) {
                    ray.addPosition(lookAheadPosition);
                    lookAheadPosition = DirectionType.facedAdjacentPosition(lookAheadPosition, directionType);
                } else {
                    Optional<Reflector> reflectorAtLookAheadPosition = map.piece(lookAheadPosition, Reflector.class);
                    if (reflectorAtLookAheadPosition.isPresent()) {
                        Optional<DirectionType> reflectionDirection = ReflectionType.reflectionDirection(((Reflector) reflectorAtLookAheadPosition.get()).getReflectionType(), directionType);
                        if (reflectionDirection.isPresent()) {
                            ray.addPosition(lookAheadPosition);
                            directionType = reflectionDirection.get();
                            ray.lastDirection = directionType;
                            lookAheadPosition = DirectionType.facedAdjacentPosition(lookAheadPosition, directionType);
                        } else {
                            ray.targetPosition = Optional.of(lookAheadPosition);
                            finished = true;
                        }
                    } else {
                        ray.targetPosition = Optional.of(lookAheadPosition);
                        finished = true;
                    }
                }
            }


        } while (!finished);

        return ray;
    }

    public void render(GameContainer gc, Graphics g, Map map) {
        long timestamp = System.currentTimeMillis() / 250;
        Color color = timestamp % 2 == 0 ? Color.red : Color.blue;
        g.setColor(color);

        /*
        When drawing the ray, following must be considered.
        - If the player stands on a field with an EMP piece, there is no ray.
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

        Protagonist protagonist = map.getProtagonist();

        Optional<Emp> emp = map.piece(protagonist.positionX, protagonist.positionY, Emp.class);
        if (emp.isPresent()) {
            return;
        }

        List<Position> positions = getRayPositions();
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
                if (getTargetPosition().isPresent()) {
                    lookAheadPosition = getTargetPosition();
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

        Optional<Position> targetPosition = getTargetPosition();
        if (targetPosition.isPresent()) {
            g.setColor(new Color(1, 0, 0, 0.33f));
            g.fillRect(targetPosition.get().getX() * Constants.TILE_SIZE, targetPosition.get().getY() * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }
    }

}
