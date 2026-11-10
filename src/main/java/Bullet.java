import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Bullet extends Element{

    public Bullet(int x, int y) {
        super(x,y);
    }

    public void move() {
        position.setY(position.getY() - 1);
    }

    public boolean isOffScreen() {
        return position.getY() < 0;
    }

    @Override
    public void draw(TextGraphics graphics) {
        graphics.setCharacter(position.getX(), position.getY(), TextCharacter.fromCharacter('|')[0]);
    }
}
