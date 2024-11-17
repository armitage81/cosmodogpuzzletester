import java.util.ArrayList;
import java.util.List;
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

        Protagonist protagonist = null;
        List<Tile> tiles = new ArrayList<>();
        List<Piece> pieces = new ArrayList<>();

        for (int y = 0; y < Constants.FIELD_HEIGHT; y++) {
            for (int x = 0; x < Constants.FIELD_WIDTH; x++) {
                int tileId = tiledMap.getTileId(Position.fromCoordinates(x, y), Constants.LAYER_INDEX_TILES);

                Tile tile;

                if (tileId == Constants.TILE_ID_WALL) {
                    tile = new Wall(x, y);
                } else if (tileId == Constants.TILE_ID_SMOOTH_WALL) {
                    tile = new SmoothWall(x, y);
                }else if (tileId == Constants.TILE_ID_EMPTY) {
                    tile = new Floor(x, y);
                } else if (tileId == Constants.TILE_ID_GLASS) {
                    tile = new Glass(x, y);
                } else if (tileId == Constants.TILE_ID_EXIT) {
                    tile = new Exit(x, y);
                } else if (tileId == Constants.TILE_ID_SMOKE) {
                    tile = new Smoke(x, y);
                } else {
                    throw new RuntimeException();
                }

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
                } else if (dynamicPieceId == Constants.TILE_ID_JAMMER) {
                    Jammer jammer = new Jammer(x, y);
                    pieces.add(jammer);
                } else if (dynamicPieceId == Constants.TILE_ID_REFLECTOR_NORTH_WEST) {
                    Reflector reflector = new Reflector(x, y, ReflectionType.NORTH_WEST);
                    pieces.add(reflector);
                } else if (dynamicPieceId == Constants.TILE_ID_REFLECTOR_NORTH_EAST) {
                    Reflector reflector = new Reflector(x, y, ReflectionType.NORTH_EAST);
                    pieces.add(reflector);
                } else if (dynamicPieceId == Constants.TILE_ID_REFLECTOR_SOUTH_EAST) {
                    Reflector reflector = new Reflector(x, y, ReflectionType.SOUTH_EAST);
                    pieces.add(reflector);
                } else if (dynamicPieceId == Constants.TILE_ID_REFLECTOR_SOUTH_WEST) {
                    Reflector reflector = new Reflector(x, y, ReflectionType.SOUTH_WEST);
                    pieces.add(reflector);
                } else if (dynamicPieceId == Constants.TILE_ID_CONVEYOR_WEST) {
                    Conveyor conveyor = new Conveyor(x, y, DirectionType.WEST);
                    pieces.add(conveyor);
                } else if (dynamicPieceId == Constants.TILE_ID_CONVEYOR_NORTH) {
                    Conveyor conveyor = new Conveyor(x, y, DirectionType.NORTH);
                    pieces.add(conveyor);
                } else if (dynamicPieceId == Constants.TILE_ID_CONVEYOR_EAST) {
                    Conveyor conveyor = new Conveyor(x, y, DirectionType.EAST);
                    pieces.add(conveyor);
                } else if (dynamicPieceId == Constants.TILE_ID_CONVEYOR_SOUTH) {
                    Conveyor conveyor = new Conveyor(x, y, DirectionType.SOUTH);
                    pieces.add(conveyor);
                }

                int protagonistTileId = tiledMap.getTileId(Position.fromCoordinates(x, y), Constants.LAYER_INDEX_ACTORS);

                if (protagonistTileId == Constants.TILE_ID_PROTAGONIST) {
                    protagonist = new Protagonist(x, y);
                }

            }
        }

        java.util.Map<Position, Switch> switches = pieces
                .stream()
                .filter(e -> e instanceof Switch)
                .collect(Collectors.toMap(e -> Position.fromCoordinates(e.positionX, e.positionY), e -> (Switch)e));

        java.util.Map<Position, Switchable> switchables = pieces
                .stream()
                .filter(e -> e instanceof Switchable)
                .collect(Collectors.toMap(e -> Position.fromCoordinates(e.positionX, e.positionY), e -> (Switchable) e));

        List<TiledLineObject> connectors = tiledMap.getObjectGroups().get("connectors").getObjects().values().stream().map(e -> (TiledLineObject)e).toList();
        for (TiledLineObject connector : connectors) {
            TiledLineObject.Point start = connector.getPoints().get(0);
            TiledLineObject.Point end = connector.getPoints().get(1);
            Position startPosition = Position.fromCoordinates((float)((int)start.x / tiledMap.getTileWidth()), (float)((int)start.y / tiledMap.getTileHeight()));
            Position endPosition = Position.fromCoordinates((float)((int)end.x / tiledMap.getTileWidth()), (float)((int)end.y / tiledMap.getTileHeight()));
            Switch aSwitch = switches.get(startPosition);
            Switchable switchable = switchables.get(endPosition);
            aSwitch.addSwitchable(switchable);
        }

        return Map.instance(tiles, pieces, protagonist);
    }
}
