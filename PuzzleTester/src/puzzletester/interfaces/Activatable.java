package puzzletester.interfaces;

import puzzletester.model.Map;

public interface Activatable {

        void activate();

        void deactivate();

        boolean isActive();

        boolean canActivate(Map map);

        boolean canDeactivate(Map map);
}
