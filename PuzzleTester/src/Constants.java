import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Constants {
    public static final int FIELD_WIDTH = 40;
    public static final int FIELD_HEIGHT = 30;
    public static final int TILE_SIZE = 30;

    public static final int LAYER_INDEX_TILES = 0;
    public static final int LAYER_INDEX_DYNAMIC_PIECES_1 = 1;
    public static final int LAYER_INDEX_DYNAMIC_PIECES_2 = 2;
    public static final int LAYER_INDEX_DYNAMIC_PIECES_3 = 3;
    public static final int LAYER_INDEX_ACTORS = 4;


    public static final int TILE_ID_EMPTY = 0;
    public static final int TILE_ID_HARDWARE = 1;
    public static final int TILE_ID_GLASS = 3;
    public static final int TILE_ID_HULL = 7;
    public static final int TILE_ID_EXIT = 8;
    public static final int TILE_ID_SMOKE = 9;
    public static final int TILE_ID_INTERSTICE = 21;

    public static final int TILE_ID_HATCH_OPEN = 4;
    public static final int TILE_ID_HATCH_CLOSED = 2;
    public static final int TILE_ID_SWITCH = 5;
    public static final int TILE_ID_JAMMER = 19;
    public static final int TILE_ID_REFLECTOR_NORTH_WEST = 28;
    public static final int TILE_ID_REFLECTOR_NORTH_EAST = 29;
    public static final int TILE_ID_REFLECTOR_SOUTH_EAST = 30;
    public static final int TILE_ID_REFLECTOR_SOUTH_WEST = 31;
    public static final int TILE_ID_EMP = 23;

    public static final int TILE_ID_CONVEYOR_WEST = 32;
    public static final int TILE_ID_CONVEYOR_NORTH = 33;
    public static final int TILE_ID_CONVEYOR_EAST = 34;
    public static final int TILE_ID_CONVEYOR_SOUTH = 35;

    public static final int TILE_ID_CRATE = 20;

    public static final int TILE_ID_PROTAGONIST = 6;


    public static SpriteSheet SPRITE_SHEET;

    static {
        try {
            SPRITE_SHEET = new SpriteSheet("maps/tiles.png", 16, 16);
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

}
