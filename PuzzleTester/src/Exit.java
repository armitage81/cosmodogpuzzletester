public class Exit extends Tile {

    public Exit(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable() {
        return true;
    }

    @Override
    public boolean transparent() {
        return true;
    }
}
