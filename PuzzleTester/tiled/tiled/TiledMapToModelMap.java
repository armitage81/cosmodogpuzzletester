package tiled;

import puzzletester.*;
import puzzletester.model.elements.*;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.ReflectionType;
import puzzletester.model.elements.Piece;
import puzzletester.model.elements.Tile;
import puzzletester.model.elements.actors.Crate;
import puzzletester.model.elements.actors.GlassTank;
import puzzletester.model.elements.actors.Protagonist;
import puzzletester.interfaces.Activatable;
import puzzletester.interfaces.ActivatableHolder;
import puzzletester.interfaces.Switchable;
import puzzletester.interfaces.SwitchableHolder;
import puzzletester.model.elements.dynamicpieces.*;
import puzzletester.model.elements.tiles.*;

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

                if (tileId == Constants.TILE_ID_HARDWARE) {
                    tile = new Hardware(x, y);
                } else if (tileId == Constants.TILE_ID_HULL) {
                    tile = new Hull(x, y);
                }else if (tileId == Constants.TILE_ID_EMPTY) {
                    tile = new Space(x, y);
                } else if (tileId == Constants.TILE_ID_GLASS) {
                    tile = new Glass(x, y);
                } else if (tileId == Constants.TILE_ID_EXIT) {
                    tile = new Exit(x, y);
                } else if (tileId == Constants.TILE_ID_SMOKE) {
                    tile = new Smoke(x, y);
                } else if (tileId == Constants.TILE_ID_INTERSTICE) {
                    tile = new Interstice(x, y);
                } else {
                    throw  new RuntimeException();
                }

                tiles.add(tile);

                int[] dynamicPieceIds = new int[] {
                        tiledMap.getTileId(Position.fromCoordinates(x, y), Constants.LAYER_INDEX_DYNAMIC_PIECES_1),
                        tiledMap.getTileId(Position.fromCoordinates(x, y), Constants.LAYER_INDEX_DYNAMIC_PIECES_2),
                        tiledMap.getTileId(Position.fromCoordinates(x, y), Constants.LAYER_INDEX_DYNAMIC_PIECES_3)
                };

                for (int dynamicPieceId : dynamicPieceIds) {

                    if (dynamicPieceId == Constants.TILE_ID_HATCH_OPEN) {
                        Hatch hatch = new Hatch(x, y, true);
                        pieces.add(hatch);
                    } else if (dynamicPieceId == Constants.TILE_ID_HATCH_CLOSED) {
                        Hatch hatch = new Hatch(x, y, false);
                        pieces.add(hatch);
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
                        Current current = new Current(x, y, DirectionType.WEST);
                        pieces.add(current);
                    } else if (dynamicPieceId == Constants.TILE_ID_CONVEYOR_NORTH) {
                        Current current = new Current(x, y, DirectionType.NORTH);
                        pieces.add(current);
                    } else if (dynamicPieceId == Constants.TILE_ID_CONVEYOR_EAST) {
                        Current current = new Current(x, y, DirectionType.EAST);
                        pieces.add(current);
                    } else if (dynamicPieceId == Constants.TILE_ID_CONVEYOR_SOUTH) {
                        Current current = new Current(x, y, DirectionType.SOUTH);
                        pieces.add(current);
                    } else if (dynamicPieceId == Constants.TILE_ID_CRATE) {
                        Crate crate = new Crate(x, y);
                        pieces.add(crate);
                    } else if (dynamicPieceId == Constants.TILE_ID_GLASSTANK) {
                        GlassTank glasstank = new GlassTank(x, y);
                        pieces.add(glasstank);
                    } else if (dynamicPieceId == Constants.TILE_ID_EMP) {
                        Emp emp = new Emp(x, y);
                        pieces.add(emp);
                    } else if (dynamicPieceId == Constants.TILE_ID_SENSOR) {
                        Sensor sensor = new Sensor(x, y);
                        pieces.add(sensor);
                    } else if (dynamicPieceId == Constants.TILE_ID_EMITTER_WEST) {
                        Emitter emitter = new Emitter(x, y, DirectionType.WEST);
                        pieces.add(emitter);
                    } else if (dynamicPieceId == Constants.TILE_ID_EMITTER_NORTH) {
                        Emitter emitter = new Emitter(x, y, DirectionType.NORTH);
                        pieces.add(emitter);
                    } else if (dynamicPieceId == Constants.TILE_ID_EMITTER_EAST) {
                        Emitter emitter = new Emitter(x, y, DirectionType.EAST);
                        pieces.add(emitter);
                    } else if (dynamicPieceId == Constants.TILE_ID_EMITTER_SOUTH) {
                        Emitter emitter = new Emitter(x, y, DirectionType.SOUTH);
                        pieces.add(emitter);
                    }
                }

                int protagonistTileId = tiledMap.getTileId(Position.fromCoordinates(x, y), Constants.LAYER_INDEX_ACTORS);

                if (protagonistTileId == Constants.TILE_ID_PROTAGONIST) {
                    protagonist = new Protagonist(x, y);
                }

            }
        }

        java.util.Map<Position, SwitchableHolder> switchableHolders = pieces
                .stream()
                .filter(e -> e instanceof SwitchableHolder)
                .collect(Collectors.toMap(e -> Position.fromCoordinates(e.positionX, e.positionY), e -> (SwitchableHolder) e));

        java.util.Map<Position, ActivatableHolder> activatableHolders = pieces
                .stream()
                .filter(e -> e instanceof ActivatableHolder)
                .collect(Collectors.toMap(e -> Position.fromCoordinates(e.positionX, e.positionY), e -> (ActivatableHolder) e));

        java.util.Map<Position, Switchable> switchables = pieces
                .stream()
                .filter(e -> e instanceof Switchable)
                .collect(Collectors.toMap(e -> Position.fromCoordinates(e.positionX, e.positionY), e -> (Switchable) e));

        java.util.Map<Position, Activatable> activatables = pieces
                .stream()
                .filter(e -> e instanceof Activatable)
                .collect(Collectors.toMap(e -> Position.fromCoordinates(e.positionX, e.positionY), e -> (Activatable) e));

        List<TiledLineObject> connectors = tiledMap.getObjectGroups().get("connectors").getObjects().values().stream().map(e -> (TiledLineObject)e).toList();
        for (TiledLineObject connector : connectors) {
            TiledLineObject.Point start = connector.getPoints().get(0);
            TiledLineObject.Point end = connector.getPoints().get(1);
            Position startPosition = Position.fromCoordinates((float)((int)start.x / tiledMap.getTileWidth()), (float)((int)start.y / tiledMap.getTileHeight()));
            Position endPosition = Position.fromCoordinates((float)((int)end.x / tiledMap.getTileWidth()), (float)((int)end.y / tiledMap.getTileHeight()));
            SwitchableHolder switchableHolder = switchableHolders.get(startPosition);
            if (switchableHolder != null) {
                Switchable switchable = switchables.get(endPosition);
                switchableHolder.addSwitchable(switchable);
            } else {
                ActivatableHolder activatableHolder = activatableHolders.get(startPosition);
                Activatable activatable = activatables.get(endPosition);
                activatableHolder.addActivatable(activatable);
            }

        }

        return Map.instance(tiles, pieces, protagonist);
    }
}
