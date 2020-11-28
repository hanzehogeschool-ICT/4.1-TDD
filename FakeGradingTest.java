package nl.hanze.hive.fakegradingtest;

import nl.hanze.hive.Hive;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class FakeGradingTest {

    static Class<? extends Hive> hiveClass;
    static Hive hive;



    public void testSlideThroughSpace() {}
    public void testSpiderSlide() {}
    public void testSpiderStack() {}
    public void testAntMove() {}
    public void testPlayerWin() {}
    public void testDraw() {}
    public void testPass() {}
    public void testMoveWithoutBee() {}
    public void testGrasshopperJumpSpace() {}
    public void testBeetleSlide() {}
    public void testBeetleStack() {}
    public void testMoveOpposingTiles() {}
    public void testTurnFourBee() {}
    public void testPassOnlyClimb() {}
    public void testSlideThroughGate() {}
    public void testAntSlide() {}
    public void testAntStack() {}
    public void testSpiderRevisitHexagon() {}
    public void testClimbThroughStackedGate() {}
    public void testPlayerWinCovered() {}
    public void testClimbThroughGate() {}
    public void testMoveTilesAdjacent() {}
    public void testInitialTiles() {}
    public void testHandSize() {}
    public void testGrasshopperJump() {}
    public void testGrasshopperMove() {}
    public void testSplitHive() {}
    public void testPlayStacked() {}
    public void testBeeSlide() {}
    public void testBeeStack() {}



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
