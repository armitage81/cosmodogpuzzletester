package puzzletester.model;

import com.google.common.collect.Lists;
import puzzletester.Constants;
import puzzletester.FixedSizeQueue;
import puzzletester.model.elements.*;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.Piece;
import puzzletester.model.elements.Tile;
import puzzletester.model.elements.actors.Plasma;
import puzzletester.model.elements.actors.Protagonist;
import puzzletester.model.elements.dynamicpieces.Emitter;
import tiled.Position;

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
        Optional<MoveableActor> optMoveableActor = piece(protagonistsTargetEntrance.getPosition(), MoveableActor.class);
        if (optMoveableActor.isPresent()) {
            MoveableActor moveableActor = optMoveableActor.get();
            Entrance moveableActorsTargetEntrance = moveableActorsTargetEntrance(moveableActor, protagonistsTargetEntrance.getEntranceDirection());
            boolean moveableActorsTargetEntrancePassable = passable(moveableActor, moveableActorsTargetEntrance);
            if (moveableActorsTargetEntrancePassable) {
                moveableActor.positionX = (int)moveableActorsTargetEntrance.getPosition().getX();
                moveableActor.positionY = (int)moveableActorsTargetEntrance.getPosition().getY();
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

    public void updatePlasma() {
        List<Emitter> emitters = getPieces().stream().filter(p -> p instanceof Emitter).map(p -> (Emitter)p).toList();
        for (Emitter emitter : emitters) {
            Plasma plasma = emitter.getPlasma();
            if (plasma == null) {
                plasma = new Plasma(emitter.positionX, emitter.positionY, emitter.getDirectionType(), emitter.isWeak());
                Entrance targetEntrance = targetEntrance(plasma, plasma.getDirection());
                if (passable(plasma, targetEntrance)) {
                    emitter.setPlasma(plasma);
                    getPieces().add(plasma);
                    plasma.positionX = (int)targetEntrance.getPosition().getX();
                    plasma.positionY = (int)targetEntrance.getPosition().getY();
                    plasma.setDirection(targetEntrance.getEntranceDirection());
                }
            } else {
                final Entrance targetEntrance = targetEntrance(plasma, plasma.getDirection());
                if (passable(plasma, targetEntrance)) {
                    plasma.positionX = (int)targetEntrance.getPosition().getX();
                    plasma.positionY = (int)targetEntrance.getPosition().getY();
                    plasma.setDirection(targetEntrance.getEntranceDirection());
                } else {

                    boolean weakPlasma = plasma.isWeak();

                    if (weakPlasma) {
                        emitter.setPlasma(null);
                        getPieces().remove(plasma);
                    } else {
                        plasma.setDirection(DirectionType.reverse(plasma.getDirection()));
                        Entrance targetEntranceReverse = targetEntrance(plasma, plasma.getDirection());
                        if (passable(plasma, targetEntranceReverse)) {
                            plasma.positionX = (int) targetEntranceReverse.getPosition().getX();
                            plasma.positionY = (int) targetEntranceReverse.getPosition().getY();
                            plasma.setDirection(targetEntranceReverse.getEntranceDirection());
                        } else {
                            emitter.setPlasma(null);
                            getPieces().remove(plasma);
                        }
                    }
                }
            }

        }
    }

    private Entrance protagonistsTargetEntrance() {
        return targetEntrance(protagonist, protagonist.getDirection());
    }

    private Entrance moveableActorsTargetEntrance(MoveableActor moveableActor, DirectionType directionType) {
        return targetEntrance(moveableActor, directionType);
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

    public <T extends Element> Optional<T> element(int x, int y, Class<T> clazz) {
        return element(Position.fromCoordinates(x, y), clazz);
    }

    public <T extends Element> Optional<T> element(Position position, Class<T> clazz) {
        Set<Piece> elements = piecesAtPosition((int)position.getX(), (int)position.getY());
        if (protagonist.positionX == position.getX() && protagonist.positionY == position.getY()) {
            elements.add(protagonist);
        }
        return elements.stream().filter(p -> clazz.isAssignableFrom(p.getClass())).map(p -> (T)p).findFirst();
    }

    public <T extends Piece> Optional<T> piece(int x, int y, Class<T> clazz) {
        return piece(Position.fromCoordinates(x, y), clazz);
    }

    public <T extends Piece> Optional<T> piece(Position position, Class<T> clazz) {
        Set<Piece> pieces = piecesAtPosition((int)position.getX(), (int)position.getY());
        return pieces.stream().filter(p -> clazz.isAssignableFrom(p.getClass())).map(p -> (T)p).findFirst();
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
