public class Smoke extends Tile {

    public Smoke(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable() {
        return true;
    }

    @Override
    public boolean transparent() {
        return false;
    }
}
