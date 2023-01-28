package byow.Test;
import byow.Core.Engine;
import byow.Core.Map;
import byow.Core.Map.*;
import byow.TileEngine.*;
import org.junit.*;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;
import static org.junit.Assert.assertEquals;
import java.util.Random;



public class EngineTest {
    @Test
    public void stringInputTest() {
        Engine engine = new Engine();
        TETile[][] world1 = engine.interactWithInputString("n22s");
        TETile[][] world2 = engine.interactWithInputString("n22s");
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                assertEquals(world1[i][j].description(), world2[i][j].description());
            }
        }
    }
    @Test
    public void SeedTest() {
        Engine engine = new Engine();
        TETile[][] world1 = engine.interactWithInputString("n223432s");

    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        Random r = new Random();

        long seed = r.nextLong();
        //long seed = -2043309164258407346L;
        System.out.println(seed);

        Map map = new Map(seed);

        TETile[][] finalWorldFrame = map.MapGenerator();
        ter.renderFrame(finalWorldFrame);

    }
}
