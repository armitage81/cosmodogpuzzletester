import com.google.common.collect.Lists;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<Piece> piecesAtPosition(Position position) {
        return piecesAtPosition((int)position.getX(), (int)position.getY());
    }
    public Set<Piece> piecesAtPosition(int x, int y) {
        return pieces.stream().filter(e -> e.positionX == x && e.positionY == y).collect(Collectors.toSet());
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

    public boolean positionPenetrable(Position position) {
        Tile tile = tileAtPosition((int)position.getX(), (int)position.getY());
        boolean impenetrablePiece =
                piecesAtPosition((int)position.getX(), (int)position.getY())
                        .stream()
                        .anyMatch(p -> !p.transparent());
        return tile.transparent() && !impenetrablePiece;
    }

    public void moveProtagonist() {
        Entrance protagonistsTargetEntrance = protagonistsTargetEntrance();
        Optional<Crate> optCrate = piece(protagonistsTargetEntrance.getPosition(), Crate.class);
        if (optCrate.isPresent()) {
            Crate crate = optCrate.get();
            Entrance cratesTargetEntrance = cratesTargetEntrance(crate, protagonistsTargetEntrance.getEntranceDirection());
            boolean cratesTargetEntrancePassable = passable(crate, cratesTargetEntrance);
            if (cratesTargetEntrancePassable) {
                crate.positionX = (int)cratesTargetEntrance.getPosition().getX();
                crate.positionY = (int)cratesTargetEntrance.getPosition().getY();
                protagonist.positionX = (int)protagonistsTargetEntrance.getPosition().getX();
                protagonist.positionY = (int)protagonistsTargetEntrance.getPosition().getY();
            }
        } else {
            boolean protagonistsTargetEntrancePassable = passable(protagonist, protagonistsTargetEntrance);
            if (protagonistsTargetEntrancePassable) {
                protagonist.positionX = (int)protagonistsTargetEntrance.getPosition().getX();
                protagonist.positionY = (int)protagonistsTargetEntrance.getPosition().getY();
            }
        }

    }

    private Entrance protagonistsTargetEntrance() {
        return targetEntrance(protagonist, protagonist.getDirection());
    }

    private Entrance cratesTargetEntrance(Crate crate, DirectionType directionType) {
        return targetEntrance(crate, directionType);
    }

    private Entrance targetEntrance(Actor actor, DirectionType directionType) {

        Position envisionedPosition;
        DirectionType envisionedPositionEntrance = directionType;

        Position protagonistsPosition = Position.fromCoordinates(actor.positionX, actor.positionY);
        DirectionType protagonistsDirection = directionType;

        Position facedAdjacentPosition = DirectionType.facedAdjacentPosition(protagonistsPosition, protagonistsDirection);

        DirectionType directionFacingProtagonist = DirectionType.reverse(directionType);

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
                envisionedPositionEntrance = otherPortal.get().directionType;
            } else {
                envisionedPosition = facedAdjacentPosition;
            }
        } else {
            envisionedPosition = facedAdjacentPosition;
        }

        return Entrance.instance(envisionedPosition, envisionedPositionEntrance);
    }

    public <T extends Piece> Optional<T> piece(int x, int y, Class<T> clazz) {
        return piece(Position.fromCoordinates(x, y), clazz);
    }

    public <T extends Piece> Optional<T> piece(Position position, Class<T> clazz) {
        Set<Piece> pieces = piecesAtPosition((int)position.getX(), (int)position.getY());
        return pieces.stream().filter(p -> p.getClass() == clazz).map(p -> (T)p).findFirst();
    }

    public boolean passable(Actor actor, Entrance entrance) {
        Position position = entrance.getPosition();
        DirectionType entranceDirection = entrance.getEntranceDirection();
        Tile tile = tileAtPosition((int)position.getX(), (int)position.getY());
        boolean impassablePiece =
                piecesAtPosition((int)position.getX(), (int)position.getY())
                        .stream()
                        .anyMatch(p -> !p.passable(actor, entranceDirection));
        return tile.passable(actor, entranceDirection) && !impassablePiece;
    }

}
