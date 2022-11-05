package PlusWorld;
import org.junit.Test;
import static org.junit.Assert.*;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of plus shaped regions.
 */
public class PlusWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.FLOOR;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }

    private static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private static void randomPlus(TETile[][] tiles, int s) {
        int rowCounter = 1;
        int colCounter = 0;
        int initialRow = getRandomNumber(0, s -1);
        int initialCol = getRandomNumber(-s + 1, 0);
        addPlus(tiles, s, initialCol, initialRow);
        while (emptyChecker(tiles)) {
            if (initialCol - s > WIDTH ) {
                switch (rowCounter % 3) {
                    case 0 : addPlus(tiles, s, initialCol, initialRow);
                    case 1 : addPlus(tiles, s, initialCol, initialRow -s);
                    case 2 : addPlus(tiles, s, initialCol, initialRow + s);
                }
                rowCounter += 1;
                initialCol += s;
            }else{
                initialRow += s;
                switch (colCounter % 3) {
                    case 1 : initialCol += s;
                    case 2 : initialCol -= s;
                    default: initialCol = initialCol;
                }
                colCounter += 1;
                addPlus(tiles, s, initialCol, initialRow);
            }
        }
    }

    private static boolean emptyChecker(TETile[][] tiles) {
        for (int i = 0; i < WIDTH; i ++ ) {
            for (int j = 0; j < HEIGHT; j ++) {
                if (tiles[i][j] == Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void addPlus(TETile[][] tiles, int s, int col, int row) {
        TETile temp = randomTile();
        addblock(tiles, temp,s, col, row);
        addblock(tiles,temp, s, col + s, row);
        addblock(tiles,temp, s, col - s, row);
        addblock(tiles,temp, s, col, row + s);
        addblock(tiles,temp, s, col, row - s);
    }

    private static void addblock(TETile[][] tiles, TETile current, int s, int col, int row) {
        for (int i = 0; i < s; i ++) {
            for (int j = 0; j < s; j ++) {
                if (col > 0 && col < WIDTH && row > 0 && row < HEIGHT) {
                    tiles[col][row] = current;
                }
                col += 1;
            }
            col = col -s;
            row += 1;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        randomPlus(world,3);



        ter.renderFrame(world);

    }
}
