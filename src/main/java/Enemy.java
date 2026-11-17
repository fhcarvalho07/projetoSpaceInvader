import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Enemy extends Element {
    public Enemy(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(TextGraphics graphics) {
        graphics.setCharacter(position.getX(), position.getY(), TextCharacter.fromCharacter('\'')[0]);
    }
}
