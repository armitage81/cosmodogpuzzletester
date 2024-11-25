import org.newdawn.slick.Image;

public class Protagonist extends Actor implements Rotational {

    private DirectionType direction;

    public Protagonist(int x, int y) {
        super(x, y);
        direction = DirectionType.EAST;
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

    @Override
    public DirectionType getDirection() {
        return direction;
    }

    @Override
    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }
}
