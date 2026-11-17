import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyBulletTest {
    private static final int movementDelay = 2;
    private static final TextCharacter enemyBulletCharacter = TextCharacter.fromCharacter('*')[0];

    private EnemyBullet bullet;

    @BeforeEach
    void setUp() {
        bullet = new EnemyBullet(10, 15);
    }

    @Test
    void constructorSetsPosition() {
        assertEquals(new Position(10, 15), bullet.getPosition());
    }

    @Test
    void moveGoesDownOneRow() {
        bullet.move();
        assertEquals(new Position(10, 16), bullet.getPosition());
    }

    @Test
    void readyToMoveOnlyEveryTwoFrames() {
        for (int i = 0; i < 3; i++) {
            for (int f = 0; f < movementDelay - 1; f++) {
                assertFalse(bullet.updateMovementCounter());
            }
            assertTrue(bullet.updateMovementCounter());
        }
    }

    @Test
    void notOffScreenOnLastRow() {
        bullet.setPosition(new Position(10, 39));
        assertFalse(bullet.isOffScreen(40));
    }

    @Test
    void offScreenBelowLastRow() {
        bullet.setPosition(new Position(10, 40));
        assertTrue(bullet.isOffScreen(40));
    }

    @Test
    void drawsStarAtPosition() {
        TextGraphics graphics = Mockito.mock(TextGraphics.class);

        bullet.draw(graphics);

        Mockito.verify(graphics).setCharacter(10, 15, enemyBulletCharacter);
        Mockito.verifyNoMoreInteractions(graphics);
    }
}
