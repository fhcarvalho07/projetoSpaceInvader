import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;

public class EnemyBullet extends Element {
    private int speedCounter = 0;
    private int movementDelay = 2;

    public EnemyBullet(int x, int y) {
        super(x, y);
    }

    public boolean updateMovementCounter() {
        speedCounter++;
        if (speedCounter < movementDelay) {
            return false;
        }
        speedCounter = 0;
        return true;
    }

    public void move() {
        position.setY(position.getY() + 1);
    }

    public boolean isOffScreen(int height) {
        return position.getY() >= height;
    }

    @Override
    public void draw(TextGraphics graphics) {
        graphics.setCharacter(position.getX(), position.getY(), TextCharacter.fromCharacter('*')[0]);
    }
}
