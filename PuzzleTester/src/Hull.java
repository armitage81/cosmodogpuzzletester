import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Hull extends Tile {

    public Hull(int x, int y) {
        super(x, y);
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
    public Image getImage() {
        return Constants.SPRITE_SHEET.getSprite(6, 0);
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);

        long time = System.currentTimeMillis() / 250;
        if (time % 2 == 0) {
            g.setColor(new Color(Color.orange));
            Position position = Position.fromCoordinates(positionX, positionY);
            if (map.portalExists(position, DirectionType.NORTH)) {
                g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE / 2f);
            }
            if (map.portalExists(position, DirectionType.SOUTH)) {
                g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f, Constants.TILE_SIZE, Constants.TILE_SIZE / 2f);
            }
            if (map.portalExists(position, DirectionType.WEST)) {
                g.fillRect(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE / 2f, Constants.TILE_SIZE);
            }
            if (map.portalExists(position, DirectionType.EAST)) {
                g.fillRect(positionX * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE / 2f, Constants.TILE_SIZE);
            }
        }
    }
}
