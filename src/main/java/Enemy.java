import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends Element {
    private int speedCounter = 0;
    private int movementDelay = 8 * 4;
    private final Random random = new Random();
    private static final int MAX_HEALTH = 3;
    private int health = MAX_HEALTH;

    public Enemy(int x, int y) {
        super(x, y);
    }

    public int getHealth() {
        return health;
    }

    public void getHit() {
        if (health > 0) {
            health--;
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    // Avança o contador de frames; devolve true quando é altura do inimigo andar (de 32 em 32 frames)
    public boolean updateMovementCounter() {
        speedCounter++;
        if (speedCounter < movementDelay) {
            return false;
        }
        speedCounter = 0;
        return true;
    }

    // Posições para onde o inimigo pode ir; quem decide se pode é a Arena
    public List<Position> possiblePositions() {
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(position.getX() + 1, position.getY())); // direita
        positions.add(new Position(position.getX() - 1, position.getY())); // esquerda
        positions.add(new Position(position.getX(), position.getY() + 1)); // baixo
        return positions;
    }

    // Escolhe uma das posições válidas (randomly)
    public Position chooseNextPosition(List<Position> validPositions) {
        return validPositions.get(random.nextInt(validPositions.size()));
    }

    @Override
    public void draw(TextGraphics graphics) {
        graphics.setCharacter(position.getX(), position.getY(), TextCharacter.fromCharacter('\'')[0]);
    }
}
