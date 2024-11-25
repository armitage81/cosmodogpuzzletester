import org.newdawn.slick.Image;

public class Conveyor extends DynamicPiece implements Switchable {

    private DirectionType directionType;

    public Conveyor(int x, int y, DirectionType directionType) {
        super(x, y);
        this.directionType = directionType;
    }

    public DirectionType getDirectionType() {
        return directionType;
    }

    @Override
    public boolean passable(DirectionType directionType) {
        return this.directionType == directionType;
    }

    @Override
    public boolean transparent() {
        return true;
    }

    @Override
    public int numberOfStates() {
        return 2;
    }

    @Override
    public int currentState() {
        return directionType == DirectionType.WEST || directionType == DirectionType.NORTH ? 0 : 1;
    }

    @Override
    public void switchToNextState() {
        directionType = DirectionType.reverse(directionType);
    }

    @Override
    public Image getImage() {
        if (getDirectionType() == DirectionType.WEST) {
            return Constants.SPRITE_SHEET.getSprite(4, 3);
        } else if (getDirectionType() == DirectionType.NORTH) {
            return Constants.SPRITE_SHEET.getSprite(5, 3);
        } else if (getDirectionType() == DirectionType.EAST) {
            return Constants.SPRITE_SHEET.getSprite(6, 3);
        } else {
            return Constants.SPRITE_SHEET.getSprite(7, 3);
        }
    }
}
