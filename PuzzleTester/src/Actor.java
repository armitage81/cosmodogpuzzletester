public abstract class Actor extends Piece {



    private DirectionType direction;

    public Actor(int x, int y) {

        super(x, y);
        direction = DirectionType.EAST;
    }

    public DirectionType getDirection() {
        return direction;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

}
