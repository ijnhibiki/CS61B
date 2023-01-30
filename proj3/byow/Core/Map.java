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

    private int AvatarX = 0;
    private int AvatarY = 0;

    private String Avatar;




    private final Random RANDOM;

    //private HashMap<Integer, Boolean> map;

    private ArrayList<Room> rooms;

    private WeightedQuickUnionUF CheckSet;

    public Map(long SEED) {
        this.RANDOM = new Random(SEED);
    }



    public TETile[][] MapGenerator() {
        this.Avatar = "Golden Bear";
        int NumRoom =RANDOM.nextInt(MAX_NUM_ROOM - MIX_NUM_ROOM) + MIX_NUM_ROOM;
        int notConnected = NumRoom;
        CheckSet = new WeightedQuickUnionUF(WIDTH * HEIGHT + 1);
        rooms = new ArrayList<>();
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        frameInitialize(world);
        while (NumRoom > 0) {
            int RoomHeight = RANDOM.nextInt(MAX_ROOM_HEIGHT - MIX_ROOM_HEIGHT) + MIX_ROOM_HEIGHT;
            int RoomLength = RANDOM.nextInt(MAX_ROOM_LENGTH - MIX_ROOM_LENGTH) + MIX_ROOM_LENGTH;
            int Xcord = RANDOM.nextInt(WIDTH);
            int Ycord = RANDOM.nextInt(HEIGHT);

            if (ValidateHouse(world, Xcord, Ycord, RoomLength, RoomHeight)) {
                SetHouse(world,Xcord, Ycord, RoomLength, RoomHeight);
                NumRoom -= 1;
                Room NewRoom = new Room(RoomHeight, RoomLength, Xcord, Ycord);
                rooms.add(NewRoom);
            }
        }
        NumRoom = notConnected;
        boolean R1 = true;
        int StartRoom = RANDOM.nextInt(NumRoom);
        CheckSet.union(xyTo1D(rooms.get(StartRoom).getXCoordinate() + 1, rooms.get(StartRoom).getYCoordinate() + 1), WIDTH*HEIGHT);
        rooms.get(StartRoom).connect();
        while (notConnected > 0) {
            int Room1;
            int Room2;
            if (R1) {
                Room1 = StartRoom;
                do {
                    Room2 = RANDOM.nextInt(NumRoom);
                } while (Room1 == Room2);
            } else {
                do {
                    Room1 = RANDOM.nextInt(NumRoom);
                    Room2 = RANDOM.nextInt(NumRoom);
                } while (rooms.get(Room1).isConnected() || Room1 == Room2);

            }
            R1 = false;
            int x1 = RANDOM.nextInt(2,rooms.get(Room1).getLength() - 2) + rooms.get(Room1).getXCoordinate();
            int x2 = RANDOM.nextInt(2,rooms.get(Room2).getLength() - 2) + rooms.get(Room2).getXCoordinate();
            int y1 = RANDOM.nextInt(2,rooms.get(Room1).getHeight() - 2) + rooms.get(Room1).getYCoordinate();
            int y2 = RANDOM.nextInt(2,rooms.get(Room2).getHeight() - 2) + rooms.get(Room2).getYCoordinate();
            SetTwoPointPath(world,x1,x2,y1,y2);
            rooms.get(Room1).connect();
            notConnected -= (update(NumRoom) + 1);
        }
        OuterWorld(world);
        SetAvatar(world, NumRoom);

        return world;
    }

    private void frameInitialize(TETile[][] input) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                input[x][y] = Tileset.NOTHING;
                //map.put(x * 100 + y, false);
            }
        }
    }

    private void SetAvatar(TETile[][] input, int NumRoom) {
        int BornRoom = RANDOM.nextInt(NumRoom);
        int BornX = rooms.get(BornRoom).getXCoordinate() + 1 + RANDOM.nextInt(rooms.get(BornRoom).getLength() -2);
        int BornY = rooms.get(BornRoom).getYCoordinate() + 1 + RANDOM.nextInt(rooms.get(BornRoom).getHeight() -2);
        input[BornX][BornY] = Tileset.AVATAR;
        AvatarX = BornX;
        AvatarY = BornY;

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
                //map.put((x+i) * 100 + (y + j), true);
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
    private boolean ValidateHouse(TETile[][] input, int x, int y, int length, int height) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j ++) {
                if ((x + i) >= WIDTH - 3 || (y + j) >= HEIGHT -3|| x < 3|| y < 3 || isOverlap(input,x + i, y + j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isOverlap(TETile[][] input, int x, int y) {
        return input[x][y] == Tileset.WALL || input[x][y] == Tileset.FLOOR;
    }

    private int xyTo1D(int x, int y) {
        return x + (y * WIDTH);
    }

    private int update(int NumRoom) {
        int RoomCounter = 0;
        for (int i = 0; i < NumRoom; i ++) {
            if(!rooms.get(i).isConnected() && CheckSet.connected(xyTo1D(rooms.get(i).getXCoordinate()+1, rooms.get(i).getYCoordinate()+1), WIDTH * HEIGHT)) {
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
                    int tileSelector = RANDOM.nextInt(100) + 1;
                    if (tileSelector < 3) {
                        input[x][y] = Tileset.FLOWER;
                    }
                    if (tileSelector >= 3 && tileSelector < 6) {
                        input[x][y] = Tileset.TREE;
                    }
                    if (tileSelector >= 6 && tileSelector < 24) {
                        input[x][y] = Tileset.WATER;
                    }
                    if (tileSelector >=24 && tileSelector < 90) {
                        input[x][y] = Tileset.GRASS;
                    }
                    if (tileSelector >= 90) {
                        input[x][y] = Tileset.MOUNTAIN;
                    }
                }
            }
        }
    }

    public boolean moveAvatar(TETile[][] input, int n){
        boolean moved = false;
        if (n == 1 && accessible(input, AvatarX + 1, AvatarY)) {
            //move right
            input[AvatarX][AvatarY] = Tileset.FLOOR;
            input[AvatarX + 1][AvatarY] = Tileset.AVATAR;
            AvatarX = AvatarX + 1;
            moved = true;
        }
        if (n == 2 && accessible(input, AvatarX - 1, AvatarY)) {
            //move left
            input[AvatarX][AvatarY] = Tileset.FLOOR;
            input[AvatarX - 1][AvatarY] = Tileset.AVATAR;
            AvatarX = AvatarX - 1;
            moved = true;
        }
        if (n == 3 && accessible(input, AvatarX, AvatarY + 1)) {
            //move up
            input[AvatarX][AvatarY] = Tileset.FLOOR;
            input[AvatarX][AvatarY + 1] = Tileset.AVATAR;
            AvatarY = AvatarY + 1;
            moved = true;
        }
        if (n == 4 && accessible(input, AvatarX, AvatarY - 1)) {
            //move down
            input[AvatarX][AvatarY] = Tileset.FLOOR;
            input[AvatarX][AvatarY - 1] = Tileset.AVATAR;
            AvatarY = AvatarY - 1;
            moved = true;
        }
        return moved;
    }
    private boolean accessible(TETile[][] input, int x, int y) {
        if (input[x][y] == Tileset.WALL) {
            return false;
        }
        return true;
    }
    public String AvatarName(){
        return this.Avatar;
    }

    public void ChangeName(String input) {
        this.Avatar = input;
    }

}
