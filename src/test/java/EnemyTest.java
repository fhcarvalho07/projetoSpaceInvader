import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {
    private Enemy enemy;

    @BeforeEach
    void setUp() {
        enemy = new Enemy(5, 5);
    }

    @Test
    void startsWithThreeHealth() {
        assertEquals(3, enemy.getHealth());
        assertFalse(enemy.isDead());
    }

    @Test
    void survivesTwoHits() {
        enemy.getHit();
        enemy.getHit();
        assertEquals(1, enemy.getHealth());
        assertFalse(enemy.isDead());
    }

    @Test
    void diesAfterThreeHits() {
        enemy.getHit();
        enemy.getHit();
        enemy.getHit();
        assertEquals(0, enemy.getHealth());
        assertTrue(enemy.isDead());
    }

    @Test
    void healthNeverGoesNegative() {
        for (int i = 0; i < 5; i++) {
            enemy.getHit();
        }
        assertEquals(0, enemy.getHealth());
    }
}
