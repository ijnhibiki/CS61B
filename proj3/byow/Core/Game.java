package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.Scanner;
import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;


public class Game {

    /** The width of the window of this game. */
    private int width;
    private TETile[][] world;
    /** The height of the window of this game. */
    private int height;

    private boolean isMenu;

    private boolean isSubMenu;


    private boolean isSeed;

    private boolean inGame;

    private InputSource inputSource;

    private TERenderer ter;

    private long SEED;
    private boolean toChangeName;

    private Map map;
    private boolean isBlocked;
    private String AvatarName;

    private boolean readySave;

    private String keys;

    private boolean isSubSubMenu;

    private boolean WinOrLose;
    private boolean Hammer;
    private int LenAtTranported;

    private int NumRooms;
    private boolean setRoom;
    private boolean setHeight;
    private boolean setWidth;
    private int MapHeight;
    private int MapWidth;
    private boolean FiatLux;

    private int NumCoin;

    private int NumHammer;
    private int health;





    public Game() {
        this.width = 60;
        this.height = 45;
        this.MapHeight = HEIGHT;
        this.MapWidth = WIDTH;
        this.isMenu = true;
        this.isSubMenu = false;
        this.isBlocked = false;
        this.isSeed = false;
        this.readySave = false;
        this.inGame = false;
        this.AvatarName = "";
        this.ter = new TERenderer();
        this.keys = "";
        this.NumRooms = 0;
        this.Hammer = false;
        this.NumHammer = 0;
        this.NumCoin = 0;
        this.setRoom = false;
        this.setWidth = false;
        this.setHeight = false;
        this.isSubSubMenu = false;
        this.FiatLux = false;
        this.health = 10;
        this.LenAtTranported = 0;
        StdDraw.setCanvasSize(width * 16, height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public TETile[][] StartGame(boolean WithKeyboard, String input) {
        if (WithKeyboard) {
            inputSource = new KeyboardInputSource();
            DrawMenu();
        } else {
            inputSource = new StringInputDevice(input);
        }

        while(inputSource.possibleNextInput()) {
            char command = inputSource.getNextKey();
            if (this.isMenu) {
                if (isSubMenu && (command == 'Q'|| command == 'q')) {
                    if (WithKeyboard) {
                        DrawMenu();
                        this.isSubMenu = false;
                    }
                } else if (!isSubMenu && (command == 'Q'|| command == 'q')) {
                    System.exit(0);
                } else if (!isSubMenu && command == 'N'|| command == 'n') {
                    SEED = 0;
                    isSubMenu = true;
                    isSeed = true;
                    if(WithKeyboard) {
                        SeedInput("");
                    }
                } else if (isSubMenu && (command == 'S'|| command == 's')) {
                    isSubMenu = false;
                    this.isMenu = false;
                    this.inGame = true;
                    map = new Map(SEED, this.NumRooms, this.setRoom, this.MapHeight, this.setHeight, this.MapWidth, this.setWidth, this.Hammer, this.WinOrLose);
                    world = map.MapGenerator();
                    if (toChangeName) {
                        map.ChangeName(AvatarName);
                    } else {
                        AvatarName = "Golden Bear";
                        map.ChangeName(AvatarName);
                    }
                    if (this.SEED == 247) {
                        AvatarName = "Berkelium";
                        map.ChangeName(AvatarName);
                        this.NumHammer = 200;
                        map.changeHammer(200);

                    }
                    if (WithKeyboard) {
                        ter.initialize(this.MapWidth, this.MapHeight + 2);
                        ter.renderFrame(world, map.AvatarX(), map.AvatarY(), this.FiatLux);
                        HUD(world);
                    }
                }
                else if (!isSubMenu && (command == 'S'|| command == 's')) {
                    this.isSubMenu = true;
                    DrawSetting();
                    while(inputSource.possibleNextInput()) {
                        char choice = inputSource.getNextKey();
                        if (choice == '1') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            int temp = 0;
                            MapSize(String.valueOf(this.MapWidth), 1);
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c != '/') {
                                    if (Character.isDigit(c)) {
                                        temp= temp*10 + Character.getNumericValue(c);
                                        if(WithKeyboard) {
                                            MapSize(Integer.toString(temp),1);
                                        }
                                    }
                                } else {
                                    if (!(temp == 0)) {
                                        this.MapWidth = temp;
                                        this.setWidth = true;
                                        MapSize(Integer.toString(temp),1);
                                    } else {
                                        MapSize(Integer.toString(this.MapWidth),1);
                                    }
                                    break;
                                }
                            }

                        }
                        else if (choice == '2') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            int temp = 0;
                            MapSize(String.valueOf(this.MapHeight), 2);
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c != '/') {
                                    if (Character.isDigit(c)) {
                                        temp = temp *10 + Character.getNumericValue(c);
                                        if(WithKeyboard) {
                                            MapSize(Integer.toString(temp),2);
                                        }
                                    }
                                } else {

                                    if (!(temp == 0)) {
                                        this.MapHeight = temp;
                                        this.setHeight = true;
                                        MapSize(Integer.toString(temp),2);
                                    } else  {
                                        MapSize(Integer.toString(this.MapHeight),2);
                                    }

                                    break;
                                }
                            }
                        }
                        else if (choice == '3') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            NumRoom("Random");
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c != '/') {
                                    if (Character.isDigit(c)) {
                                        this.NumRooms = this.NumRooms *10 + Character.getNumericValue(c);
                                        if(WithKeyboard) {
                                            NumRoom(Integer.toString(this.NumRooms));
                                        }
                                    }
                                } else {
                                    NumRoom(Integer.toString(this.NumRooms));
                                    this.setRoom = true;
                                    break;
                                }
                            }
                        }
                        else if (choice == '4') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            WinOrLose(String.valueOf(this.WinOrLose));
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c == '1') {
                                    this.WinOrLose = true;
                                    WinOrLose(String.valueOf(c));
                                }
                                else if(c == '2') {
                                    this.WinOrLose = false;
                                    WinOrLose(String.valueOf(c));
                                }
                                else if (c == '/'){
                                    WinOrLose(String.valueOf(this.WinOrLose));
                                    break;
                                }
                            }
                        }
                        else if (choice == '5') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            Hammer(String.valueOf(this.Hammer));
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c == '1') {
                                    this.Hammer = true;
                                    Hammer(String.valueOf(c));
                                }
                                else if(c == '2') {
                                    this.Hammer = false;
                                    Hammer(String.valueOf(c));
                                }
                                else if (c == '/'){
                                    Hammer(String.valueOf(this.Hammer));
                                    break;
                                }
                            }
                        }
                        else if (!isSubSubMenu && (choice == 'Q'||choice == 'q')){
                            DrawMenu();
                            this.isSubMenu = false;
                            break;
                        }
                        else if (isSubSubMenu && (choice == 'Q'||choice == 'q')) {
                            DrawSetting();
                            this.isSubSubMenu = false;
                            this.isSubMenu = true;
                            //break;
                        }
                    }
                }
                else if (!isSubMenu && (command == 'A'||command == 'a')){
                    this.isSubMenu = true;
                    NameInput("");
                    AvatarName = "";
                    while(inputSource.possibleNextInput()) {
                        char name = inputSource.getNextKey();
                        if (name != '/') {
                            AvatarName += Character.toLowerCase(name) ;
                            NameInput(AvatarName);
                        } else {
                            toChangeName = AvatarName.length() > 0;
                            break;
                        }
                        NameInput(AvatarName);
                    }

                }
                else if (!isSubMenu && command == 'L'||command == 'l') {
                    if(!Load()) {
                        System.exit(0);
                    }
                    isSubMenu = false;
                    this.isMenu = false;
                    this.inGame = true;
                    map = new Map(SEED, this.NumRooms, this.setRoom, this.MapHeight, this.setHeight, this.MapWidth, this.setWidth, this.Hammer, this.WinOrLose);
                    world = map.MapGenerator();
                    if (toChangeName) {
                        map.ChangeName(AvatarName);
                    }
                    for (int i = 0; i < this.keys.length(); i++) {
                        move(this.keys.charAt(i));
                    }
                    if (WithKeyboard) {
                        ter.initialize(this.MapWidth, this.MapHeight + 2);
                        ter.renderFrame(world, map.AvatarX(), map.AvatarY(), this.FiatLux);
                        HUD(world);
                    }

                }
                else if (isSeed){
                    if (Character.isDigit(command)) {
                        SEED = SEED*10 + Character.getNumericValue(command);
                        if(WithKeyboard) {
                            SeedInput(Long.toString(SEED));
                        }
                    }


                }
            }
            else if (inGame){

                if (isMoveCommand(command)) {
                    this.keys += command;
                    move(command);
                    if(!map.isActive() && world[map.PX1()][map.PY1()] != Tileset.ClOSE_PORTAL) {
                        world[map.PX1()][map.PY1()] = Tileset.ClOSE_PORTAL;
                        world[map.PX2()][map.PY2()] = Tileset.ClOSE_PORTAL;
                        this.LenAtTranported = keys.length();
                    }
                    this.NumHammer = map.NumHammer();
                    if (keys.length() >= (LenAtTranported + 5) && !map.isActive()) {
                        world[map.PX1()][map.PY1()] = Tileset.OPENED_PORTAL;
                        world[map.PX2()][map.PY2()] = Tileset.OPENED_PORTAL;
                        map.toActive();
                        this.LenAtTranported = 0;
                    }
                }
                else if (!readySave && command == ':') {
                    this.readySave = true;
                } else if (readySave && (command == 'Q'|| command == 'q')) {
                    SaveFile();
                    System.exit(0);
                }  else if (command == 'l' || command == 'L') {
                    this.FiatLux = !this.FiatLux;
                }

                this.NumCoin = map.NumCoin();
                this.health = map.HP();

                if (WithKeyboard) {
                    ter.renderFrame(world, map.AvatarX(), map.AvatarY(), this.FiatLux);
                    HUD(world);
                }
                if (this.health == 0) {
                    Lose();
                    System.exit(0);
                }
                if(this.NumCoin == 7) {
                    Win();
                    System.exit(0);
                }

            }
        }
        return world;
    }



    public void DrawMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        Font fontSmall = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 4 * 3, "CS61B: THE GAME");
        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, height / 2.5, "New Game (N)");
        StdDraw.text(width / 2, height / 2.9, "Load Game (L)");
        StdDraw.text(width / 2, height / 3.5, "Setting (S)");
        StdDraw.text(width / 2, height / 4.4, "Avatar (A)");
        StdDraw.text(width / 2, height / 5.7, "Quit (Q)");
        StdDraw.show();

    }
    public void SeedInput(String input) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height /4 * 3, "Enter the seed: (End with S)");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(this.width / 2, this.height / 2, input);
        StdDraw.show();
    }

    public void DrawSetting() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height /4 * 3, "Settings");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, height / 2.5, "Change Map Width (1)");
        StdDraw.text(width / 2, height / 2.9, "Change Map Height (2)");
        StdDraw.text(width / 2, height / 3.5, "Change Number of Room (3)");
        StdDraw.text(width / 2, height / 4.4, "Win and Lose (4)");
        StdDraw.text(width / 2, height / 5.7, "Hammer (5)");
        StdDraw.show();
    }
    public void HUD(TETile[][] input) {
        //StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontSmall = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.textLeft(0 ,this.MapHeight + 1, map.AvatarName());
        StdDraw.textRight(23 ,this.MapHeight + 1, CurrentTime());
        if(!isBlocked) {
            StdDraw.text(27,this.MapHeight + 1, "Go bears!");

        } else {
            StdDraw.text(27,this.MapHeight + 1, "Go back!");
        }
        StdDraw.text(35,this.MapHeight + 1, "Hammer: " + this.NumHammer);
        StdDraw.text(42,this.MapHeight + 1, "Coin: " + this.NumCoin);
        StdDraw.text(53,this.MapHeight + 1, "l :open/close light");
        StdDraw.text(65,this.MapHeight + 1, "health: " + this.health);
        int MouseX = (int) StdDraw.mouseX();
        int MouseY = (int) StdDraw.mouseY();
        if (MouseX >=0 && MouseX < this.MapWidth && MouseY >=0 && MouseY < this.MapHeight) {
            if (this.FiatLux) {
                StdDraw.textRight(this.MapWidth ,this.MapHeight + 1, input[MouseX][MouseY].description());
            } else {
                if (Math.abs(MouseX - map.AvatarX()) >=4 || Math.abs(MouseY - map.AvatarY()) >=4) {
                    StdDraw.textRight(this.MapWidth ,this.MapHeight + 1, "???");
                } else {
                    StdDraw.textRight(this.MapWidth ,this.MapHeight + 1, input[MouseX][MouseY].description());
                }
            }

        } else {
            StdDraw.textRight(this.MapWidth ,this.MapHeight + 1, "???");
        }
        StdDraw.line(0,this.MapHeight + 0.5, this.MapWidth + 0.5, this.MapHeight + 0.5);

        StdDraw.show(100);
    }
    public void Win() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 2, "You Win!");
        StdDraw.show();
        StdDraw.pause(3000);

    }
    public void Lose() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 2, "You Lose!");
        StdDraw.show();
        StdDraw.pause(3000);

    }

    public String CurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }


    public void NameInput(String input) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height /4 * 3, "Enter your name: (End with /)");
        StdDraw.text(width / 2, height /4, "Press Q to quit");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(this.width / 2, this.height / 2, input);
        StdDraw.show();
    }

    public void move(Character command) {
        if (command == 'D'||command == 'd') {
            this.isBlocked = map.moveAvatar(world,1);

        }
        if (command == 'A'||command == 'a') {
            this.isBlocked = map.moveAvatar(world,2);

        }
        if (command == 'W'||command == 'w') {
            this.isBlocked = map.moveAvatar(world,3);

        }
        if (command == 'S'||command == 's') {
            this.isBlocked = map.moveAvatar(world,4);

        }

    }

    public boolean isMoveCommand(Character input) {
        return input == 'w' || input == 'W' || input == 's' ||input == 'S' || input == 'a' || input == 'A' || input == 'd' || input == 'D';
    }

    public void SaveFile() {
        File file = new File("./save.txt");
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter writer = new PrintWriter("save.txt", StandardCharsets.UTF_8);
            writer.println(this.SEED);
            writer.println(this.keys);
            writer.println(this.AvatarName);
            writer.println(this.setWidth);
            writer.println(this.MapWidth);
            writer.println(this.setHeight);
            writer.println(this.MapHeight);
            writer.println(setRoom);
            writer.println(NumRooms);
            writer.println(WinOrLose);
            writer.println(Hammer);
            writer.close();
        } catch (ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean Load() {
        try {
            File save = new File("save.txt");
            if (!save.exists()) {
                return false;
            }
            Scanner myReader = new Scanner(save);
            this.SEED = Long.parseLong(myReader.nextLine());
            this.keys = myReader.nextLine();
            this.AvatarName = myReader.nextLine();
            this.setWidth = Boolean.parseBoolean(myReader.nextLine());
            this.MapWidth = Integer.parseInt(myReader.nextLine());
            this.setHeight = Boolean.parseBoolean(myReader.nextLine());
            this.MapHeight = Integer.parseInt(myReader.nextLine());
            this.setRoom = Boolean.parseBoolean(myReader.nextLine());
            this.NumRooms = Integer.parseInt(myReader.nextLine());
            this.WinOrLose = Boolean.parseBoolean(myReader.nextLine());
            this.Hammer = Boolean.parseBoolean(myReader.nextLine());
            myReader.close();
            //generate world with seed
            //for char in game string, process char
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void NumRoom(String input) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 4 * 3, "Enter the number of room you want: (End with /)");
        StdDraw.text(width / 2, height / 4, "Press Q to quit");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(this.width / 2, this.height / 2, input);
        StdDraw.show();
    }

    public void WinOrLose(String input) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 4 * 3, "Press 1 to enable win or lose Press 2 to disable : (End with /)");
        StdDraw.text(width / 2, height / 4, "Press Q to quit");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(this.width / 2, this.height / 2, input);
        StdDraw.show();
    }
    public void Hammer(String input) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 4 * 3, "Press 1 to enable Hammer Press 2 to disable : (End with /)");
        StdDraw.text(width / 2, height / 4, "Press Q to quit");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(this.width / 2, this.height / 2, input);
        StdDraw.show();
    }

    public void MapSize(String input, int indicator) {
        String type;
        if (indicator == 1) {
            type = "width";
        } else {
            type = "height";
        }
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 4 * 3, "Enter the " + type + " you want: (End with /)");
        StdDraw.text(width / 2, height / 4, "Press Q to quit");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(this.width / 2, this.height / 2, input);
        StdDraw.show();
    }


    

    //Add portals to your world which teleport the avatar.
}
