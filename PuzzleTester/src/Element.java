public abstract class Element {

    public int positionX;
    public int positionY;

    public Element(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    @Override
    public String toString() {
        return positionX + "/" + positionY;
    }

    public abstract boolean passable(DirectionType directionType);

    public abstract boolean transparent();

}
