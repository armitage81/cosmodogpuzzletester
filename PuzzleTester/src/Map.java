import model.Piece;
import model.Tile;

import java.util.ArrayList;
import java.util.List;

public class Map {

    List<Tile> tiles = new ArrayList<>();
    List<Piece> pieces = new ArrayList<>();

    public static Map instance(List<Tile> tiles, List<Piece> pieces) {
        Map map = new Map();
        map.tiles = tiles;
        map.pieces = pieces;
        return map;
    }

    public Tile tileAtPosition(int x, int y) {
        return tiles.get(x + y * Constants.FIELD_WIDTH);
    }


}
