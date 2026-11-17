import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;

public class SpaceShip extends Element {
    private int d = 0;
    private int speedCounter = 0;
    private int movementDelay = 4;
    private static final int MAX_LIVES = 3;
    private int lives = MAX_LIVES;

    public SpaceShip(int x, int y) {
        super(x, y);
    }

    public int getLives() {
        return lives;
    }

    public void getHit() {
        if (lives > 0) {
            lives--;
        }
    }

    public boolean isDead() {
        return lives <= 0;
    }

    public void setDirection(int d) {
        this.d = d;
    }

    // Avança o contador de frames; devolve true quando é altura da nave andar (de 4 em 4 frames)
    public boolean updateMovementCounter() {
        if (d == 0) {
            return false; // se a direcão for 0 não mexe
        }

        speedCounter++;
        if (speedCounter < movementDelay) {
            return false;
        }
        speedCounter = 0;
        return true;
    }

    // Posição para onde a nave quer ir; quem decide se pode é a Arena
    public Position nextPosition() {
        return new Position(position.getX() + d, position.getY());
    }

    @Override
    public void draw(TextGraphics graphics) {
        graphics.setCharacter(position.getX(), position.getY(), TextCharacter.fromCharacter('-')[0]);
    }

}
