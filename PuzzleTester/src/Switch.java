import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.List;

public class Switch extends DynamicPiece implements Pressable {

    private final List<Switchable> switchables = new ArrayList<>();

    public Switch(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(DirectionType directionType) {
        return true;
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
}
