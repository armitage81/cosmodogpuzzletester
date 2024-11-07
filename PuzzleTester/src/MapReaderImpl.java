import model.Door;
import model.Element;
import model.Piece;
import model.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MapReaderImpl implements MapReader {

    public Map read(Path path) {

        String filler = "                                                                                   ";

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (lines.size() < Constants.FIELD_HEIGHT) {
            for (int i = lines.size(); i < Constants.FIELD_HEIGHT; i++) {
                lines.add("");
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.length() < Constants.FIELD_WIDTH) {
                line = line + filler.substring(0, Constants.FIELD_WIDTH - line.length());
                lines.set(i, line);
            }
        }

        List<Tile> tiles = new ArrayList<>();
        List<Piece> pieces = new ArrayList<>();
        for (String line : lines) {
            for (int i = 0; i < Constants.FIELD_WIDTH; i++) {
                char c = line.charAt(i);
                if (c == '#') {
                    Tile tile = new Tile();
                    tile.type = Tile.TileType.WALL;
                    tiles.add(tile);
                } else if (c == 'x') {
                    Tile tile = new Tile();
                    tile.type = Tile.TileType.OBSTACLE;
                    tiles.add(tile);
                } else if (c == 'c' || c == 'o') {

                    Tile tile = new Tile();
                    tile.type = Tile.TileType.EMPTY;
                    tiles.add(tile);

                    Door door = new Door();
                    door.open = c == 'c';
                    pieces.add(door);
                } else {
                    Tile tile = new Tile();
                    tile.type = Tile.TileType.EMPTY;
                    tiles.add(tile);
                }
            }
        }
        return Map.instance(tiles, pieces);
    }
}
