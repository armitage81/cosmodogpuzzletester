public class Jammer extends DynamicPiece implements Pressable {

    public Jammer(int x, int y) {
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

    @Override
    public void press(Map map, Protagonist protagonist) {
        map.clearPortals();
    }
}
