import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;

public class SpaceShip {
    private Position position;

    public SpaceShip(int x, int y) {
        this.position = new Position(x, y);
    }

    public void draw(TextGraphics graphics) {
        graphics.setCharacter(position.getX(), position.getY(), TextCharacter.fromCharacter('-')[0]);
    }

}
