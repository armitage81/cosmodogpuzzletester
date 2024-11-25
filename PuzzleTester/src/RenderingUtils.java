import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.List;
import java.util.Optional;

public class RenderingUtils {

    public static void translateToOffset(GameContainer gc, Graphics g) {
        float offsetX = (gc.getWidth() - Constants.FIELD_WIDTH * Constants.TILE_SIZE) / 2.0f;
        float offsetY = (gc.getHeight() - Constants.FIELD_HEIGHT * Constants.TILE_SIZE) / 2.0f;
        g.translate(offsetX, offsetY);
    }

    public static void resetTranslation(GameContainer gc, Graphics g) {
        g.resetTransform();
    }

}
