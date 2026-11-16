import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        Position position = Mockito.mock(Position.class);
        ship.setPosition(position);
        assertSame(position, ship.getPosition());
    }

    @Test
    void nextPositionIsBasedOnCurrentPosition() {
        Position position = Mockito.mock(Position.class);
        Mockito.when(position.getX()).thenReturn(3);
        Mockito.when(position.getY()).thenReturn(4);
        ship.setPosition(position);
        ship.setDirection(1);

        assertEquals(new Position(4, 4), ship.nextPosition());
    }

    @Test
    void drawsSpaceShipAtPosition() {
        TextGraphics graphics = Mockito.mock(TextGraphics.class);

        ship.draw(graphics);

        Mockito.verify(graphics).setCharacter(10, 15, TextCharacter.fromCharacter('-')[0]);
        Mockito.verifyNoMoreInteractions(graphics);
    }

    @Test
    void drawFollowsShipPosition() {
        TextGraphics graphics = Mockito.mock(TextGraphics.class);
        ship.setPosition(new Position(3, 4));

        ship.draw(graphics);

        Mockito.verify(graphics).setCharacter(3, 4, TextCharacter.fromCharacter('-')[0]);
    }
}
