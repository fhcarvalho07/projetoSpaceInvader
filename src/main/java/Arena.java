import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.ArrayList;
import java.util.List;


public class Arena {
    private final int width;
    private final int height;
    private SpaceShip spaceShip;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private int fireCooldown = 0;
    private static final int COOLDOWN_FRAMES = 10;


    public Arena(int width, int height) {
        this.width = width;
        this.height = height;
        this.spaceShip = new SpaceShip(10, 30);
        this.bullets = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.enemies.add(new Enemy(8, 4));
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
    public void moveEnemies() {
        for (Enemy enemy : enemies) {
            if (!enemy.updateMovementCounter()) {
                continue;
            }

            List<Position> validPositions = enemy.possiblePositions();
            validPositions.removeIf(p -> !isInside(p));

            if (!validPositions.isEmpty()) {
                enemy.setPosition(enemy.chooseNextPosition(validPositions));
            }
        }
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
