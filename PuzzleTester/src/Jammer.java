import org.newdawn.slick.Image;

public class Jammer extends DynamicPiece implements Pressable {

    public Jammer(int x, int y) {
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

    @Override
    public void press(Map map, Protagonist protagonist) {
        map.clearPortals();
    }

    @Override
    public Image getImage() {
        return Constants.SPRITE_SHEET.getSprite(0, 2);
    }
}
