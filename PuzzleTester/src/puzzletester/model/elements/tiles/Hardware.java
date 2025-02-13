package puzzletester.model.elements.tiles;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import puzzletester.*;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.Tile;

public class Hardware extends Tile {

    public Hardware(int x, int y) {
        super(x, y);
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
        return Constants.SPRITE_SHEET.getSprite(0, 0);
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }
}
