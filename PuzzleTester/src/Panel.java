public class Panel extends Tile {

    public Panel(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(DirectionType directionType) {
        return false;
    }

    @Override
    public boolean transparent() {
        return false;
    }

}
