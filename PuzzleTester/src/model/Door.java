package model;

public class Door extends DynamicPiece implements Switchable {

    public boolean open;

    public Door(int x, int y, boolean open) {
        super(x, y);
        this.open = open;
    }

    @Override
    public int numberOfStates() {
        return 2;
    }

    @Override
    public int currentState() {
        return open ? 1 : 0;
    }

    @Override
    public void switchToNextState() {
        open = !open;
    }
}
