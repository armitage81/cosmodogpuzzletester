public class Floor extends Tile {

    public Floor(int x, int y) {
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
}
