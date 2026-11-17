import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Shield extends PowerUp {
    public Shield(int x, int y) {
        super(x, y);
    }

    @Override
    public void applyEffect(SpaceShip spaceShip) {
        spaceShip.activateShield();
    }

    @Override
    public void draw(TextGraphics graphics) {
        graphics.setCharacter(position.getX(), position.getY(), TextCharacter.fromCharacter('|')[0]);
    }
}


