import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class BulletTest {
    private static final TextCharacter bulletCharacter = TextCharacter.fromCharacter('|')[0];

    private Bullet bullet;

    @BeforeEach
    void setUp() {
        bullet = new Bullet(10, 15);
    }

    @Test
    void constructorSetsPosition() {
        assertEquals(new Position(10, 15), bullet.getPosition());
    }

    @Test
    void moveGoesUpOneRow() {
        bullet.move();
        assertEquals(new Position(10, 14), bullet.getPosition());
    }

    @Test
    void moveSeveralTimes() {
        for (int i = 0; i < 5; i++) bullet.move();
        assertEquals(new Position(10, 10), bullet.getPosition());
    }

    @Test
    void moveNeverChangesColumn() {
        for (int i = 0; i < 5; i++) bullet.move();
        assertEquals(10, bullet.getPosition().getX());
    }

    @Test
    void moveUpdatesPositionY() {
        Position position = Mockito.mock(Position.class);
        Mockito.when(position.getY()).thenReturn(7);
        bullet.setPosition(position);

        bullet.move();

        Mockito.verify(position).setY(6);
        Mockito.verify(position, Mockito.never()).setX(Mockito.anyInt());
    }

    @Test
    void notOffScreenInsideArena() {
        assertFalse(bullet.isOffScreen());
    }

    @Test
    void notOffScreenOnTopRow() {
        bullet.setPosition(new Position(10, 0));
        assertFalse(bullet.isOffScreen());
    }

    @Test
    void offScreenAboveTopRow() {
        bullet.setPosition(new Position(10, -1));
        assertTrue(bullet.isOffScreen());
    }

    @Test
    void becomesOffScreenAfterMovingPastTopRow() {
        bullet.setPosition(new Position(10, 1));

        bullet.move();
        assertFalse(bullet.isOffScreen());

        bullet.move();
        assertTrue(bullet.isOffScreen());
    }

    @Test
    void drawsPipeAtPosition() {
        TextGraphics graphics = Mockito.mock(TextGraphics.class);

        bullet.draw(graphics);

        Mockito.verify(graphics).setCharacter(10, 15, bulletCharacter);
        Mockito.verifyNoMoreInteractions(graphics);
    }

    @Test
    void drawFollowsBulletMovement() {
        TextGraphics graphics = Mockito.mock(TextGraphics.class);
        bullet.move();

        bullet.draw(graphics);

        Mockito.verify(graphics).setCharacter(10, 14, bulletCharacter);
    }
}
