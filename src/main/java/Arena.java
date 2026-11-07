import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;


public class Arena {
    private final int width;
    private final int height;
    private SpaceShip spaceShip;

    public Arena(int width, int height) {
        this.width = width;
        this.height = height;
        this.spaceShip = new SpaceShip(10, 30);
    }

    //Spaceship
    public void moveSpaceShip() {
        spaceShip.move(width);
    }

    public void setSpaceShipDirection(int d) {
        spaceShip.setDirection(d);
    }

    //draw
    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#000000"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        spaceShip.draw(graphics);
    }
}
