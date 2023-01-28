package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashMap;
import java.util.Random;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;
public class Map {
    private static final int MIX_NUM_ROOM = 15;
    private static final int MAX_NUM_ROOM = 25;
    private static final int MAX_ROOM_HEIGHT = 14;
    private static final int MIX_ROOM_HEIGHT = 6;
    private static final int MAX_ROOM_LENGTH = 14;
    private static final int MIX_ROOM_LENGTH = 6;




    private final Random RANDOM;

    private HashMap<Integer, Boolean> map;

    public Map(long SEED) {
        this.RANDOM = new Random(SEED);
    }



    public TETile[][] MapGenerator() {
        int NumRoom =RANDOM.nextInt(MAX_NUM_ROOM - MIX_NUM_ROOM) + MIX_NUM_ROOM;
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        map = new HashMap<>();
        frameInitialize(world);
        while (NumRoom > 0) {
            int RoomHeight = RANDOM.nextInt(MAX_ROOM_HEIGHT - MIX_ROOM_HEIGHT) + MIX_ROOM_HEIGHT;
            int RoomLength = RANDOM.nextInt(MAX_ROOM_LENGTH - MIX_ROOM_LENGTH) + MIX_ROOM_LENGTH;

            int Xcord = RANDOM.nextInt(WIDTH);
            int Ycord = RANDOM.nextInt(HEIGHT);

            if (ValidateHouse(Xcord, Ycord, RoomLength, RoomHeight)) {
                SetHouse(world,Xcord, Ycord, RoomLength, RoomHeight);
                NumRoom -= 1;
            }
        }
        return world;
    }

    private void frameInitialize(TETile[][] input) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                input[x][y] = Tileset.NOTHING;
                map.put(x * 100 + y, false);
            }
        }
    }

    private void SetHouse(TETile[][] input, int x, int y, int length, int height) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j ++) {
                if (i == 0 || j == 0 || i == length -1 || j == height - 1) {
                    input[x+i][j+y] = Tileset.WALL;
                } else {
                    input[x+i][j+y] = Tileset.FLOOR;
                }
                map.put((x+i) * 100 + (y + j), true);
            }
        }
    }



    private boolean ValidateHouse(int x, int y, int length, int height) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j ++) {
                if ((x + i) >= WIDTH || (y + j) >= HEIGHT || isOverlap(x + i, y + j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isOverlap(int x, int y) {
        return map.get(x * 100 + y);
    }

}
