import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class ArenaTest {
    private static final int width = 20;
    private static final int height = 40;
    private static final int movementDelay = 4;
    private static final int cooldownFrames = 10;
    private static final TextCharacter shipCharacter = TextCharacter.fromCharacter('-')[0];
    private static final TextCharacter bulletCharacter = TextCharacter.fromCharacter('|')[0];

    private Arena arena;
    private TextGraphics graphics;

    @BeforeEach
    void setUp() {
        arena = new Arena(width, height);
        graphics = Mockito.mock(TextGraphics.class);
    }

    private void moveFrames(int frames) {
        for (int i = 0; i < frames; i++) arena.moveSpaceShip();
    }

    private void moveBulletsFrames(int frames) {
        for (int i = 0; i < frames; i++) arena.moveBullets();
    }

    private int shipX() {
        return arena.getSpaceShip().getPosition().getX();
    }

    private void verifyBulletsDrawn(int count) {
        Mockito.verify(graphics, Mockito.times(count))
                .setCharacter(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(bulletCharacter));
    }

    // SpaceShip

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
        arena.getSpaceShip().setPosition(new Position(width - 1, 30));
        arena.setSpaceShipDirection(1);
        moveFrames(movementDelay * 3);
        assertEquals(width - 1, shipX());
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

    // Bullets

    @Test
    void noBulletsBeforeFiring() {
        arena.draw(graphics);

        verifyBulletsDrawn(0);
    }

    @Test
    void bulletIsFiredAboveShip() {
        arena.fireBullet();
        arena.draw(graphics);

        Mockito.verify(graphics).setCharacter(10, 29, bulletCharacter);
        verifyBulletsDrawn(1);
    }

    @Test
    void cannotFireDuringCooldown() {
        arena.fireBullet();
        arena.fireBullet();
        moveBulletsFrames(cooldownFrames - 1);
        arena.fireBullet();
        arena.draw(graphics);

        verifyBulletsDrawn(1);
    }

    @Test
    void canFireAgainAfterCooldown() {
        arena.fireBullet();
        moveBulletsFrames(cooldownFrames);
        arena.fireBullet();
        arena.draw(graphics);

        Mockito.verify(graphics).setCharacter(10, 29 - cooldownFrames, bulletCharacter);
        Mockito.verify(graphics).setCharacter(10, 29, bulletCharacter);
        verifyBulletsDrawn(2);
    }

    @Test
    void bulletIsFiredFromCurrentShipPosition() {
        arena.getSpaceShip().setPosition(new Position(4, 30));
        arena.fireBullet();
        arena.draw(graphics);

        Mockito.verify(graphics).setCharacter(4, 29, bulletCharacter);
    }

    @Test
    void bulletsMoveUpEachFrame() {
        arena.fireBullet();
        moveBulletsFrames(3);
        arena.draw(graphics);

        Mockito.verify(graphics).setCharacter(10, 26, bulletCharacter);
        verifyBulletsDrawn(1);
    }

    @Test
    void bulletStaysWhileOnTopRow() {
        arena.getSpaceShip().setPosition(new Position(10, 2));
        arena.fireBullet(); // y = 1
        moveBulletsFrames(1); // y = 0
        arena.draw(graphics);

        Mockito.verify(graphics).setCharacter(10, 0, bulletCharacter);
    }

    @Test
    void bulletIsRemovedWhenOffScreen() {
        arena.getSpaceShip().setPosition(new Position(10, 2));
        arena.fireBullet(); // y = 1
        moveBulletsFrames(2); // y = -1
        arena.draw(graphics);

        verifyBulletsDrawn(0);
    }

    // Draw

    @Test
    void drawClearsBackgroundThenDrawsShip() {
        arena.draw(graphics);

        InOrder inOrder = Mockito.inOrder(graphics);
        inOrder.verify(graphics).setBackgroundColor(TextColor.Factory.fromString("#000000"));
        inOrder.verify(graphics).fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        inOrder.verify(graphics).setCharacter(10, 30, shipCharacter);
    }

    @Test
    void drawFollowsShipMovement() {
        arena.setSpaceShipDirection(1);
        moveFrames(movementDelay);
        arena.draw(graphics);

        Mockito.verify(graphics).setCharacter(11, 30, shipCharacter);
    }
}
