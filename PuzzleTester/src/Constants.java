import model.Tile;

import java.util.HashMap;

public class Constants {
    public static final int FIELD_WIDTH = 40;
    public static final int FIELD_HEIGHT = 30;
    public static final int TILE_SIZE = 20;

    public static final int LAYER_INDEX_TILES = 0;
    public static final int LAYER_INDEX_DYNAMIC_PIECES = 1;

    public static final int TILE_ID_DOOR_OPEN = 4;
    public static final int TILE_ID_DOOR_CLOSED = 2;



    public static final java.util.Map<Integer, Tile.TileType> TILE_ID_TO_TILE_TYPE = new HashMap<>();

    static {
        TILE_ID_TO_TILE_TYPE.put(0, Tile.TileType.EMPTY);
        TILE_ID_TO_TILE_TYPE.put(1, Tile.TileType.WALL);

        TILE_ID_TO_TILE_TYPE.put(3, Tile.TileType.OBSTACLE);
    }
}
