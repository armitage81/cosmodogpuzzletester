public enum DirectionType {
    NORTH,
    EAST,
    SOUTH,
    WEST;

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
