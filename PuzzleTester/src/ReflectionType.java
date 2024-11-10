public enum ReflectionType {

    NORTH_WEST,
    NORTH_EAST,
    SOUTH_EAST,
    SOUTH_WEST;


    public static ReflectionType next(ReflectionType reflectionType) {

        if (reflectionType == NORTH_WEST) {
            return NORTH_EAST;
        }
        if (reflectionType == NORTH_EAST) {
            return SOUTH_EAST;
        }

        if (reflectionType == SOUTH_EAST) {
            return SOUTH_WEST;
        }

        if (reflectionType == SOUTH_WEST) {
            return NORTH_WEST;
        }

        throw new RuntimeException();
    }

}
