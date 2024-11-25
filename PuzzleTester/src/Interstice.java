import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Interstice extends Tile {

    public Interstice(int x, int y) {
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
    public Image getImage() {
        return Constants.SPRITE_SHEET.getSprite(0, 10);
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {

    }
}
