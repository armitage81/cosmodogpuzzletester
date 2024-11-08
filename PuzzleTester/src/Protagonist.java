public class Protagonist extends Actor {

    public Protagonist(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable() {
        return false;
    }

    @Override
    public boolean penetrable() {
        return false;
    }
}
