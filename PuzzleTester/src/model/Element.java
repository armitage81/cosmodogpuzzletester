package model;

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
}
