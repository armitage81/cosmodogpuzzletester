import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class Element {

    public int positionX;
    public int positionY;

    public Element(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    @Override
    public String toString() {
        return positionX + "/" + positionY;
    }

    public abstract boolean passable(Actor actor, DirectionType directionType);

    public abstract boolean transparent();

    public abstract Image getImage();

    public abstract void render(GameContainer gc, Graphics g, Map map);

}
