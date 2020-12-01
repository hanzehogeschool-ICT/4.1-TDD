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
    public void testSlideThroughSpace() {}

    public void testSpiderSlide() {}
    public void testSpiderStack() {}

    public void testAntMove() {
        // TODO: I do not know what is happening in this function please see testAntSlide
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
    }

    // Known used requirements
    //  - Req 12a: A player can pass if they have no valid plays or moves
    public void testPass() {}

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

    public void testGrasshopperJumpSpace() {}

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

    public void testPassOnlyClimb() {}
    public void testSlideThroughGate() {}

    @Test
    public void testAntSlide() {
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

    public void testSpiderRevisitHexagon() {}
    public void testClimbThroughStackedGate() {}
    public void testPlayerWinCovered() {}
    public void testClimbThroughGate() {}

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
        assertDoesNotThrow(() -> hive.move(-1, 0, 0, -1));
        assertDoesNotThrow(() -> hive.move(2, 0, 2, -1));
        /**             A1    A2
         *               Q1 Q2
         */
        assertDoesNotThrow(() -> hive.move(0, 0, 1, -1));
        assertDoesNotThrow(() -> hive.move(1, 0, 2, 0));
        /**             A1 Q1 A2
         *                      Q2
         */
        assertDoesNotThrow(() -> hive.move(0, -1, 0, 0));
        assertDoesNotThrow(() -> hive.move(2, -1, 1, 0));
        /**                Q1
         *               A1 A2 Q2
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
