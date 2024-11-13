public class Glass extends Tile {

    public Glass(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable() {
        return false;
    }

    @Override
    public boolean transparent() {
        return true;
    }
}
