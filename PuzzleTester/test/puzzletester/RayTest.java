package puzzletester;

import org.junit.jupiter.api.Test;
import puzzletester.model.elements.*;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.Ray;
import puzzletester.model.ReflectionType;
import puzzletester.model.elements.Piece;
import puzzletester.model.elements.Tile;
import puzzletester.model.elements.actors.Protagonist;
import puzzletester.model.elements.dynamicpieces.Reflector;
import puzzletester.model.elements.tiles.Hardware;
import puzzletester.model.elements.tiles.Space;
import tiled.Position;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RayTest {

    @Test
    public void testStraightRay() {
        Tile tile_0_0 = new Space(0, 0);
        Tile tile_1_0 = new Space(1, 0);
        Tile tile_2_0 = new Hardware(2, 0);

        Protagonist protagonist = new Protagonist(0, 0);
        protagonist.setDirection(DirectionType.EAST);

        List<Tile> tiles = new ArrayList<>();
        tiles.add(tile_0_0);
        tiles.add(tile_1_0);
        tiles.add(tile_2_0);

        List<Piece> pieces = new ArrayList<>();

        Map map = Map.instance(tiles, pieces, protagonist);
        Ray ray = Ray.create(map);
        assertEquals(1, ray.getRayPositions().size());
        assertTrue(ray.getTargetPosition().isPresent());
        assertEquals(Position.fromCoordinates(2, 0), ray.getTargetPosition().get());

    }

    @Test
    public void testShortestRay() {
        Tile tile_0_0 = new Space(0, 0);
        Tile tile_1_0 = new Hardware(1, 0);

        Protagonist protagonist = new Protagonist(0, 0);
        protagonist.setDirection(DirectionType.EAST);

        List<Tile> tiles = new ArrayList<>();
        tiles.add(tile_0_0);
        tiles.add(tile_1_0);

        List<Piece> pieces = new ArrayList<>();

        Map map = Map.instance(tiles, pieces, protagonist);
        Ray ray = Ray.create(map);
        assertEquals(0, ray.getRayPositions().size());
        assertTrue(ray.getTargetPosition().isPresent());
        assertEquals(Position.fromCoordinates(1, 0), ray.getTargetPosition().get());

    }

    @Test
    public void testLRay() {

        Map map = emptyMap();
        Tile tile_2_2 = new Hardware(2, 2);

        Protagonist protagonist = new Protagonist(0, 0);
        protagonist.setDirection(DirectionType.EAST);

        Reflector reflector = new Reflector(2, 0, ReflectionType.SOUTH_WEST);

        map.setTileAtPosition(2, 2, tile_2_2);

        map.addPiece(reflector);
        map.setProtagonist(protagonist);

        Ray ray = Ray.create(map);
        assertEquals(3, ray.getRayPositions().size());
        assertTrue(ray.getTargetPosition().isPresent());
        assertEquals(Position.fromCoordinates(2, 2), ray.getTargetPosition().get());
        assertEquals(DirectionType.SOUTH, ray.getLastDirection());
    }

    @Test
    public void testURay() {

        Map map = emptyMap();
        Tile tile_0_2 = new Hardware(0, 2);

        Protagonist protagonist = new Protagonist(0, 0);
        protagonist.setDirection(DirectionType.EAST);

        Reflector reflector1 = new Reflector(2, 0, ReflectionType.SOUTH_WEST);
        Reflector reflector2 = new Reflector(2, 2, ReflectionType.NORTH_WEST);

        map.setTileAtPosition(0, 2, tile_0_2);

        map.addPiece(reflector1);
        map.addPiece(reflector2);
        map.setProtagonist(protagonist);

        Ray ray = Ray.create(map);
        assertEquals(5, ray.getRayPositions().size());
        assertTrue(ray.getTargetPosition().isPresent());
        assertEquals(Position.fromCoordinates(0, 2), ray.getTargetPosition().get());
        assertEquals(DirectionType.WEST, ray.getLastDirection());
    }

    @Test
    public void testORay() {

        Map map = emptyMap();

        Protagonist protagonist = new Protagonist(0, 0);
        protagonist.setDirection(DirectionType.EAST);

        Reflector reflector1 = new Reflector(2, 0, ReflectionType.SOUTH_WEST);
        Reflector reflector2 = new Reflector(2, 1, ReflectionType.NORTH_WEST);
        Reflector reflector3 = new Reflector(1, 1, ReflectionType.NORTH_EAST);


        map.addPiece(reflector1);
        map.addPiece(reflector2);
        map.addPiece(reflector3);
        map.setProtagonist(protagonist);

        Ray ray = Ray.create(map);
        assertEquals(4, ray.getRayPositions().size());
        assertFalse(ray.getTargetPosition().isPresent());
        assertEquals(DirectionType.NORTH, ray.getLastDirection());
    }

    @Test
    public void testHittingProtagonist() {

        Map map = emptyMap();

        Protagonist protagonist = new Protagonist(0, 0);
        protagonist.setDirection(DirectionType.EAST);

        Reflector reflector1 = new Reflector(1, 0, ReflectionType.SOUTH_WEST);
        Reflector reflector2 = new Reflector(1, 1, ReflectionType.NORTH_WEST);
        Reflector reflector3 = new Reflector(0, 1, ReflectionType.NORTH_EAST);


        map.addPiece(reflector1);
        map.addPiece(reflector2);
        map.addPiece(reflector3);
        map.setProtagonist(protagonist);

        Ray ray = Ray.create(map);
        assertEquals(3, ray.getRayPositions().size());
        assertFalse(ray.getTargetPosition().isPresent());
        assertEquals(DirectionType.NORTH, ray.getLastDirection());
    }

    private Map emptyMap() {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < Constants.FIELD_WIDTH * Constants.FIELD_HEIGHT; i++) {
            tiles.add(new Space(i % Constants.FIELD_WIDTH, i / Constants.FIELD_WIDTH));
        }
        return Map.instance(tiles, new ArrayList<>(), new Protagonist(0, 0));
    }

}
