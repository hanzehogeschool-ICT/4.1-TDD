package nl.hanze.hive.fakegradingtest;

import nl.hanze.hive.Hive;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Every test in this file is guessed based on the exceptions that are visible in the output from the teacher.
 *
 * Please contribute if you have any insight in how a test may be implemented
 *
 * Missing Test Cases:
 *      testPassOnlyClimb
 *      testClimbThroughStackedGate
 *      testClimbThroughGate
 */
public class FakeGradingTest {

    static Class<? extends Hive> hiveClass;
    static Hive hive;

    @BeforeEach
    private void setup() throws Exception {
        hive = hiveClass.getConstructor().newInstance();
    }

    // Known used requirements
    //  - Req. 4d: Plays are valid if they are adjacent to own tiles but not opposing tiles
    @Test
    public void testSlideThroughSpace() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, -1));
        /**         Q2
         *           Q1
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(0, 0, 2, 0));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(0, 0, -1, -1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(0, 0, 0, -1));
        assertDoesNotThrow(() -> hive.move(0, 0, 1, -1));  // p1
        /**         Q2 Q1           */
    }

    @Test
    public void testSpiderSlide() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, 2, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 0, -1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 2, -1));
        /**            B1    B2
         *           S1 Q1 Q2 S2
         */
        assertDoesNotThrow(() -> hive.move(-1, 0, 1, -2)); // p1
        assertDoesNotThrow(() -> hive.move(2, 0, 2, -2));
        assertDoesNotThrow(() -> hive.move(0, 0, -1, 0)); // p1
        /**             S1 S2
         *             B1    B2
         *           Q1    Q2
         */

        // Not a valid slide because the board disconnects
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, 2, -3));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, 0, -2));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, 2, -2));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, 0, 0));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, -1, 1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, 0, 1));
    }

    @Test
    public void testSpiderStack() {
        // TODO: I do not know what is being test here. This function in particular is one big guess
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, 2, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -2, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 3, 0));
        /**
         *           B1 S1 Q1 Q2 S2 B2
         */
        assertDoesNotThrow(() -> hive.move(-2, 0, -1, 0));
        assertDoesNotThrow(() -> hive.move(3, 0, 2, 0));
        /**
         *           (S1)B1 Q1 Q2 (S2)B2
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, 0, 1, 1)); // Test if the game thinks we are moving the spider instead of the beetle

        assertDoesNotThrow(() -> hive.move(-1, 0, 0, -1));
        assertDoesNotThrow(() -> hive.move(2, 0, 2, -1));
        /**            B1    B2
         *           S1 Q1 Q2 S2
         */

        assertDoesNotThrow(() -> hive.move(-1, 0, 1, 1)); // now its allowed
    }

    @Test
    public void testAntMove() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, -1));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, -1));
        /**      A2 Q2
         *           Q1 A1
         */
        assertDoesNotThrow(() -> hive.move(1, 0, 1, -1)); // p1
        assertDoesNotThrow(() -> hive.move(-1, -1, -1, 0));
        assertDoesNotThrow(() -> hive.move(1, -1, 1, -2)); // p1
        assertDoesNotThrow(() -> hive.move(-1, 0, -1, 1));
        assertDoesNotThrow(() -> hive.move(1, -2, 0, -2)); // p1
        assertDoesNotThrow(() -> hive.move(-1, 1, 0, 1));
        assertDoesNotThrow(() -> hive.move(0, -2, -1, -1)); // p1
        assertDoesNotThrow(() -> hive.move(0, 1, 1, 0));
        /**      A1 Q2
         *           Q1 A2
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, -1, -1, -1)); // You can not move to the same position
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, -1, -99, 99)); // You must be connected
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, -1, 0, 0)); // You must go to an empty field
        assertDoesNotThrow(() -> hive.move(-1, -1, 2, 0)); // p1
        /**         Q2
         *           Q1 A2 A1
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(1, 0, 3, 0));
    }

    // Known used requirements
    //  - Req. 4d: Plays are valid if they are adjacent to own tiles but not opposing tiles
    //  - Req. 3c: Black wins if white queen bee is surrounded
    @Test
    public void testPlayerWin() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0,-1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0,0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1,-2)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1,0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1,-1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 1));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 0,-2)); // p1
        assertDoesNotThrow(() -> hive.move(-1, 1, -1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, -1,-2)); // p1
        assertDoesNotThrow(() -> hive.move(1, 0, 1, -1));
        /**
                     S1 A1 A1
                      A1 Q1 A2
                       A2 Q2
         */

        assertTrue(() -> hive.isWinner(Hive.Player.BLACK));
    }

    @Test
    public void testDraw() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0,-1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0,0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1,-2)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1,0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1,-1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 1));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 0,-2)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, 0, 1));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, -1,-2)); // p1
        assertDoesNotThrow(() -> hive.move(1, 0, 1, -1));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, -1,-3)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -1,-4)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -2, 1));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -1,-5)); // p1
        assertDoesNotThrow(() -> hive.move(-2, 1, -1, 0));
        /**
            B1 B1 S1 S1 A1 A1
                      A1 Q1 A2
                       A2 Q2 B2
                        B2 S2
         */

        assertTrue(() -> hive.isDraw());
        assertFalse(() -> hive.isWinner(Hive.Player.WHITE));
        assertFalse(() -> hive.isWinner(Hive.Player.BLACK));
    }

    // Known used requirements
    //  - Req 12a: A player can pass if they have no valid plays or moves
    @Test
    public void testPass() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0,-1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0,0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, -1,-1)); // p1
        assertDoesNotThrow(() -> hive.move(0,0, 1,-1));
        assertDoesNotThrow(() -> hive.move(-1, -1, 2,-1)); // p1

        assertDoesNotThrow(() -> hive.pass());
    }

    @Test
    public void testMoveWithoutBee() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
        /**
         *            A1 A1 A2 A2
         */

        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, 0, 0, -1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, 0, -1, +1));

        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, 0, -1, -1)); // not a valid end position
    }

    @Test
    public void testGrasshopperJumpSpace() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, -1));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, -2, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, 3, -1));
        /**                     A2 G2
         *           G1 A1 Q1 Q2
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-2, 0, 5, 0)); // can not jump too far
        assertDoesNotThrow(() -> hive.move(-2, 0, 2, 0)); // p1

        assertThrows(Hive.IllegalMove.class, () -> hive.move(3, -1, 0, -1)); // can not jump too far
        assertThrows(Hive.IllegalMove.class, () -> hive.move(3, -1, 2, -1));
        assertDoesNotThrow(() -> hive.move(3, -1, 1, -1)); // p1
    }

    @Test
    public void testBeetleSlide() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 2, 0));
        /**
         *           B1 Q1 Q2 B2
         */
        assertDoesNotThrow(() -> hive.move(-1, 0, 0, 0)); // p1
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, 0, 1, -1));
        assertDoesNotThrow(() -> hive.move(2, 0, 2, -1));
    }

    @Test
    public void testBeetleStack() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 2, 0));
        /**
         *           B1 Q1 Q2 B2
         */
        assertDoesNotThrow(() -> hive.move(-1, 0, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.move(2, 0, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 2, 0));

        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.BEETLE, -1, 0));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(0, -1, 0, 0));
        assertDoesNotThrow(() -> hive.move(-1, 0, 0, 0)); // p1
    }

    @Test
    public void testMoveOpposingTiles() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 2, 0));
        /**
         *           B1 Q1 Q2 B2
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, 0, 2, -1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, 0, 1, 0));
    }

    @Test
    public void testTurnFourBee() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, -2, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, 3, 0));
        /**
         *          S1 A1 A1 A2 A2 S2
         */

        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.BEETLE, -3, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, -3, 0)); // p1

        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.BEETLE, 4, 0));
    }

    public void testPassOnlyClimb() {
        // TODO: I have no idea what is tested here specifically
    }
    @Test
    public void testSlideThroughGate() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, 2, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 0, -1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 2, -1));
        assertDoesNotThrow(() -> hive.move(-1, 0, 1, -2)); // p1
        assertDoesNotThrow(() -> hive.move(2, 0, 2, -2));
        assertDoesNotThrow(() -> hive.move(0, 0, -1, 0)); // p1
        /**             S1 S2
         *             B1    B2
         *           Q1    Q2
         */
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 3, -2));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, -1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 3, -3));
        assertDoesNotThrow(() -> hive.move(-1, -1, 2, -3));
        /**              A1 A2
         *             S1 S2 A2
         *            B1    B2
         *           Q1    Q2
         */

        // not a valid slide because the the tile is stuck and can not naturally move from there
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, 2, -3));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, 0, -2));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, 2, -2));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, 0, 0));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, -1, 1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, -2, 0, 1));
    }

    @Test
    public void testAntSlide() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 0, -1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 2, -1));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1, -2)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 3, -2));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        /**                A1    A2
         *                B1    B2
         *              A1 Q1 Q2
         */
        assertDoesNotThrow(() -> hive.move(3, -2, 2, -2)); // p2

        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.SOLDIER_ANT, 1, -1)); // playing
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, 0, 1, -1)); // moving into requires jump so its illegal

        assertDoesNotThrow(() -> hive.move(-1, 0, 3, -2));
    }

    @Test
    public void testAntStack() {
        // TODO: This is most likely not the test that is in the real GradingTest
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -2, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 3, 0));
        /**
         *           B1 A1 Q1 Q2 A2 B2
         */
        assertDoesNotThrow(() -> hive.move(-2, 0, -1, 0));
        assertDoesNotThrow(() -> hive.move(3, 0, 2, 0));
        /**
         *           (A1)B1 Q1 Q2 (A2)B2
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, 0, 3, 0)); // test If the game thinks we are moving the the ant
    }

    @Test
    public void testSpiderRevisitHexagon() {
        // TODO: is very similar to testSpiderRevisitHexagon in these tests
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, 2, 0));
        /**
         *            S1 Q1 Q2 S2
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, 0, -1, 1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, 0, 0, -1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, 0, 0, 0));
    }
    public void testClimbThroughStackedGate() {}

    @Test
    public void testPlayerWinCovered() {
        // TODO: Im not really sure what the difference is between this function and  testPlayerWin()
        // TODO:   So I made the assumption a full game is played in one the 2 and the other is just a simple check

        // Game based on: https://www.youtube.com/watch?v=adSZPyNUzyk
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, -1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, 1, -1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, -2, 0));
        /**                S1
         *          S2 S2 G1
         */
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0)); // 1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -3, 1));
        /**                S1
         *          S2 S2 G1 Q1
         *         A2
         */
        assertDoesNotThrow(() -> hive.move(1, -1, -2, -1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, -2, 1));
        /**        S1
         *          S2 S2 G1 Q1
         *         A2 Q2
         */
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SPIDER, 0, 1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, -2, 2));
        /**        S1
         *          S2 S2 G1 Q1
         *         A2 Q2    S1
         *              G2
         */
        assertDoesNotThrow(() -> hive.move(0, 1, -2, 3)); // p1 spider move to block Grasshopper
        assertDoesNotThrow(() -> hive.move(-3, 1, -2, -2)); // p2 ant to above spider
        /**     A2
         *        S1
         *          S2 S2 G1 Q1
         *            Q2
         *              G2
         *                S1
         */
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -3, 4)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -3, 1));
        /**     A2
         *        S1
         *          S2 S2 G1 Q1
         *         B2 Q2
         *              G2
         *                S1
         *              B1
         */
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1, -1)); // p1
        assertDoesNotThrow(() -> hive.move(-3, 1, -2, 0)); // moves beetle on top of spider
        /**     A2
         *        S1        A1
         *         (B2) S2 G1 Q1
         *           Q2
         *              G2
         *                S1
         *              B1
         */
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 0, 1)); // p1
        assertDoesNotThrow(() -> hive.move(-2, 0, -2, -1)); // moves beetle from on top of spider to on top of spider opponent
        /**     A2
         *        (B2)      A1
         *          S2 S2 G1 Q1
         *           Q2    B1
         *              G2
         *                S1
         *              B1
         */
        assertDoesNotThrow(() -> hive.move(-3, 4, -2, 3)); // p1 moves beetle on top of spider opponent
        assertDoesNotThrow(() -> hive.move(-2, -2, 2, -1)); // p2 moves ant away
        /**
         *        (B2)      A1 A2
         *          S2 S2 G1 Q1
         *           Q2    B1
         *              G2
         *                (B1)
         */
        assertDoesNotThrow(() -> hive.move(1, 0, 2, 0)); // p1 moves queen
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, -1));
        /**
         *        (B2)A2    A1 A2
         *          S2 S2 G1    Q1
         *           Q2    B1
         *              G2
         *                (B1)
         */
        assertDoesNotThrow(() -> hive.move(-2, 3, -2, 2)); // p1 moves beetle up free his own spider
        assertDoesNotThrow(() -> hive.move(-1, -1, 2, 1)); // p2 moves ant to other side of the board next to queen
        /**
         *        (B2)      A1 A2
         *          S2 S2 G1    Q1
         *           Q2    B1    A2
         *             (B1)
         *                S1
         */

        assertDoesNotThrow(() -> hive.move(-2, 2, -2, 1)); // p1 moves beetle up
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, -3, 0));
        /**
         *        (B2)      A1 A2
         *       G2 S2 S2 G1    Q1
         *           (B1)  B1    A2
         *              G2
         *                S1
         */

        assertDoesNotThrow(() -> hive.move(-2, 3, 1, 1)); // p1 moves spider SPECIAL
        assertThrows(Hive.IllegalMove.class, () -> hive.move(2, 1, 2, 0)); // ants can only move along the edge so they can not jump to here
        assertDoesNotThrow(() -> hive.move(-3, 0, 1, 0)); // p2 Grasshopper jump
        /**
         *        (B2)      A1 A2
         *          S2 S2 G1 G2 Q1
         *           (B1)  B1 S1 A2
         *             G2
         */

        assertDoesNotThrow(() -> hive.move(2, 0, 3, 0)); // p1 moves queen right
        assertDoesNotThrow(() -> hive.move(2, -1, 3, -1)); // p2 Grasshopper jump
        /**
         *        (B2)      A1    eA2
         *          S2 S2 G1 G2    Q1
         *          (B1)   B1 S1 A2
         *            G2
         */

        assertDoesNotThrow(() -> hive.move(0, 1, 1, 0)); // p1 moves beetle
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, -3, 0));
        /**
         *        (B2)      A1     A2
         *       G2 S2 S2 G1(B1)    Q1
         *          (B1)       S1 A2
         *             G2
         */

        assertDoesNotThrow(() -> hive.move(1, -1, -4, 1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 4, -2));
        /**                         B2
         *        (B2)            A2
         *       G2 S2 S2 G1(B1)   Q1
         *     A1   (B1)      S1 A2
         *             G2
         */

        assertDoesNotThrow(() -> hive.move(1, 0, 2, 0)); // p1 moves beetle off other player
        assertDoesNotThrow(() -> hive.move(2,1, 4, 0)); // moves ant
        /**                         B2
         *        (B2)            A2
         *       G2 S2 S2 G1 G2 B1 Q1 A2
         *     A1   (B1)      S1
         *             G2
         */

        assertDoesNotThrow(() -> hive.move(-4, 1, 5, -3)); // p1 moves ant
        assertDoesNotThrow(() -> hive.move(4,0, 3, 1)); // moves ant
        /**                           A1
         *                          B2
         *        (B2)            A2
         *       G2 S2 S2 G1 G2 B1 Q1
         *           (B1)      S1    A2
         *             G2
         */

        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 6, -4)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 4, 1)); // moves ant
        /**                             A1
         *                            A1
         *                          B2
         *        (B2)            A2
         *       G2 S2 S2 G1 G2 B1 Q1
         *           (B1)      S1    A2 A2
         *             G2
         */

        assertDoesNotThrow(() -> hive.move(6, -4, -3, 1)); // p1 move newly placed ant
        assertDoesNotThrow(() -> hive.move(-3, 0, 4, 0)); // Grasshoper jump
        /**                           A1
         *                          B2
         *        (B2)            A2
         *          S2 S2 G1 G2 B1 Q1 G2
         *        A1 (B1)      S1    A2 A2
         *             G2
         */

        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -4, 2)); // p1
        assertDoesNotThrow(() -> hive.move(-2, 2, -2, -2)); // Grasshoper jump
        /**                           A1
         *       G2                 B2
         *        (B2)            A2
         *          S2 S2 G1 G2 B1 Q1 G2
         *        A1 (B1)      S1    A2 A2
         *      A1
         */

        assertDoesNotThrow(() -> hive.move(-4, 2, -2, 2)); // p1 move newly placed ant
        assertDoesNotThrow(() -> hive.move(-2, -1, -2, 0)); // Grasshoper jump
        /**                           A1
         *       G2                 B2
         *         S1             A2
         *         (B2)S2 G1 G2 B1 Q1 G2
         *        A1 (B1)      S1    A2 A2
         *             A1
         */

        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, -3, 2)); // p1 SPECIAL place while neighbour stack contains opponent with your beetle on top
        assertDoesNotThrow(() -> hive.move(-2, 0, -2, 1)); // B2 op B1 on Q2
        /**                           A1
         *       G2                 B2
         *         S1             A2
         *          S2 S2 G1 G2 B1 Q1 G2
         *        A1((B2))     S1    A2 A2
         *          G1 A1
         */
        assertDoesNotThrow(() -> hive.move(5, -3, -1, 1)); // p1 moves ant and wins

        assertTrue(hive.isWinner(Hive.Player.WHITE));
        assertFalse(hive.isWinner(Hive.Player.BLACK));
        assertFalse(hive.isDraw());
    }

    public void testClimbThroughGate() {
        // TODO: same as testSlideThroughGate, no idee what is being tested here.
    }

    @Test
    public void testMoveTilesAdjacent() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, -1));
        /**         Q2
         *           Q1
         */

        // connected to the opponent stone
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.SOLDIER_ANT, 1, -1));
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.SOLDIER_ANT, 1, -2));
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.SOLDIER_ANT, 0, -2));
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.SOLDIER_ANT, -1, -1));
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.SOLDIER_ANT, 0, -1));

        // valid
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 1, -2));

        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 0, 1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 0, -2));

        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, -1));

        /**       A2 A2
         *       A2 Q2
         *           Q1 A1
         *          A1 A1
         */

        // not connected to anything
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.SOLDIER_ANT, 1, 1));
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.SOLDIER_ANT, -2, 1));
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.SOLDIER_ANT, -2, 0));
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
    }

    @Test
    public void testInitialTiles() {
        assertFalse(hive.isWinner(Hive.Player.WHITE));
        assertFalse(hive.isWinner(Hive.Player.BLACK));
        assertFalse(hive.isDraw());
        assertThrows(Hive.IllegalMove.class, () -> hive.pass());
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 9999, 9999)); // p1
    }

    @Test
    public void testHandSize() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.QUEEN_BEE, -1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.QUEEN_BEE, -2, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
        /**
         *            A1 Q1 Q2 A2
         */
    }

    @Test
    public void testGrasshopperJump() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, -2, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, 3, 0)); // p1
        /**
         *           G1 A1 Q1 Q2 A2 G2
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-2, 0, 5, 0)); // can not jump too far
        assertDoesNotThrow(() -> hive.move(-2, 0, 4, 0)); // p1

        assertThrows(Hive.IllegalMove.class, () -> hive.move(3, 0, -2, 0)); // p2 jumps break up the board
        assertThrows(Hive.IllegalMove.class, () -> hive.move(3, 0, 5, 0));
    }

    @Test
    public void testGrasshopperMove() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, -2, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.GRASSHOPPER, 3, 0)); // p1
        /**
         *           G1 A1 Q1 Q2 A2 G2
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-2, 0, -1, 0));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-2, 0, -1, -1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-2, 0, -2, -1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-2, 0, -3, 0));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-2, 0, -3, 1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-2, 0, -2, 1));
    }

    @Test
    public void testSplitHive() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
        /**
         *            A1 Q1 Q2 A2
         */

        assertThrows(Hive.IllegalMove.class, () -> hive.play(Hive.Tile.QUEEN_BEE, 5, 5));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, 0, -2, 0));
    }

    @Test
    public void testPlayStacked() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -2, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 3, 0));
        /**
         *           B1 A1 Q1 Q2 A2 B2
         */
        assertDoesNotThrow(() -> hive.move(-2, 0, -1, 0));
        assertDoesNotThrow(() -> hive.move(3, 0, 2, 0));
        /**
         *           (A1)B1 Q1 Q2 (A2)B2
         */
        assertDoesNotThrow(() -> hive.move(-1, 0, 0, -1));
        assertDoesNotThrow(() -> hive.move(2, 0, 2, -1));
        /**            B1    B2
         *           A1 Q1 Q2 A2
         */
        assertDoesNotThrow(() -> hive.move(-1, 0, -1, -1));
        assertDoesNotThrow(() -> hive.move(2, 0, 1, 1));
        /**            B1     B2
         *               Q1 Q2
         *             A1     A2
         */
    }

    @Test
    public void testBeeSlide() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
        /**
         *            A1 Q1 Q2 A2
         */
        assertDoesNotThrow(() -> hive.move(-1, 0, 0, -1)); // p1
        assertDoesNotThrow(() -> hive.move(2, 0, 2, -1));
        /**             A1    A2
         *               Q1 Q2
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(0, 0, 1, -1));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 0, -2)); // p1
        assertThrows(Hive.IllegalMove.class, () -> hive.move(1, 0, 2, 0));
        /**           A1
         *              A1 Q1 A2
         *                x   x  Q2
         */
    }

    @Test
    public void testBeeStack() {
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 0, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.QUEEN_BEE, 1, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 2, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, -2, 0)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.BEETLE, 3, 0));
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, -1, -1)); // p1
        assertDoesNotThrow(() -> hive.play(Hive.Tile.SOLDIER_ANT, 3, -1));
        /**            A1          A2
         *           B1 A1 Q1 Q2 A2 B2
         */
        assertThrows(Hive.IllegalMove.class, () -> hive.move(-1, -1, -2, 0));
        assertDoesNotThrow(() -> hive.move(-2, 0, -1, -1));
        assertThrows(Hive.IllegalMove.class, () -> hive.move(3, -1, 3, 0));
        assertDoesNotThrow(() -> hive.move(3, 0, 3, -1));
    }



    // This is based on the sanity check that is provided on blackboard
    @BeforeAll
    static void findYourClassImplementationOfHive() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources("");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            if (!url.getProtocol().equals("file")) {
                continue;
            }
            Deque<File> queue = new ArrayDeque<>();
            queue.addFirst(new File(url.toURI()));
            String path = queue.peekFirst().getPath();
            while (!queue.isEmpty()) {
                File file = queue.remove();
                String name = file.getName();
                if (file.isDirectory() && !name.contains(".")) {
                    for (File child : file.listFiles()) {
                        queue.addFirst(child);
                    }
                } else if (file.isFile() && name.endsWith(".class")) {
                    Class cls = Class.forName(file.getPath().substring(path.length() + 1, file.getPath().length() - 6).replace(File.separator, "."));
                    if (Hive.class.isAssignableFrom(cls) && !cls.isInterface()) {
                        hiveClass = cls;
                        System.out.println("FakeGradingTest for: " + hiveClass);
                        hive = hiveClass.getConstructor().newInstance();
                    }
                }
            }
        }
        if (hiveClass == null || hive == null) {
            fail("No implementations of " + Hive.class.getCanonicalName() + " found");
        }
    }
}
