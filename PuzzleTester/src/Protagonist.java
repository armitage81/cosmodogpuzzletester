import org.newdawn.slick.Image;

public class Protagonist extends Actor {

    public Protagonist(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(DirectionType directionType) {
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
}
