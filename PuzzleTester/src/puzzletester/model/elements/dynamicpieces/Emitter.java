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

public class Emitter extends DynamicPiece {

    private final DirectionType directionType;
    private Plasma plasma;

    public Emitter(int x, int y, DirectionType directionType) {
        super(x, y);
        this.directionType = directionType;
    }

    public DirectionType getDirectionType() {
        return directionType;
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
        if (plasma == null) {
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
}
