import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArenaTest {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 40;
    private static final int movementDelay = 4;

    private Arena arena;

    @BeforeEach
    void setUp() {
        arena = new Arena(WIDTH, HEIGHT);
    }

    private void moveFrames(int frames) {
        for (int i = 0; i < frames; i++) arena.moveSpaceShip();
    }

    private int shipX() {
        return arena.getSpaceShip().getPosition().getX();
    }

    @Test
    void shipDoesNotMoveWithoutDirection() {
        moveFrames(movementDelay * 3);
        assertEquals(10, shipX());
    }

    @Test
    void shipMovesOnlyEveryFourFrames() {
        arena.setSpaceShipDirection(1);

        moveFrames(movementDelay - 1);
        assertEquals(10, shipX());

        moveFrames(1);
        assertEquals(11, shipX());
    }

    @Test
    void shipMovesLeft() {
        arena.setSpaceShipDirection(-1);
        moveFrames(movementDelay * 2);
        assertEquals(8, shipX());
    }

    @Test
    void shipVerticalPositionNeverChanges() {
        int y = arena.getSpaceShip().getPosition().getY();
        arena.setSpaceShipDirection(1);
        moveFrames(movementDelay * 5);
        assertEquals(y, arena.getSpaceShip().getPosition().getY());
    }

    @Test
    void shipStopsAtLeftEdge() {
        arena.getSpaceShip().setPosition(new Position(0, 30));
        arena.setSpaceShipDirection(-1);
        moveFrames(movementDelay * 3);
        assertEquals(0, shipX());
    }

    @Test
    void shipStopsAtRightEdge() {
        arena.getSpaceShip().setPosition(new Position(WIDTH - 1, 30));
        arena.setSpaceShipDirection(1);
        moveFrames(movementDelay * 3);
        assertEquals(WIDTH - 1, shipX());
    }

    @Test
    void hittingEdgeResetsDirection() {
        arena.getSpaceShip().setPosition(new Position(0, 30));
        arena.setSpaceShipDirection(-1);
        moveFrames(movementDelay); // bate na parede, direção passa a 0

        // A nave fica parada até receber uma nova direção
        moveFrames(movementDelay * 2);
        assertEquals(0, shipX());

        arena.setSpaceShipDirection(1);
        moveFrames(movementDelay);
        assertEquals(1, shipX());
    }

    @Test
    void shipChangesDirectionMidWay() {
        arena.setSpaceShipDirection(1);
        moveFrames(movementDelay);
        assertEquals(11, shipX());

        arena.setSpaceShipDirection(-1);
        moveFrames(movementDelay * 2);
        assertEquals(9, shipX());
    }

    @Test
    void stoppingKeepsPosition() {
        arena.setSpaceShipDirection(1);
        moveFrames(movementDelay);
        arena.setSpaceShipDirection(0);
        moveFrames(movementDelay * 3);
        assertEquals(11, shipX());
    }
}
