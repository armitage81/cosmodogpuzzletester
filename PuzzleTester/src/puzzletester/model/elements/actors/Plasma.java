package puzzletester.model.elements.actors;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import puzzletester.Constants;
import puzzletester.interfaces.Immaterial;
import puzzletester.interfaces.Rotational;
import puzzletester.interfaces.Small;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.MoveableActor;

public class Plasma extends Actor implements Rotational, Small, Immaterial {

    private DirectionType direction;
    private boolean weak;

    public Plasma(int x, int y, DirectionType direction, boolean weak) {
        super(x, y);
        this.direction = direction;
        this.weak = weak;
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {

        return !(actor instanceof MoveableActor);
    }

    @Override
    public boolean transparent() {
        return true;
    }

    @Override
    public Image getImage() {
        return Constants.SPRITE_SHEET.getSprite(0, 4);
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    @Override
    public DirectionType getDirection() {
        return this.direction;
    }

    @Override
    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public boolean isWeak() {
        return weak;
    }
}
