package puzzletester.model.elements.dynamicpieces;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import puzzletester.Constants;
import puzzletester.interfaces.Activatable;
import puzzletester.interfaces.Switchable;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.DynamicPiece;
import puzzletester.model.elements.actors.Plasma;

public class Emitter extends DynamicPiece implements Activatable, Switchable {

    private final DirectionType directionType;
    private boolean weak;
    private Plasma plasma;
    private final boolean initiallyOnNotOff;
    private boolean onNotOff;

    public Emitter(int x, int y, DirectionType directionType, boolean initiallyOnNotOff) {
        super(x, y);
        this.directionType = directionType;
        this.initiallyOnNotOff = initiallyOnNotOff;
        this.onNotOff = initiallyOnNotOff;
    }

    public DirectionType getDirectionType() {
        return directionType;
    }

    public boolean isWeak() {
        return weak;
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return false;
    }

    @Override
    public boolean transparent() {
        return false;
    }

    @Override
    public Image getImage() {
        if (weak) {
            if (getDirectionType() == DirectionType.WEST) {
                return Constants.SPRITE_SHEET.getSprite(5, 4);
            } else if (getDirectionType() == DirectionType.NORTH) {
                return Constants.SPRITE_SHEET.getSprite(6, 4);
            } else if (getDirectionType() == DirectionType.EAST) {
                return Constants.SPRITE_SHEET.getSprite(7, 4);
            } else {
                return Constants.SPRITE_SHEET.getSprite(8, 4);
            }
        } else {
            if (getDirectionType() == DirectionType.WEST) {
                return Constants.SPRITE_SHEET.getSprite(1, 4);
            } else if (getDirectionType() == DirectionType.NORTH) {
                return Constants.SPRITE_SHEET.getSprite(2, 4);
            } else if (getDirectionType() == DirectionType.EAST) {
                return Constants.SPRITE_SHEET.getSprite(3, 4);
            } else {
                return Constants.SPRITE_SHEET.getSprite(4, 4);
            }
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public Plasma getPlasma() {
        return plasma;
    }

    public void setPlasma(Plasma plasma) {
        this.plasma = plasma;
    }

    @Override
    public void activate() {
        this.onNotOff = !initiallyOnNotOff;
    }

    @Override
    public void deactivate() {
        this.onNotOff = initiallyOnNotOff;
    }

    @Override
    public boolean isActive() {
        return onNotOff != initiallyOnNotOff;
    }

    @Override
    public boolean canActivate(Map map) {
        return true;
    }

    @Override
    public boolean canDeactivate(Map map) {
        return true;
    }

    @Override
    public int numberOfStates() {
        return 2;
    }

    @Override
    public int currentState() {
        return onNotOff == initiallyOnNotOff ? 0 : 1;
    }

    @Override
    public void switchToNextState() {
        onNotOff = !onNotOff;
    }
}
