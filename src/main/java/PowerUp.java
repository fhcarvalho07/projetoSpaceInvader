public abstract class PowerUp extends Element {
    protected int movementCounter = 0;
    protected static final int MOVEMENT_DELAY = 6;

    public PowerUp(int x, int y) {
        super(x, y);
    }

    public void move() {
        position = new Position(position.getX(), position.getY() + 1);
    }

    public boolean isOffScreen(int height) {
        return position.getY() >= height;
    }

    public abstract void applyEffect(SpaceShip spaceShip);
}
