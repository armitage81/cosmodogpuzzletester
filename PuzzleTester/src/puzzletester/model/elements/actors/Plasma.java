package puzzletester.model.elements.actors;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import puzzletester.Constants;
import puzzletester.interfaces.Rotational;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;

public class Plasma extends Actor implements Rotational {

    private DirectionType direction;

    public Plasma(int x, int y, DirectionType direction) {
        super(x, y);
        this.direction = direction;
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return true;
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
}
