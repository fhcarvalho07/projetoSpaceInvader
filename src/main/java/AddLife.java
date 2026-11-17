import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;

public class AddLife extends PowerUp {
    public AddLife(int x, int y) {
        super(x, y);
    }

    @Override
    public void applyEffect(SpaceShip spaceShip) {
        spaceShip.addLife();
    }

    @Override
    public void draw(TextGraphics graphics) {
        graphics.setCharacter(position.getX(), position.getY(), TextCharacter.fromCharacter('|')[0]);
    }
}


