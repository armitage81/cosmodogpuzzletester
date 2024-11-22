import org.newdawn.slick.Image;

public class Door extends DynamicPiece implements Switchable {

    public boolean open;

    public Door(int x, int y, boolean open) {
        super(x, y);
        this.open = open;
    }

    @Override
    public int numberOfStates() {
        return 2;
    }

    @Override
    public int currentState() {
        return open ? 1 : 0;
    }

    @Override
    public void switchToNextState() {
        open = !open;
    }

    @Override
    public boolean passable(DirectionType directionType) {
        return open;
    }

    @Override
    public boolean transparent() {
        return open;
    }

    @Override
    public Image getImage() {
        if (open) {
            return Constants.SPRITE_SHEET.getSprite(3, 0);
        } else {
            return Constants.SPRITE_SHEET.getSprite(1, 0);
        }

    }
}
