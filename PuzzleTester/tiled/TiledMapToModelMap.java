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

        for (int i = 0; i < Constants.FIELD_WIDTH; i++) {
            for (int j = 0; j < Constants.FIELD_HEIGHT; j++) {
                int tileId = tiledMap.getTileId(Position.fromCoordinates(i, j), Constants.LAYER_INDEX_TILES);
                Tile tile = Tile.instance(i, j, Constants.TILE_ID_TO_TILE_TYPE.get(tileId));
                tiles.add(tile);

                int dynamicPieceId = tiledMap.getTileId(Position.fromCoordinates(i, j), Constants.LAYER_INDEX_DYNAMIC_PIECES);

                if (dynamicPieceId == Constants.TILE_ID_DOOR_OPEN) {
                    Door door = new Door(i, j, true);
                    pieces.add(door);
                } else if (dynamicPieceId == Constants.TILE_ID_DOOR_CLOSED) {
                    Door door = new Door(i, j, false);
                    pieces.add(door);
                }
            }
        }

        return Map.instance(tiles, pieces);
    }
}
