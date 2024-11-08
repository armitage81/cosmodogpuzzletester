package model;

public class Door extends DynamicPiece {

    public boolean open;

    public Door(int x, int y, boolean open) {
        super(x, y);
        this.open = open;
    }
}
