import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Bullet {
    private Position position;

    public Bullet(int x, int y) {
        this.position = new Position(x, y);
    }

    public void move() {
        position.setY(position.getY() - 1);
    }

    public boolean isOffScreen() {
        return position.getY() < 0;
    }

    public void draw(TextGraphics graphics) {
        graphics.setCharacter(position.getX(), position.getY(), TextCharacter.fromCharacter('|')[0]);
    }

    public Position getPosition() {
        return position;
    }
}
