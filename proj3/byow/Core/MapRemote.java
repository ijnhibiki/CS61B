package byow.Core;

import byow.TileEngineRemote.TETileRemote;
import byow.TileEngineRemote.TilesetRemote;

import java.util.ArrayList;
import java.util.Random;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class MapRemote {
    private static final int MIX_NUM_ROOM = 20;
    private static final int MAX_NUM_ROOM = 25;
    private static final int MAX_ROOM_HEIGHT = 14;
    private static final int MIX_ROOM_HEIGHT = 6;
    private static final int MAX_ROOM_LENGTH = 14;
    private static final int MIX_ROOM_LENGTH = 6;

    private int AvatarX = 0;
    private int AvatarY = 0;

    private int NumRoom;

    private String Avatar;

    private boolean setRooms;
    private boolean setHeight;
    private boolean setWidth;
    private int MapHeight;
    private int MapWidth;
    private boolean WinOrLose;
    private boolean Hammer;

    private int NumHammer;
    private int NumCoin;
    private int health;

    private TETileRemote temp;
    private boolean extraRoom;

    private int ExRoomX;
    private int ExRoomY;
    private int PX1;
    private int PY1;
    private int PX2;
    private int PY2;
    private boolean isActive;
    private int avatarSelector;







    private final Random RANDOM;

    private ArrayList<Room> rooms;

    private WeightedQuickUnionUF CheckSet;

    public MapRemote(long SEED, int NumRoom, boolean setRooms, int MapHeight, boolean setHeight, int MapWidth, boolean setWidth
    ,boolean Hammer, boolean WinOrLose, int avatarSelector) {
        this.RANDOM = new Random(SEED);
        this.NumRoom = NumRoom;
        this.health = 10;
        this.setRooms = setRooms;
        this.MapHeight = MapHeight;
        this.setHeight = setHeight;
        this.MapWidth = MapWidth;
        this.setWidth = setWidth;
        this.Hammer = Hammer;
        this.extraRoom = false;
        this.NumCoin = 0;
        this.ExRoomX = 0;
        this.ExRoomY = 0;
        this.isActive = true;
        this.PX1 = 0;
        this.PX2 = 0;
        this.PY1 = 0;
        this.PY2 = 0;
        this.avatarSelector = avatarSelector;

        this.NumHammer = 0;
        this.WinOrLose = WinOrLose;
        this.temp = TilesetRemote.FLOOR;


    }



    public TETileRemote[][] MapGenerator() {
        this.Avatar = "Golden Bear";
        if (!setRooms) {
            this.NumRoom =RANDOM.nextInt(MAX_NUM_ROOM - MIX_NUM_ROOM) + MIX_NUM_ROOM;
        }
        int notConnected = NumRoom;
        CheckSet = new WeightedQuickUnionUF(this.MapWidth * this.MapHeight + 1);
        rooms = new ArrayList<>();
        TETileRemote[][] world = new TETileRemote[this.MapWidth][this.MapHeight];
        frameInitialize(world);
        while (NumRoom > 0) {
            int RoomHeight = RANDOM.nextInt(MAX_ROOM_HEIGHT - MIX_ROOM_HEIGHT) + MIX_ROOM_HEIGHT;
            int RoomLength = RANDOM.nextInt(MAX_ROOM_LENGTH - MIX_ROOM_LENGTH) + MIX_ROOM_LENGTH;
            int Xcord = RANDOM.nextInt(this.MapWidth);
            int Ycord = RANDOM.nextInt(this.MapHeight);

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
        CheckSet.union(xyTo1D(rooms.get(StartRoom).getXCoordinate() + 1, rooms.get(StartRoom).getYCoordinate() + 1), this.MapWidth*this.MapHeight);
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
        SetPortal(world, this.Hammer);
        if(Hammer) {
            SetHammer(world, NumRoom);

        }
        if(WinOrLose) {
            SetCoin(world);
            SetSand(world);
            SetHammer(world, NumRoom);
        }
        return world;
    }

    private void frameInitialize(TETileRemote[][] input) {
        for (int x = 0; x < this.MapWidth; x += 1) {
            for (int y = 0; y < this.MapHeight; y += 1) {
                input[x][y] = TilesetRemote.NOTHING;
                //map.put(x * 100 + y, false);
            }
        }
    }

    private void SetCoin(TETileRemote[][] input) {
        for (int i = 0; i < 7; i ++){
            int CoinRoom = RANDOM.nextInt(NumRoom);
            if (!rooms.get(CoinRoom).hasCoin()) {
                int CoinX = rooms.get(CoinRoom).getXCoordinate() + 1 + RANDOM.nextInt(rooms.get(CoinRoom).getLength() -2);
                int CoinY = rooms.get(CoinRoom).getYCoordinate() + 1 + RANDOM.nextInt(rooms.get(CoinRoom).getHeight() -2);
                if (input[CoinX][CoinY] != TilesetRemote.HAMMER) {
                    input[CoinX][CoinY] = TilesetRemote.COIN;
                } else {
                    i -= 1;
                }
            }else {
                i -=1;
            }

        }
    }
    private void SetPortal(TETileRemote[][] input, boolean exRoom) {
        if(exRoom) {
            this.extraRoom = true;
            int extraRoom = 1;
            while (extraRoom > 0) {
                int RoomHeight = RANDOM.nextInt(MAX_ROOM_HEIGHT - MIX_ROOM_HEIGHT) + MIX_ROOM_HEIGHT;
                int RoomLength = RANDOM.nextInt(MAX_ROOM_LENGTH - MIX_ROOM_LENGTH) + MIX_ROOM_LENGTH;
                int Xcord = RANDOM.nextInt(this.MapWidth);
                int Ycord = RANDOM.nextInt(this.MapHeight);

                if (ValidateHouse(input, Xcord, Ycord, RoomLength, RoomHeight)) {
                    SetHouse(input,Xcord, Ycord, RoomLength, RoomHeight);
                    extraRoom -= 1;
                    this.ExRoomX = Xcord;
                    this.ExRoomY = Ycord;
                    Room NewRoom = new Room(RoomHeight, RoomLength, Xcord, Ycord);
                    rooms.add(NewRoom);
                }
            }
        }

        int PX1;
        int PY1;
        int PX2;
        int PY2;
        if (exRoom) {
            PX1 = this.ExRoomX;
            PY1 = this.ExRoomY;
            PX1 += 2;
            PY1 += 2;
        } else {
            PX1 = 2;
            PY1 = 2;
        }
        int Room2 = RANDOM.nextInt(NumRoom);
        PX2 = rooms.get(Room2).getXCoordinate();
        PX2 += 2;
        PY2 = rooms.get(Room2).getYCoordinate();
        PY2 += 2;
        input[PX1][PY1] = TilesetRemote.OPENED_PORTAL;
        input[PX2][PY2] = TilesetRemote.OPENED_PORTAL;
        this.PY1 = PY1;
        this.PY2 = PY2;
        this.PX1 = PX1;
        this.PX2 = PX2;

    }

    private void SetAvatar(TETileRemote[][] input, int NumRoom) {
        int BornRoom = RANDOM.nextInt(NumRoom);
        int BornX = rooms.get(BornRoom).getXCoordinate() + 1 + RANDOM.nextInt(rooms.get(BornRoom).getLength() -2);
        int BornY = rooms.get(BornRoom).getYCoordinate() + 1 + RANDOM.nextInt(rooms.get(BornRoom).getHeight() -2);
        input[BornX][BornY] = avatarSelector(this.avatarSelector);
        AvatarX = BornX;
        AvatarY = BornY;

    }
    private void SetHammer(TETileRemote[][] input, int NumRoom) {
        for (int i = 0; i < NumRoom / 5; i ++){
            int HammerRoom = RANDOM.nextInt(NumRoom);
            if (!rooms.get(HammerRoom).hasHammer()) {
                int HammerX = rooms.get(HammerRoom).getXCoordinate() + 1 + RANDOM.nextInt(rooms.get(HammerRoom).getLength() -2);
                int HammerY = rooms.get(HammerRoom).getYCoordinate() + 1 + RANDOM.nextInt(rooms.get(HammerRoom).getHeight() -2);
                if (input[HammerX][HammerY] != TilesetRemote.COIN) {
                    input[HammerX][HammerY] = TilesetRemote.HAMMER;
                }else {
                    i -= 1;
                }
            } else {
                i -=1;
            }

        }
    }
    private void SetSand(TETileRemote[][] input) {
        for (int i = 0; i < 20; i ++){
            int SandX = RANDOM.nextInt(this.MapWidth);
            int SandY = RANDOM.nextInt(this.MapHeight);
            if (input[SandX][SandY] == TilesetRemote.FLOOR) {
                    input[SandX][SandY] = TilesetRemote.SAND;
                }else {
                    i -= 1;
                }

            }


    }

    private void SetHouse(TETileRemote[][] input, int x, int y, int length, int height) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j ++) {
                if (i == 0 || j == 0 || i == length -1 || j == height - 1) {
                    input[x+i][j+y] = TilesetRemote.WALL;
                } else {
                    input[x+i][j+y] = TilesetRemote.FLOOR;
                    CheckSet.union(xyTo1D((x+i), (y + j)), xyTo1D((x+1), (y+1)));
                }
            }
        }
    }

    private void SetHPath(TETileRemote[][] input, int y, int a, int b) {
        int x1 = Math.min(a,b);
        int x2 = Math.max(a,b);

        for (int i = x1; i <= x2; i++) {
            input[i][y] = TilesetRemote.FLOOR;
            CheckSet.union(xyTo1D((i), (y)), xyTo1D(x1,y));
            if (!input[i][y+1].equals(TilesetRemote.FLOOR) ) {
                input[i][y+1] = TilesetRemote.WALL;
            }
            if (!input[i][y-1].equals(TilesetRemote.FLOOR) ) {
                input[i][y-1] = TilesetRemote.WALL;
            }

        }
        if (!input[x1][y].equals(TilesetRemote.FLOOR) ){
            input[x1][y] = TilesetRemote.WALL;
        }
        if (!input[x2][y].equals(TilesetRemote.FLOOR)){
            input[x2][y] = TilesetRemote.WALL;
        }
    }


    private void SetVPath(TETileRemote[][] input, int x, int a, int b) {
        int y1 = Math.min(a,b);
        int y2 = Math.max(a,b);

        for (int i = y1 -1; i <= y2 +1; i++) {
            if(i != y1 -1 && i != y2 + 1) {
                input[x][i] = TilesetRemote.FLOOR;
                CheckSet.union(xyTo1D((x), (i)), xyTo1D(x,y1));
            }
            if (!input[x+1][i].equals(TilesetRemote.FLOOR) ) {
                input[x+1][i] = TilesetRemote.WALL;
            }
            if (!input[x-1][i].equals(TilesetRemote.FLOOR)) {
                input[x-1][i] = TilesetRemote.WALL;
            }

        }
        if (!input[x][y1].equals(TilesetRemote.FLOOR)){
            input[x][y1] = TilesetRemote.WALL;
        }
        if (!input[x][y2].equals(TilesetRemote.FLOOR)){
            input[x][y2] = TilesetRemote.WALL;
        }
    }

    private void SetTwoPointPath(TETileRemote[][] input, int x1, int x2, int y1, int y2) {
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
            input[turningPoint][y1] = TilesetRemote.FLOOR;
            input[turningPoint][y2] = TilesetRemote.FLOOR;
        }

    }
    private boolean ValidateHouse(TETileRemote[][] input, int x, int y, int length, int height) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j ++) {
                if ((x + i) >= this.MapWidth - 3 || (y + j) >= this.MapHeight -3|| x < 3|| y < 3 || isOverlap(input,x + i, y + j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isOverlap(TETileRemote[][] input, int x, int y) {
        return input[x][y] == TilesetRemote.WALL || input[x][y] == TilesetRemote.FLOOR;
    }

    private int xyTo1D(int x, int y) {
        return x + (y * this.MapWidth);
    }

    private int update(int NumRoom) {
        int RoomCounter = 0;
        for (int i = 0; i < NumRoom; i ++) {
            if(!rooms.get(i).isConnected() && CheckSet.connected(xyTo1D(rooms.get(i).getXCoordinate()+1, rooms.get(i).getYCoordinate()+1), this.MapWidth * this.MapHeight)) {
                rooms.get(i).connect();
                RoomCounter += 1;
            }
        }
        return RoomCounter;
    }



    private void OuterWorld(TETileRemote[][] input) {
        for (int x = 0; x < this.MapWidth; x += 1) {
            for (int y = 0; y < this.MapHeight; y += 1) {
                if (!input[x][y].equals(TilesetRemote.WALL) && !input[x][y].equals(TilesetRemote.FLOOR)) {
                    int tileSelector = RANDOM.nextInt(100) + 1;
                    if (tileSelector < 3) {
                        input[x][y] = TilesetRemote.FLOWER;
                    }
                    if (tileSelector >= 3 && tileSelector < 6) {
                        input[x][y] = TilesetRemote.TREE;
                    }
                    if (tileSelector >= 6 && tileSelector < 24) {
                        input[x][y] = TilesetRemote.WATER;
                    }
                    if (tileSelector >=24 && tileSelector < 90) {
                        input[x][y] = TilesetRemote.GRASS;
                    }
                    if (tileSelector >= 90) {
                        input[x][y] = TilesetRemote.MOUNTAIN;
                    }
                }
            }
        }
    }

    public boolean moveAvatar(TETileRemote[][] input, int n){
        if (n == 1 && accessible(input, AvatarX + 1, AvatarY)) {
            //move right
            if (input[AvatarX + 1][AvatarY].equals(TilesetRemote.OPENED_PORTAL)) {
                input[AvatarX][AvatarY] = temp;
                int TargatX = this.PX1;
                int TargatY = this.PY1;
                if (TargatX == AvatarX + 1 && TargatY == AvatarY) {
                    TargatX = this.PX2;
                    TargatY = this.PY2;
                }
                TETileRemote temp2 = input[TargatX + 1][TargatY];
                input[TargatX + 1][TargatY] = avatarSelector(this.avatarSelector);
                this.temp = temp2;
                this.isActive = false;
                AvatarX =TargatX + 1;
                AvatarY = TargatY;
                return false;
            }else {
                input[AvatarX][AvatarY] = temp;
                if (!input[AvatarX + 1][AvatarY].equals(TilesetRemote.WALL)  && !input[AvatarX + 1][AvatarY].equals(TilesetRemote.HAMMER)&& !input[AvatarX + 1][AvatarY].equals(TilesetRemote.COIN)) {
                    temp = input[AvatarX + 1][AvatarY];
                }
                input[AvatarX + 1][AvatarY] = avatarSelector(this.avatarSelector);
                AvatarX = AvatarX + 1;
                return false;
            }

        }
        if (n == 2 && accessible(input, AvatarX - 1, AvatarY)) {
            //move left
            if (input[AvatarX - 1][AvatarY].equals(TilesetRemote.OPENED_PORTAL)) {
                input[AvatarX][AvatarY] = temp;
                int TargatX = this.PX1;
                int TargatY = this.PY1;
                if (TargatX == AvatarX - 1 && TargatY == AvatarY) {
                    TargatX = this.PX2;
                    TargatY = this.PY2;
                }
                TETileRemote temp2 = input[TargatX - 1][TargatY];
                input[TargatX - 1][TargatY] = avatarSelector(this.avatarSelector);
                this.temp = temp2;
                this.isActive = false;
                AvatarX =TargatX - 1;
                AvatarY = TargatY;
                return false;
            }else {
                input[AvatarX][AvatarY] = temp;
                if (!input[AvatarX - 1][AvatarY].equals(TilesetRemote.WALL)  && !input[AvatarX - 1][AvatarY].equals(TilesetRemote.HAMMER)&& !input[AvatarX - 1][AvatarY].equals(TilesetRemote.COIN)) {
                    temp = input[AvatarX - 1][AvatarY];
                }
                input[AvatarX - 1][AvatarY] = avatarSelector(this.avatarSelector);
                AvatarX = AvatarX - 1;
                return false;
            }

        }
        if (n == 3 && accessible(input, AvatarX, AvatarY + 1)) {
            //move up
            if (input[AvatarX][AvatarY + 1].equals(TilesetRemote.OPENED_PORTAL)) {
                input[AvatarX][AvatarY] = temp;
                int TargatX = this.PX1;
                int TargatY = this.PY1;
                if (TargatY == AvatarY + 1&& TargatX == AvatarX) {
                    TargatX = this.PX2;
                    TargatY = this.PY2;
                }
                TETileRemote temp2 = input[TargatX ][TargatY + 1];
                input[TargatX][TargatY + 1] = avatarSelector(this.avatarSelector);
                this.temp = temp2;
                this.isActive = false;
                AvatarX =TargatX;
                AvatarY = TargatY + 1;
                return false;
            }else {
                input[AvatarX][AvatarY] = temp;
                if (!input[AvatarX][AvatarY + 1].equals(TilesetRemote.WALL)  && !input[AvatarX][AvatarY + 1].equals(TilesetRemote.HAMMER)&& !input[AvatarX][AvatarY + 1].equals(TilesetRemote.COIN)) {
                    temp = input[AvatarX][AvatarY + 1];
                }
                input[AvatarX][AvatarY + 1] = avatarSelector(this.avatarSelector);
                AvatarY = AvatarY + 1;

                return false;
            }
        }
        if (n == 4 && accessible(input, AvatarX, AvatarY - 1)) {
            //move down
            if (input[AvatarX ][AvatarY -1].equals(TilesetRemote.OPENED_PORTAL)) {
                input[AvatarX][AvatarY] = temp;
                int TargatX = this.PX1;
                int TargatY = this.PY1;
                if (TargatY == AvatarY - 1&& TargatX == AvatarX) {
                    TargatX = this.PX2;
                    TargatY = this.PY2;
                }
                TETileRemote temp2 = input[TargatX][TargatY - 1];
                input[TargatX][TargatY - 1] = avatarSelector(this.avatarSelector);
                this.temp = temp2;
                this.isActive = false;
                AvatarX =TargatX ;
                AvatarY = TargatY - 1;
                return false;
            }else {
                input[AvatarX][AvatarY] = temp;
                if (!input[AvatarX][AvatarY - 1].equals(TilesetRemote.WALL)  && !input[AvatarX][AvatarY - 1].equals(TilesetRemote.HAMMER)&& !input[AvatarX][AvatarY - 1].equals(TilesetRemote.COIN)) {
                    temp = input[AvatarX][AvatarY - 1];
                }
                input[AvatarX][AvatarY - 1] = avatarSelector(this.avatarSelector);
                AvatarY = AvatarY - 1;
                return false;
            }
        }
        return true;
    }
    private boolean accessible(TETileRemote[][] input, int x, int y) {
        if (x >= this.MapWidth || x <0 || y >= this.MapHeight || y <0){
            return false;
        }
         if (input[x][y] == TilesetRemote.WALL && this.NumHammer == 0) {
            return false;
        }
         if(input[x][y] == TilesetRemote.ClOSE_PORTAL) {
             return false;
         }
         if(input[x][y] == TilesetRemote.OPENED_PORTAL) {
            return true;
        }

         else if(input[x][y] == TilesetRemote.WALL && this.NumHammer > 0) {
             this.NumHammer -= 1;
             return true;
         }
         else if (input[x][y] == TilesetRemote.HAMMER) {
             this.NumHammer += 1;
             return true;
         }
         else if (input[x][y] == TilesetRemote.COIN) {
             this.NumCoin += 1;
             return true;
         }
         else if (input[x][y] == TilesetRemote.SAND) {
             this.health -= 1;
             return true;
         }
        return true;
    }
    public String AvatarName(){
        return this.Avatar;
    }

    public void ChangeName(String input) {
        this.Avatar = input;
    }
    public int AvatarX() {
        return AvatarX;
    }
    public int AvatarY(){
        return AvatarY;
    }
    public int NumCoin() {
        return this.NumCoin;
    }
    public int NumHammer(){
        return this.NumHammer;
    }
    public int HP(){
        return this.health;
    }

    public void changeHammer(int num) {
        this.NumHammer = num;
    }
    public boolean isActive(){
        return isActive;
    }
    public int PX1(){
        return this.PX1;
    }
    public int PY1(){
        return this.PY1;
    }
    public int PX2(){
        return this.PX2;
    }
    public int PY2(){
        return this.PY2;
    }
    public void toActive(){
        this.isActive = true;
    }
    public TETileRemote avatarSelector(int selector) {
        if(selector == 1) {
            return TilesetRemote.AVATAR1;
        } else if(selector == 2) {
            return TilesetRemote.AVATAR2;
        } else {
            return TilesetRemote.AVATAR3;
        }
    }

}
