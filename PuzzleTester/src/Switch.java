import java.util.ArrayList;
import java.util.List;

public class Switch extends DynamicPiece {

    private final List<Switchable> switchables = new ArrayList<>();

    public Switch(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable() {
        return true;
    }

    @Override
    public boolean penetrable() {
        return true;
    }

    public void addSwitchable(Switchable switchable) {
        this.switchables.add(switchable);
    }

    public List<Switchable> getSwitchables() {
        return switchables;
    }
}
