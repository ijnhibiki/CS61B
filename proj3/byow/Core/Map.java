package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;
public class Map {
    private static final int MIX_NUM_ROOM = 10;
    private static final int MAX_NUM_ROOM = 20;



    private final Random RANDOM;

    public Map(int SEED) {
        this.RANDOM = new Random(SEED);
    }



    public TETile[][] MapGenerator() {
        int NumRoom =RANDOM.nextInt(MAX_NUM_ROOM - MIX_NUM_ROOM) + MIX_NUM_ROOM;
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        frameInitialize(world);
        while (NumRoom > 0) {

        }



        return null;
    }

    private TETile[][] frameInitialize(TETile[][] input) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                input[x][y] = Tileset.NOTHING;
            }
        }
        return input;
    }

}
