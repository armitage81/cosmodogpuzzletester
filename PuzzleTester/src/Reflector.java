public class Reflector extends DynamicPiece implements Switchable {

    private ReflectionType reflectionType;

    public Reflector(int x, int y, ReflectionType reflectionType) {
        super(x, y);
        this.reflectionType = reflectionType;
    }

    @Override
    public boolean passable() {
        return false;
    }

    @Override
    public boolean penetrable() {
        return false;
    }

    @Override
    public int numberOfStates() {
        return 4;
    }

    @Override
    public int currentState() {
        return 0;
    }

    @Override
    public void switchToNextState() {
        reflectionType = ReflectionType.next(reflectionType);
    }

    public ReflectionType getReflectionType() {
        return reflectionType;
    }
}
