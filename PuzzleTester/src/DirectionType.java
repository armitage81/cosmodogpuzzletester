public enum DirectionType {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public static DirectionType direction(Position sourcePosition, Position targetPosition) {

        if (sourcePosition.equals(targetPosition)) {
            throw new RuntimeException("Positions must not equal.");
        }

        float x1 = sourcePosition.getX();
        float x2 = targetPosition.getX();

        float y1 = sourcePosition.getY();
        float y2 = targetPosition.getY();

        float distanceX = x1 - x2;
        float distanceY = y1 - y2;

        if (Math.abs(distanceX) > 1) {
            throw new RuntimeException("Positions must be adjacent");
        }
        if (Math.abs(distanceY) > 1) {
            throw new RuntimeException("Positions must be adjacent");
        }
        if (Math.abs(distanceX) == 1 && Math.abs(distanceY) == 1) {
            throw new RuntimeException("Positions must be adjacent");
        }

        if (distanceX != 0) {
            return distanceX < 0 ? DirectionType.EAST : DirectionType.WEST;
        } else {
            return distanceY < 0 ? DirectionType.SOUTH : DirectionType.NORTH;
        }

    }

    public static DirectionType reverse(DirectionType directionType) {
        if (directionType == DirectionType.WEST) {
            return DirectionType.EAST;
        } else if (directionType == DirectionType.EAST) {
            return DirectionType.WEST;
        } else if (directionType == DirectionType.NORTH) {
            return DirectionType.SOUTH;
        } else if (directionType == DirectionType.SOUTH) {
            return DirectionType.NORTH;
        }
        throw new RuntimeException();
    }

    public static Position facedAdjacentPosition(Position position, DirectionType direction) {

        Position facedAdjacentPosition = position;

        if (direction == DirectionType.EAST) {
            facedAdjacentPosition = position.shifted(1, 0);
        } else if (direction == DirectionType.WEST) {
            facedAdjacentPosition = position.shifted(-1, 0);
        } else if (direction == DirectionType.NORTH) {
            facedAdjacentPosition = position.shifted(0, -1);
        } else {
            facedAdjacentPosition = position.shifted(0, 1);
        }

        return facedAdjacentPosition;
    }
}
