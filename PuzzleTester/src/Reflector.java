import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Reflector extends DynamicPiece implements Switchable {

    private ReflectionType reflectionType;

    public Reflector(int x, int y, ReflectionType reflectionType) {
        super(x, y);
        this.reflectionType = reflectionType;
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
    public int numberOfStates() {
        return 4;
    }

    @Override
    public int currentState() {
        return 0;
    }

    @Override
    public void switchToNextState() {
        reflectionType = ReflectionType.next(reflectionType);
    }

    public ReflectionType getReflectionType() {
        return reflectionType;
    }

    @Override
    public Image getImage() {

        if (getReflectionType() == ReflectionType.NORTH_WEST) {
            return Constants.SPRITE_SHEET.getSprite(0, 3);
        } else if (getReflectionType() == ReflectionType.NORTH_EAST) {
            return Constants.SPRITE_SHEET.getSprite(1, 3);
        } else if (getReflectionType() == ReflectionType.SOUTH_EAST) {
            return Constants.SPRITE_SHEET.getSprite(2, 3);
        } else  {
            return Constants.SPRITE_SHEET.getSprite(3, 3);
        }


    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }
}
