import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceShipTest {
    private static final int movementDelay = 4;

    private SpaceShip ship;

    @BeforeEach
    void setUp() {
        ship = new SpaceShip(10, 15);
    }

    @Test
    void constructorSetsPosition() {
        assertEquals(new Position(10, 15), ship.getPosition());
    }

    @Test
    void notReadyToMoveWithoutDirection() {
        for (int i = 0; i < movementDelay * 3; i++) {
            assertFalse(ship.updateMovementCounter());
        }
    }

    @Test
    void readyToMoveOnlyEveryFourFrames() {
        ship.setDirection(1);

        for (int round = 0; round < 3; round++) {
            for (int i = 0; i < movementDelay - 1; i++) {
                assertFalse(ship.updateMovementCounter());
            }
            assertTrue(ship.updateMovementCounter());
        }
    }

    @Test
    void nextPositionRight() {
        ship.setDirection(1);
        assertEquals(new Position(11, 15), ship.nextPosition());
    }

    @Test
    void nextPositionLeft() {
        ship.setDirection(-1);
        assertEquals(new Position(9, 15), ship.nextPosition());
    }

    @Test
    void nextPositionWithoutDirectionIsCurrentPosition() {
        assertEquals(new Position(10, 15), ship.nextPosition());
    }

    @Test
    void nextPositionDoesNotMoveShip() {
        ship.setDirection(1);
        ship.nextPosition();
        assertEquals(new Position(10, 15), ship.getPosition());
    }

    @Test
    void setPositionUpdatesPosition() {
        ship.setPosition(new Position(3, 4));
        assertEquals(new Position(3, 4), ship.getPosition());
    }

    @Test
    void drawsDashAtPosition() {
        BasicTextImage image = new BasicTextImage(new TerminalSize(20, 20));
        TextGraphics graphics = image.newTextGraphics();

        ship.draw(graphics);

        assertEquals('-', image.getCharacterAt(10, 15).getCharacterString().charAt(0));
        assertEquals(' ', image.getCharacterAt(11, 15).getCharacterString().charAt(0));
    }
}
