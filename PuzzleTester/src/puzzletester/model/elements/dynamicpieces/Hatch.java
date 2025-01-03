package puzzletester.model.elements.dynamicpieces;

import puzzletester.*;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.DynamicPiece;
import puzzletester.interfaces.Activatable;
import puzzletester.interfaces.Switchable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import puzzletester.model.elements.MoveableActor;

public class Hatch extends DynamicPiece implements Switchable, Activatable {

    public boolean open;
    public boolean initialOpen;

    public Hatch(int x, int y, boolean open) {
        super(x, y);
        this.open = open;
        this.initialOpen = open;
    }

    @Override
    public int numberOfStates() {
        return 2;
    }

    @Override
    public int currentState() {
        return open ? 1 : 0;
    }

    @Override
    public void switchToNextState() {
        open = !open;
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return open;
    }

    @Override
    public boolean transparent() {
        return open;
    }

    @Override
    public Image getImage() {
        if (open) {
            return Constants.SPRITE_SHEET.getSprite(3, 0);
        } else {
            return Constants.SPRITE_SHEET.getSprite(1, 0);
        }

    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    @Override
    public void activate() {
        this.open = !initialOpen;
    }

    @Override
    public void deactivate() {
        this.open = initialOpen;
    }

    @Override
    public boolean isActive() {
        return open != initialOpen;
    }

    @Override
    public boolean canActivate(Map map) {
        return true;
    }

    @Override
    public boolean canDeactivate(Map map) {
        return map.element(positionX, positionY, Actor.class).isEmpty();
    }
}
