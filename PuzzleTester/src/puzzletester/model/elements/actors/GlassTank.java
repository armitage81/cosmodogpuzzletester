package puzzletester.model.elements.actors;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import puzzletester.Constants;
import puzzletester.interfaces.Immaterial;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.MoveableActor;

public class GlassTank extends MoveableActor {

    public GlassTank(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return (actor instanceof Protagonist || actor instanceof Immaterial);
    }

    @Override
    public boolean transparent() {
        return true;
    }

    @Override
    public Image getImage() {
        return Constants.SPRITE_SHEET.getSprite(6, 2);
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }
}
