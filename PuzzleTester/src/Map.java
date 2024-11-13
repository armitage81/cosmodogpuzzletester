import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Map {

    private Protagonist protagonist;
    private List<Tile> tiles = new ArrayList<>();
    private List<Piece> pieces = new ArrayList<>();
    private FixedSizeQueue<Portal> portals = new FixedSizeQueue<Portal>(2);

    public static Map instance(List<Tile> tiles, List<Piece> pieces, Protagonist protagonist) {
        Map map = new Map();
        map.tiles = tiles;
        map.pieces = pieces;
        map.protagonist = protagonist;
        return map;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void createPortal(Portal portal) {
        portals.offer(portal);
    }

    public void clearPortals() {
        portals.clear();
    }

    public List<Portal> portals() {
        return Lists.newArrayList(portals.iterator());
    }

    public boolean portalExists(Position position, DirectionType directionType) {
        for (Portal portal : portals) {
            if (portal.position.equals(position) && portal.directionType == directionType) {
                return true;
            }
        }
        return false;
    }

    public Tile tileAtPosition(Position position) {
        return tileAtPosition((int)position.getX(), (int)position.getY());
    }
    public Tile tileAtPosition(int x, int y) {
        return tiles.get(x + y * Constants.FIELD_WIDTH);
    }

    public void setTileAtPosition(int x, int y, Tile tile) {
        setTileAtPosition(Position.fromCoordinates(x, y), tile);
    }
    public void setTileAtPosition(Position position, Tile tile) {
        tiles.set((int)position.getX() + (int)position.getY() * Constants.FIELD_WIDTH, tile);
    }

    public Optional<Piece> pieceAtPosition(Position position) {
        return pieceAtPosition((int)position.getX(), (int)position.getY());
    }
    public Optional<Piece> pieceAtPosition(int x, int y) {
        return pieces.stream().filter(e -> e.positionX == x && e.positionY == y).findFirst();
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public Protagonist getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(Protagonist protagonist) {
        this.protagonist = protagonist;
    }

    public boolean positionPassable(Position position) {
        Tile tile = tileAtPosition((int)position.getX(), (int)position.getY());
        Optional<Piece> piece = pieceAtPosition((int)position.getX(), (int)position.getY());
        return tile.passable() && (piece.isEmpty() || piece.get().passable());
    }

    public boolean positionPenetrable(Position position) {
        Tile tile = tileAtPosition((int)position.getX(), (int)position.getY());
        Optional<Piece> piece = pieceAtPosition((int)position.getX(), (int)position.getY());
        return tile.transparent() && (piece.isEmpty() || piece.get().transparent());
    }

    public Position protagonistsEnvisionedPosition(Protagonist protagonist) {

        Position envisionedPosition;

        Position protagonistsPosition = Position.fromCoordinates(protagonist.positionX, protagonist.positionY);
        DirectionType protagonistsDirection = protagonist.getDirection();

        Position facedAdjacentPosition = DirectionType.facedAdjacentPosition(protagonistsPosition, protagonistsDirection);

        DirectionType directionFacingProtagonist = DirectionType.reverse(protagonist.getDirection());

        if (portalExists(facedAdjacentPosition, directionFacingProtagonist)) {
            Optional<Portal> otherPortal = Optional.empty();
            for (Portal portal : portals()) {
                if (!portal.position.equals(facedAdjacentPosition) || portal.directionType != directionFacingProtagonist) {
                    otherPortal = Optional.of(portal);
                    break;
                }
            }
            if (otherPortal.isPresent()) {
                envisionedPosition = DirectionType.facedAdjacentPosition(otherPortal.get().position, otherPortal.get().directionType);
            } else {
                envisionedPosition = facedAdjacentPosition;
            }
        } else {
            envisionedPosition = facedAdjacentPosition;
        }

        return envisionedPosition;
    }

    public void goToEnvisionedPosition(Protagonist protagonist) {
        Position envisionedPosition = protagonistsEnvisionedPosition(protagonist);
        protagonist.positionX = (int)envisionedPosition.getX();
        protagonist.positionY = (int)envisionedPosition.getY();
    }

    public boolean protagonistCanGoStraight(Protagonist protagonist) {
        return positionPassable(protagonistsEnvisionedPosition(protagonist));
    }

}
