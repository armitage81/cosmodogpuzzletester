public class Wall extends Tile {

    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable() {
        return false;
    }

    @Override
    public boolean transparent() {
        return false;
    }
}
