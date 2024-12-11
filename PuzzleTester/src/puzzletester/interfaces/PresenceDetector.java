package puzzletester.interfaces;

import puzzletester.model.elements.Actor;

public interface PresenceDetector {

    void presenceDetected(Actor presence);
    void presenceLost();

}
