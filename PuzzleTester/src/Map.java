import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Map {

    Protagonist protagonist;
    List<Tile> tiles = new ArrayList<>();
    List<Piece> pieces = new ArrayList<>();

    public static Map instance(List<Tile> tiles, List<Piece> pieces, Protagonist protagonist) {
        Map map = new Map();
        map.tiles = tiles;
        map.pieces = pieces;
        map.protagonist = protagonist;
        return map;
    }

    public Tile tileAtPosition(Position position) {
        return tileAtPosition((int)position.getX(), (int)position.getY());
    }
    public Tile tileAtPosition(int x, int y) {
        return tiles.get(x + y * Constants.FIELD_WIDTH);
    }


    public Optional<Piece> pieceAtPosition(Position position) {
        return pieceAtPosition((int)position.getX(), (int)position.getY());
    }
    public Optional<Piece> pieceAtPosition(int x, int y) {
        return pieces.stream().filter(e -> e.positionX == x && e.positionY == y).findFirst();
    }

    public Protagonist getProtagonist() {
        return protagonist;
    }

    public boolean positionPassable(Position position) {
        Tile tile = tileAtPosition((int)position.getX(), (int)position.getY());
        Optional<Piece> piece = pieceAtPosition((int)position.getX(), (int)position.getY());
        return tile.passable() && (piece.isEmpty() || piece.get().passable());
    }

    public boolean positionPenetrable(Position position) {
        Tile tile = tileAtPosition((int)position.getX(), (int)position.getY());
        Optional<Piece> piece = pieceAtPosition((int)position.getX(), (int)position.getY());
        return tile.penetrable() && (piece.isEmpty() || piece.get().penetrable());
    }

    public List<Position> rayPositions(Protagonist protagonist) {
        List<Position> positionsWithRay = new ArrayList<>();

        Position nextPosition = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);
        Actor.DirectionType directionType = protagonist.getDirection();
        if (directionType == Actor.DirectionType.EAST) {
            nextPosition = nextPosition.shifted(1, 0);
            while (positionPenetrable(nextPosition)) {
                positionsWithRay.add(nextPosition);
                nextPosition = nextPosition.shifted(1, 0);
            }
        } else if (directionType == Actor.DirectionType.WEST) {
            nextPosition = nextPosition.shifted(-1, 0);
            while (positionPenetrable(nextPosition)) {
                positionsWithRay.add(nextPosition);
                nextPosition = nextPosition.shifted(-1, 0);
            }
        } else if (directionType == Actor.DirectionType.NORTH) {
            nextPosition = nextPosition.shifted(0, -1);
            while (positionPenetrable(nextPosition)) {
                positionsWithRay.add(nextPosition);
                nextPosition = nextPosition.shifted(0, -1);
            }
        } else {
            nextPosition = nextPosition.shifted(0, 1);
            while (positionPenetrable(nextPosition)) {
                positionsWithRay.add(nextPosition);
                nextPosition = nextPosition.shifted(0, 1);
            }
        }
        return positionsWithRay;
    }

    public Position rayTargetPosition(Protagonist protagonist) {

        Position nextPosition = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);
        Actor.DirectionType directionType = protagonist.getDirection();
        if (directionType == Actor.DirectionType.EAST) {
            nextPosition = nextPosition.shifted(1, 0);
            while (positionPenetrable(nextPosition)) {
                nextPosition = nextPosition.shifted(1, 0);
            }
        } else if (directionType == Actor.DirectionType.WEST) {
            nextPosition = nextPosition.shifted(-1, 0);
            while (positionPenetrable(nextPosition)) {
                nextPosition = nextPosition.shifted(-1, 0);
            }
        } else if (directionType == Actor.DirectionType.NORTH) {
            nextPosition = nextPosition.shifted(0, -1);
            while (positionPenetrable(nextPosition)) {
                nextPosition = nextPosition.shifted(0, -1);
            }
        } else {
            nextPosition = nextPosition.shifted(0, 1);
            while (positionPenetrable(nextPosition)) {
                nextPosition = nextPosition.shifted(0, 1);
            }
        }
        return nextPosition;
    }

    public boolean protagonistCanGoStraight(Protagonist protagonist) {
        return positionPassable(protagonist.envisionedPosition());
    }

}
