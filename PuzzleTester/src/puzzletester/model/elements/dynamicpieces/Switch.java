package puzzletester.model.elements.dynamicpieces;

import puzzletester.*;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.DynamicPiece;
import puzzletester.model.elements.actors.Protagonist;
import puzzletester.interfaces.Pressable;
import puzzletester.interfaces.Switchable;
import puzzletester.interfaces.SwitchableHolder;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.List;

public class Switch extends DynamicPiece implements Pressable, SwitchableHolder {

    private final List<Switchable> switchables = new ArrayList<>();

    public Switch(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return actor instanceof Protagonist;
    }

    @Override
    public boolean transparent() {
        return true;
    }

    public void addSwitchable(Switchable switchable) {
        this.switchables.add(switchable);
    }

    public List<Switchable> getSwitchables() {
        return switchables;
    }

    @Override
    public void press(Map map, Protagonist protagonist) {
        switchables.forEach(Switchable::switchToNextState);
    }

    public Image getImage() {
        return Constants.SPRITE_SHEET.getSprite(4, 0);
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }
}
