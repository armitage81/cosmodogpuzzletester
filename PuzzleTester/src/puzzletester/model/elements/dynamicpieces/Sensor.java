package puzzletester.model.elements.dynamicpieces;

import puzzletester.*;
import puzzletester.model.DirectionType;
import puzzletester.model.Map;
import puzzletester.model.elements.Actor;
import puzzletester.model.elements.DynamicPiece;
import puzzletester.model.elements.actors.Protagonist;
import puzzletester.interfaces.Activatable;
import puzzletester.interfaces.ActivatableHolder;
import puzzletester.interfaces.PresenceDetector;
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
        return true;
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
    public void presenceDetected(Map map, Actor presence) {
        for (Activatable activatable : activatables) {
            if (activatable.canActivate(map)) {
                activatable.activate();
            }
        }
    }

    @Override
    public void presenceLost(Map map) {
        for (Activatable activatable : activatables) {
            if (activatable.canDeactivate(map)) {
                activatable.deactivate();
            }
        }
    }
}
