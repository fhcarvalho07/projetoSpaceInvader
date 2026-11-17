import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Arena {
    private final int width;
    private final int height;
    private SpaceShip spaceShip;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private int fireCooldown = 0;
    private static final int COOLDOWN_FRAMES = 10;

    private static final int ENEMY_COUNT = 15;
    private static final int ENEMY_MIN_Y = 1;       // Enemies nascem entre y = 1 e y = 6
    private static final int ENEMY_MAX_Y = 10;
    private final Random random = new Random();


    public Arena(int width, int height) {
        this.width = width;
        this.height = height;
        this.spaceShip = new SpaceShip(10, 30);
        this.bullets = new ArrayList<>();
        this.enemies = new ArrayList<>();
        createEnemies();
    }

    // Bullet
    public void fireBullet() {
        if (fireCooldown == 0) {
            int bulletX = spaceShip.getPosition().getX();
            int bullety = spaceShip.getPosition().getY() - 1;
            bullets.add(new Bullet(bulletX, bullety));

            fireCooldown = COOLDOWN_FRAMES;
        }
    }

    public void moveBullets() {
        if (fireCooldown > 0) {
            fireCooldown--;
        }

        for (Bullet bullet : bullets) {
            bullet.move();
        }
        bullets.removeIf(Bullet::isOffScreen);
    }

    // SpaceShip
    public void moveSpaceShip() {
        if (!spaceShip.updateMovementCounter()) {
            return;
        }

        Position next = spaceShip.nextPosition();
        if (canSpaceShipMove(next)) {
            spaceShip.setPosition(next);
        } else {
            spaceShip.setDirection(0); // bateu na parede, pára
        }
    }

    private boolean canSpaceShipMove(Position position) {
        return position.getX() >= 0 && position.getX() < width;
    }

    public void setSpaceShipDirection(int d) {
        spaceShip.setDirection(d);
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    // Enemy
    private void createEnemies() {
        while (enemies.size() < ENEMY_COUNT) {
            int x = random.nextInt(width);
            int y = ENEMY_MIN_Y + random.nextInt(ENEMY_MAX_Y - ENEMY_MIN_Y + 1);
            Position position = new Position(x, y);
            if (!isOccupied(position)) {
                enemies.add(new Enemy(x, y));
            }
        }
    }

    // Cada inimigo escolhe a sua direção (random)
    public void moveEnemies() {
        for (Enemy enemy : enemies) {
            if (!enemy.updateMovementCounter()) {
                continue;
            }

            List<Position> validPositions = enemy.possiblePositions();
            validPositions.removeIf(p -> !isInside(p) || isOccupied(p));

            if (!validPositions.isEmpty()) {
                enemy.setPosition(enemy.chooseNextPosition(validPositions));
            }
        }
    }

    // Verifica se já existe um inimigo nessa posição
    private boolean isOccupied(Position position) {
        for (Enemy enemy : enemies) {
            if (enemy.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInside(Position position) {
        return position.getX() >= 0 && position.getX() < width && position.getY() < height;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    // draw
    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#000000"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        spaceShip.draw(graphics);

        for (Bullet bullet : bullets) {
            bullet.draw(graphics);
        }

        for (Enemy enemy : enemies) {
            enemy.draw(graphics);
        }
    }
}
