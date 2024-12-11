package puzzletester.model;

import tiled.Position;

public class Entrance {

    private Position position;
    private DirectionType entranceDirection;

    public static Entrance instance(Position position, DirectionType entranceDirection) {
        Entrance entrance = new Entrance();
        entrance.position = position;
        entrance.entranceDirection = entranceDirection;
        return entrance;
    }

    public DirectionType getEntranceDirection() {
        return entranceDirection;
    }

    public Position getPosition() {
        return position;
    }
}
