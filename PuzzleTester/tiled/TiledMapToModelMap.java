import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                } else if (dynamicPieceId == Constants.TILE_ID_SWITCH) {
                    Switch aSwitch = new Switch(x, y);
                    pieces.add(aSwitch);
                }
            }
        }

        java.util.Map<Position, Switch> switches = pieces
                .stream()
                .filter(e -> e instanceof Switch)
                .collect(Collectors.toMap(e -> Position.fromCoordinates(e.positionX, e.positionY), e -> (Switch)e));

        java.util.Map<Position, Door> doors = pieces
                .stream()
                .filter(e -> e instanceof Door)
                .collect(Collectors.toMap(e -> Position.fromCoordinates(e.positionX, e.positionY), e -> (Door)e));

        List<TiledLineObject> connectors = tiledMap.getObjectGroups().get("connectors").getObjects().values().stream().map(e -> (TiledLineObject)e).toList();
        for (TiledLineObject connector : connectors) {
            TiledLineObject.Point start = connector.getPoints().get(0);
            TiledLineObject.Point end = connector.getPoints().get(1);
            Position startPosition = Position.fromCoordinates((float)((int)start.x / tiledMap.getTileWidth()), (float)((int)start.y / tiledMap.getTileHeight()));
            Position endPosition = Position.fromCoordinates((float)((int)end.x / tiledMap.getTileWidth()), (float)((int)end.y / tiledMap.getTileHeight()));
            Switch aSwitch = switches.get(startPosition);
            Switchable switchable = doors.get(endPosition);
            aSwitch.addSwitchable(switchable);
        }

        return Map.instance(tiles, pieces);
    }
}
