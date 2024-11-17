public class Conveyor extends DynamicPiece implements Switchable {

    private DirectionType directionType;

    public Conveyor(int x, int y, DirectionType directionType) {
        super(x, y);
        this.directionType = directionType;
    }

    public DirectionType getDirectionType() {
        return directionType;
    }

    @Override
    public boolean passable(DirectionType directionType) {
        return this.directionType == directionType;
    }

    @Override
    public boolean transparent() {
        return true;
    }

    @Override
    public int numberOfStates() {
        return 2;
    }

    @Override
    public int currentState() {
        return directionType == DirectionType.WEST || directionType == DirectionType.NORTH ? 0 : 1;
    }

    @Override
    public void switchToNextState() {
        directionType = DirectionType.reverse(directionType);
    }
}
