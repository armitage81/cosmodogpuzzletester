package puzzletester.interfaces;

import puzzletester.model.Map;
import puzzletester.model.elements.Actor;

public interface PresenceDetector {

    void presenceDetected(Map map, Actor presence);
    void presenceLost(Map map);

}
