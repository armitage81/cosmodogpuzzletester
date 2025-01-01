package puzzletester.model.elements.tiles;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import puzzletester.*;
import puzzletester.interfaces.Small;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.actors.Protagonist;
import puzzletester.model.elements.Tile;

public class Interstice extends Tile {

    public Interstice(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return actor instanceof Small;
    }

    @Override
    public boolean transparent() {
        return true;
    }

    @Override
    public Image getImage() {
        return Constants.SPRITE_SHEET.getSprite(2, 2);
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }
}
