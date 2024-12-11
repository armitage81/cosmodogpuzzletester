package puzzletester.interfaces;

import puzzletester.model.Map;
import puzzletester.model.elements.actors.Protagonist;

public interface Pressable {

    void press(Map map, Protagonist protagonist);

}
