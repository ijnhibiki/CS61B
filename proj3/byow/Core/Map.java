package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;
public class Map {
    private static final int MIX_NUM_ROOM = 20;
    private static final int MAX_NUM_ROOM = 25;
    private static final int MAX_ROOM_HEIGHT = 14;
    private static final int MIX_ROOM_HEIGHT = 6;
    private static final int MAX_ROOM_LENGTH = 14;
    private static final int MIX_ROOM_LENGTH = 6;




    private final Random RANDOM;

    private HashMap<Integer, Boolean> map;

    private ArrayList<Room> rooms;

    private WeightedQuickUnionUF CheckSet;

    public Map(long SEED) {
        this.RANDOM = new Random(SEED);
    }



    public TETile[][] MapGenerator() {
        int NumRoom =RANDOM.nextInt(MAX_NUM_ROOM - MIX_NUM_ROOM) + MIX_NUM_ROOM;
        int notConnected = NumRoom;
        CheckSet = new WeightedQuickUnionUF(WIDTH * HEIGHT + 1);
        rooms = new ArrayList<>();
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
                Room NewRoom = new Room(RoomHeight, RoomLength, Xcord, Ycord);
                rooms.add(NewRoom);
            }
        }
        NumRoom = notConnected;
        int StartRoom = RANDOM.nextInt(NumRoom);
        CheckSet.union(xyTo1D(rooms.get(StartRoom).getXCoordinte() + 1, rooms.get(StartRoom).getYCoordinte() + 1), WIDTH*HEIGHT);

        while (notConnected > 0) {
            int Room1;
            int Room2;
            do {
                Room1 = RANDOM.nextInt(NumRoom);
                Room2 = RANDOM.nextInt(NumRoom);
            } while (rooms.get(Room1).isConnected() || Room1 == Room2);
            int x1 = RANDOM.nextInt(2,rooms.get(Room1).getLength() - 2) + rooms.get(Room1).getXCoordinte();
            int x2 = RANDOM.nextInt(2,rooms.get(Room2).getLength() - 2) + rooms.get(Room2).getXCoordinte();
            int y1 = RANDOM.nextInt(2,rooms.get(Room1).getHeight() - 2) + rooms.get(Room1).getYCoordinte();
            int y2 = RANDOM.nextInt(2,rooms.get(Room2).getHeight() - 2) + rooms.get(Room2).getYCoordinte();
            SetTwoPointPath(world,x1,x2,y1,y2);
            rooms.get(Room1).connect();
            notConnected -= (update(NumRoom) + 1);
        }
        //OuterWorld(world);
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
                    CheckSet.union(xyTo1D((x+i), (y + j)), xyTo1D((x+1), (y+1)));
                }
                map.put((x+i) * 100 + (y + j), true);
            }
        }
    }

    private void SetHPath(TETile[][] input, int y, int a, int b) {
        int x1 = Math.min(a,b);
        int x2 = Math.max(a,b);

        for (int i = x1; i <= x2; i++) {
            input[i][y] = Tileset.FLOOR;
            CheckSet.union(xyTo1D((i), (y)), xyTo1D(x1,y));
            if (!input[i][y+1].equals(Tileset.FLOOR) ) {
                input[i][y+1] = Tileset.WALL;
            }
            if (!input[i][y-1].equals(Tileset.FLOOR) ) {
                input[i][y-1] = Tileset.WALL;
            }

        }
        if (!input[x1][y].equals(Tileset.FLOOR) ){
            input[x1][y] = Tileset.WALL;
        }
        if (!input[x2][y].equals(Tileset.FLOOR)){
            input[x2][y] = Tileset.WALL;
        }
    }


    private void SetVPath(TETile[][] input, int x, int a, int b) {
        int y1 = Math.min(a,b);
        int y2 = Math.max(a,b);

        for (int i = y1 -1; i <= y2 +1; i++) {
            if(i != y1 -1 && i != y2 + 1) {
                input[x][i] = Tileset.FLOOR;
                CheckSet.union(xyTo1D((x), (i)), xyTo1D(x,y1));
            }
            if (!input[x+1][i].equals(Tileset.FLOOR) ) {
                input[x+1][i] = Tileset.WALL;
            }
            if (!input[x-1][i].equals(Tileset.FLOOR)) {
                input[x-1][i] = Tileset.WALL;
            }

        }
        if (!input[x][y1].equals(Tileset.FLOOR)){
            input[x][y1] = Tileset.WALL;
        }
        if (!input[x][y2].equals(Tileset.FLOOR)){
            input[x][y2] = Tileset.WALL;
        }
    }

    private void SetTwoPointPath(TETile[][] input, int x1, int x2, int y1, int y2) {
        if (y1 == y2) {
            SetHPath(input, y1, x1, x2);
        }
        if (x1 == x2) {
            SetVPath(input, x1, y1, y2);
        } else {
            int turningPoint = RANDOM.nextInt(Math.min(x1,x2), Math.max(x1,x2));
            SetHPath(input, y1, x1, turningPoint);
            SetHPath(input, y2, turningPoint, x2);
            SetVPath(input,turningPoint, y1, y2);
            input[turningPoint][y1] = Tileset.FLOOR;
            input[turningPoint][y2] = Tileset.FLOOR;
        }

    }
    private boolean ValidateHouse(int x, int y, int length, int height) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j ++) {
                if ((x + i) >= WIDTH - 3 || (y + j) >= HEIGHT -3|| x < 3|| y < 3 || isOverlap(x + i, y + j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isOverlap(int x, int y) {
        return map.get(x * 100 + y);
    }

    private int xyTo1D(int x, int y) {
        return x + (y * WIDTH);
    }

    private int update(int NumRoom) {
        int RoomCounter = 0;
        for (int i = 0; i < NumRoom; i ++) {
            if(!rooms.get(i).isConnected() && CheckSet.connected(xyTo1D(rooms.get(i).getXCoordinte()+1, rooms.get(i).getYCoordinte()+1), WIDTH * HEIGHT)) {
                rooms.get(i).connect();
                RoomCounter += 1;
            }
        }
        return RoomCounter;
    }



    private void OuterWorld(TETile[][] input) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (!input[x][y].equals(Tileset.WALL) && !input[x][y].equals(Tileset.FLOOR)) {
                    int tileNum = RANDOM.nextInt(5);
                    switch (tileNum) {
                        case 0:
                            input[x][y] = Tileset.WATER;
                            break;
                        case 1:
                            input[x][y] = Tileset.FLOWER;
                            break;
                        case 2:
                            input[x][y] = Tileset.SAND;
                            break;
                        case 3:
                            input[x][y] = Tileset.TREE;
                            break;
                        case 4:
                            input[x][y] = Tileset.GRASS;
                            break;
                        default:
                            input[x][y] = Tileset.NOTHING;
                            break;
                    }
                }
            }
        }
    }

}
