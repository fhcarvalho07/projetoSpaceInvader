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


    public Arena(int width, int height) {
        this.width = width;
        this.height = height;
        this.spaceShip = new SpaceShip(10, 30);
        this.bullets = new ArrayList<>();
    }

    //bullets
    public void fireBullet() {
        int bulletX = spaceShip.getPosition().getX();
        int bullety = spaceShip.getPosition().getY() - 1;
        bullets.add(new Bullet(bulletX, bullety));
    }

    //Spaceship
    public void moveSpaceShip() {
        spaceShip.move(width);
    }

    public void moveBullets() {
        for (Bullet bullet : bullets) {
            bullet.move();
        }
        bullets.removeIf(Bullet::isOffScreen);
    }

    public void setSpaceShipDirection(int d) {
        spaceShip.setDirection(d);
    }

    //draw
    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#000000"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        spaceShip.draw(graphics);

        for (Bullet bullet : bullets) {
            bullet.draw(graphics);
        }
    }
}
