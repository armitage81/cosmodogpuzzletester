public class SmoothWall extends Tile {

    public SmoothWall(int x, int y) {
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
