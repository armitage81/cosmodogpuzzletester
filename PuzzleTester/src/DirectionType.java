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
}
