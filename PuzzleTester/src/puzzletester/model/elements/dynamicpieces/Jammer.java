package puzzletester.model.elements.dynamicpieces;

import puzzletester.*;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.DynamicPiece;
import puzzletester.model.elements.actors.Protagonist;
import puzzletester.interfaces.Pressable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Jammer extends DynamicPiece implements Pressable {

    public Jammer(int x, int y) {
        super(x, y);
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
    public void press(Map map, Protagonist protagonist) {
        map.clearPortals();
    }

    @Override
    public Image getImage() {
        return Constants.SPRITE_SHEET.getSprite(0, 2);
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        long time = System.currentTimeMillis() / 250;
        int phase = (int)(time % 5);
        if (phase <= 2) {
            getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }
    }
}
