package byow.TileEngineRemote;


import byow.Networking.BYOWServer;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERendererRemote {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff, boolean remote, BYOWServer server) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        if (!remote) {
            StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        } else {
            StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
            server.sendCanvasConfig(width * TILE_SIZE, height * TILE_SIZE);
        }

        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        if (!remote) {
            StdDraw.show();
        } else {
            StdDraw.show();
            server.sendCanvas();
        }

    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, boolean remote, BYOWServer server) {
        initialize(w, h, 0, 0, remote, server);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETileRemote[][] world, int avatarX, int avatarY, boolean fiatLux, boolean remote, BYOWServer server) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        if (fiatLux) {
            for (int x = 0; x < numXTiles; x += 1) {
                for (int y = 0; y < numYTiles; y += 1) {
                    if (world[x][y] == null) {
                        throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                                + " is null.");
                    }
                    world[x][y].draw(x + xOffset, y + yOffset);
                }
            }
        } else {
            for (int x = avatarX - 4; x <= avatarX + 4; x++) {
                for (int y = avatarY - 4; y <= avatarY + 4; y++) {
                    /*if (world[x][y] == null) {
                        throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                                + " is null.");
                    } */
                    if (lightCheck(world, x, y, avatarX, avatarY) && lightCheck2(world, x, y, avatarX, avatarY)) {
                        world[x][y].draw(x + xOffset, y + yOffset);
                    }
                }
            }
        }
        if (!remote) {
            StdDraw.show();
        } else {
            StdDraw.show();
            server.sendCanvas();
        }

    }

    public boolean lightCheck(TETileRemote[][] input, int x, int y, int avatarX, int avatarY) {
        if (x < 0 || x >= input.length || y < 0 || y >= input[0].length) {
            return false;
        }
        if (x == avatarX && y == avatarY) {
            return true;
        } else if (y == avatarY && x > avatarX - 5 && x < avatarX + 5) {
            return true;
        } else if ((y == avatarY + 1 || y == avatarY - 1) && x > avatarX - 4 && x < avatarX + 4) {
            return true;
        } else if ((y == avatarY + 2 || y == avatarY - 2) && x > avatarX - 3 && x < avatarX + 3) {
            return true;
        } else if ((y == avatarY + 3 || y == avatarY - 3) && x > avatarX - 2 && x < avatarX + 2) {
            return true;
        } else if ((y == avatarY + 4 || y == avatarY - 4) && x > avatarX - 1 && x < avatarX + 1) {
            return true;
        } else {
            return false;
        }

    }


    public boolean lightCheck2(TETileRemote[][] input, int x, int y, int avatarX, int avatarY) {
        if (x >= avatarX - 1 && x <= avatarX + 1 && y >= avatarY - 1 && y <= avatarY + 1) {
            return true;
        } else if (y >= avatarY + 2 && x >= avatarX - 1 && x <= avatarX + 1) {
            for (int i = y - 1; i > avatarY; i--) {
                if (input[x][i] == TilesetRemote.WALL) {
                    return false;
                }
            }
            return true;
        } else if (y <= avatarY - 2 && x >= avatarX - 1 && x <= avatarX + 1) {
            for (int i = y + 1; i < avatarY; i++) {
                if (input[x][i] == TilesetRemote.WALL) {
                    return false;
                }
            }
            return true;
        } else if (x >= avatarX + 2 && y >= avatarY - 1 && y <= avatarY + 1) {
            for (int i = x - 1; i > avatarX; i--) {
                if (input[i][y] == TilesetRemote.WALL) {
                    return false;
                }
            }
            return true;
        } else if (x <= avatarX - 2 && y >= avatarY - 1 && y <= avatarY + 1) {
            for (int i = x + 1; i < avatarX; i++) {
                if (input[i][y] == TilesetRemote.WALL) {
                    return false;
                }
            }
            return true;
        } else if (x == avatarX - 2 && y == avatarY + 2) {
            return input[x][y - 1] != TilesetRemote.WALL && input[x + 1][y] != TilesetRemote.WALL && input[x + 1][y - 1] != TilesetRemote.WALL && input[x + 1][y - 2] != TilesetRemote.WALL;
        } else if (x == avatarX + 2 && y == avatarY + 2) {
            return input[x][y - 1] != TilesetRemote.WALL && input[x - 1][y] != TilesetRemote.WALL && input[x - 1][y - 1] != TilesetRemote.WALL && input[x - 1][y - 2] != TilesetRemote.WALL;
        } else if (x == avatarX - 2 && y == avatarY - 2) {
            return input[x][y + 1] != TilesetRemote.WALL && input[x + 1][y] != TilesetRemote.WALL && input[x + 1][y - 1] != TilesetRemote.WALL && input[x + 1][y - 2] != TilesetRemote.WALL;
        } else if (x == avatarX + 2 && y == avatarY - 2) {
            return input[x][y - 1] != TilesetRemote.WALL && input[x - 1][y] != TilesetRemote.WALL && input[x - 1][y - 1] != TilesetRemote.WALL && input[x - 1][y - 2] != TilesetRemote.WALL;
        }
        return true;
    }


}
