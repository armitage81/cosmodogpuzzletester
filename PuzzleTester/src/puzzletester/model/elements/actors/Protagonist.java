package puzzletester.model.elements.actors;

import puzzletester.interfaces.Small;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;
import puzzletester.interfaces.Rotational;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import puzzletester.Constants;
import puzzletester.model.DirectionType;

public class Protagonist extends Actor implements Rotational, Small {

    private DirectionType direction;

    public Protagonist(int x, int y) {
        super(x, y);
        direction = DirectionType.EAST;
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
        return Constants.SPRITE_SHEET.getSprite(5, 0);
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        long timestamp = System.currentTimeMillis() / 100;
        boolean blinking = timestamp % 2 == 0;

        if (blinking) {
            getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }
    }

    @Override
    public DirectionType getDirection() {
        return direction;
    }

    @Override
    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

}
