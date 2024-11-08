public class Obstacle extends Tile {

    public Obstacle(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable() {
        return false;
    }

    @Override
    public boolean penetrable() {
        return true;
    }
}
