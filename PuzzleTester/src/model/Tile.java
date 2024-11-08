package model;

public class Tile extends Element {

    public Tile(int x, int y, TileType type) {
        super(x, y);
        this.type = type;
    }

    public static enum TileType {
        EMPTY,
        WALL,
        OBSTACLE;
    }

    public TileType type;

    public static Tile instance(int positionX, int positionY, TileType type) {
        return new Tile(positionX, positionY, type);
    }

    @Override
    public String toString() {
        return super.toString() + ": " + type;
    }
}
