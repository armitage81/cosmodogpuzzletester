public class SmoothWall extends DynamicPiece {

    private boolean hasPortal = false;
    private int portalNumber = 0;

    public SmoothWall(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable() {
        return false;
    }

    @Override
    public boolean penetrable() {
        return false;
    }

    public void activatePortal() {
        this.hasPortal = true;
    }

    public void deactivatePortal() {
        this.hasPortal = false;
    }

    public void setPortalNumber(int number) {
        this.portalNumber = number;
    }

    public int getPortalNumber() {
        return portalNumber;
    }

    public boolean hasPortal() {
        return this.hasPortal;
    }

}
