import model.Door;
import model.Piece;
import model.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TiledMapToModelMap implements Function<CustomTiledMap, Map> {


    public static TiledMapToModelMap tiledMapToModelMap = new TiledMapToModelMap();

    public static TiledMapToModelMap instance() {
        return tiledMapToModelMap;
    }


    private TiledMapToModelMap() {

    }

    @Override
    public Map apply(CustomTiledMap tiledMap) {
        List<Tile> tiles = new ArrayList<>();
        List<Piece> pieces = new ArrayList<>();

        for (int y = 0; y < Constants.FIELD_HEIGHT; y++) {
            for (int x = 0; x < Constants.FIELD_WIDTH; x++) {
                int tileId = tiledMap.getTileId(Position.fromCoordinates(x, y), Constants.LAYER_INDEX_TILES);
                Tile tile = Tile.instance(x, y, Constants.TILE_ID_TO_TILE_TYPE.get(tileId));
                tiles.add(tile);

                int dynamicPieceId = tiledMap.getTileId(Position.fromCoordinates(x, y), Constants.LAYER_INDEX_DYNAMIC_PIECES);

                if (dynamicPieceId == Constants.TILE_ID_DOOR_OPEN) {
                    Door door = new Door(x, y, true);
                    pieces.add(door);
                } else if (dynamicPieceId == Constants.TILE_ID_DOOR_CLOSED) {
                    Door door = new Door(x, y, false);
                    pieces.add(door);
                }
            }
        }

        return Map.instance(tiles, pieces);
    }
}
