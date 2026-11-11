import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;

public class SpaceShip extends Element {
    private int d = 0;
    private int speedCounter = 0;
    private int movementDelay = 4;

    public SpaceShip(int x, int y) {
        super(x, y);
    }

    public void setDirection(int d) {
        this.d = d;
    }

    public void move(int max_width) {
        if (d == 0) {
            return; // se a direcão for 0 não mexe
        }

        speedCounter++;
        if (speedCounter < movementDelay) {
            return; // Só anda de 4 em 4 frames para ser mais lenta
        }
        speedCounter = 0;

        int newX = position.getX() + d;


        if (newX < 0) {
            newX = 0;
            d = 0;
        } else if (newX >= max_width) {
            newX = max_width - 1;
            d = 0;
        }

        position.setX(newX);
    }

    @Override
    public void draw(TextGraphics graphics) {
        graphics.setCharacter(position.getX(), position.getY(), TextCharacter.fromCharacter('-')[0]);
    }

}
