package model;

public class Tile extends Element {

    public static enum TileType {
        EMPTY,
        WALL,
        OBSTACLE;
    }

    public TileType type;

    public static Tile instance(int positionX, int positionY, TileType type) {
        Tile tile = new Tile();
        tile.positionX = positionX;
        tile.positionY = positionY;
        tile.type = type;
        return tile;
    }

}
