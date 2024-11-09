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

    public Position envisionedPosition() {
        Position envisionedPosition = Position.fromCoordinates(positionX, positionY);
        DirectionType direction = getDirection();
        if (direction == DirectionType.EAST) {
            envisionedPosition = envisionedPosition.shifted(1, 0);
        } else if (direction == DirectionType.WEST) {
            envisionedPosition = envisionedPosition.shifted(-1, 0);
        } else if (direction == DirectionType.NORTH) {
            envisionedPosition = envisionedPosition.shifted(0, -1);
        } else {
            envisionedPosition = envisionedPosition.shifted(0, 1);
        }
        return envisionedPosition;
    }

    public void goToEnvisionedPosition() {
        Position envisionedPosition = envisionedPosition();
        this.positionX = (int)envisionedPosition.getX();
        this.positionY = (int)envisionedPosition.getY();
    }


}
