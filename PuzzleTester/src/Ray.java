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

    Optional<Position> getTargetPosition() {
        return targetPosition;
    }

    public boolean positionTransparent(Map map, Position position) {
        Tile tile = map.tileAtPosition((int)position.getX(), (int)position.getY());
        Optional<Piece> piece = map.pieceAtPosition((int)position.getX(), (int)position.getY());
        return tile.transparent() && (piece.isEmpty() || piece.get().transparent());
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
                if (ray.positionTransparent(map, lookAheadPosition)) {
                    ray.addPosition(lookAheadPosition);
                    lookAheadPosition = DirectionType.facedAdjacentPosition(lookAheadPosition, directionType);
                } else {
                    Optional<Piece> pieceAtLookAheadPosition = map.pieceAtPosition(lookAheadPosition);
                    if (pieceAtLookAheadPosition.isPresent() && pieceAtLookAheadPosition.get() instanceof Reflector) {
                        Optional<DirectionType> reflectionDirection = ReflectionType.reflectionDirection(((Reflector) pieceAtLookAheadPosition.get()).getReflectionType(), directionType);
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

}
