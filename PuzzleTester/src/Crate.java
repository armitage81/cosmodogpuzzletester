import org.newdawn.slick.Image;

public class Crate extends Actor {

    public Crate(int x, int y) {
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
        return null;
    }
}
