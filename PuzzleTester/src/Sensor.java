import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.List;

public class Sensor extends DynamicPiece implements ActivatableHolder, PresenceDetector {

    private final List<Activatable> activatables = new ArrayList<>();

    public Sensor(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return actor instanceof Protagonist;
    }

    @Override
    public boolean transparent() {
        return true;
    }

    @Override
    public Image getImage() {
        return Constants.SPRITE_SHEET.getSprite(5, 2);
    }

    @Override
    public void render(GameContainer gc, Graphics g, Map map) {
        getImage().draw(positionX * Constants.TILE_SIZE, positionY * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    @Override
    public void addActivatable(Activatable activatable) {
        activatables.add(activatable);
    }

    @Override
    public List<Activatable> getActivatables() {
        return activatables;
    }

    @Override
    public void presenceDetected(Actor presence) {
        activatables.forEach(Activatable::activate);
    }

    @Override
    public void presenceLost() {
        activatables.forEach(Activatable::deactivate);
    }
}
