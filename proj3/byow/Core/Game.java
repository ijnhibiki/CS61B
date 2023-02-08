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
    private String avatarName;

    private boolean readySave;

    private String keys;

    private boolean isSubSubMenu;

    private boolean winOrLose;
    private boolean hasHammer;
    private int lenAtTranported;

    private int numRooms;
    private boolean setRoom;
    private boolean setHeight;
    private boolean setWidth;
    private int mapHeight;
    private int mapWidth;
    private boolean fiatLux;

    private int numCoin;

    private int numHammer;
    private int health;
    private int avatarSelector;






    public Game() {
        this.width = 60;
        this.height = 45;
        this.mapHeight = HEIGHT;
        this.mapWidth = WIDTH;
        this.isMenu = true;
        this.isSubMenu = false;
        this.isBlocked = false;
        this.isSeed = false;
        this.readySave = false;
        this.inGame = false;
        this.avatarName = "";
        this.ter = new TERenderer();
        this.keys = "";
        this.numRooms = 0;
        this.hasHammer = false;
        this.numHammer = 0;
        this.numCoin = 0;
        this.setRoom = false;
        this.setWidth = false;
        this.setHeight = false;
        this.isSubSubMenu = false;
        this.fiatLux = false;
        this.health = 10;
        this.lenAtTranported = 0;
        this.avatarSelector = 1;

    }

    public TETile[][] startGame(boolean showScreen, String input) {
        if (showScreen) {
            StdDraw.setCanvasSize(width * 16, height * 16);
            Font font = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.setXscale(0, width);
            StdDraw.setYscale(0, height);
            StdDraw.clear(Color.BLACK);
            StdDraw.enableDoubleBuffering();
            inputSource = new KeyboardInputSource();
            DrawMenu();
        } else {
            inputSource = new StringInputDevice(input);
        }
        while (inputSource.possibleNextInput()) {
            char command = inputSource.getNextKey();
            if (this.isMenu) {
                if (isSubMenu && (command == 'Q' || command == 'q')) {
                    if (showScreen) {
                        DrawMenu();
                        this.isSubMenu = false;
                    }
                } else if (!isSubMenu && (command == 'Q' || command == 'q')) {
                    System.exit(0);
                } else if (!isSubMenu && command == 'N' || command == 'n') {
                    SEED = 0;
                    isSubMenu = true;
                    isSeed = true;
                    if (showScreen) {
                        SeedInput("");
                    }
                } else if (isSubMenu && (command == 'S' || command == 's')) {
                    isSubMenu = false;
                    this.isMenu = false;
                    this.inGame = true;
                    map = new Map(SEED, this.numRooms, this.setRoom, this.mapHeight, this.setHeight, this.mapWidth, this.setWidth, this.hasHammer, this.winOrLose, this.avatarSelector);
                    world = map.MapGenerator();
                    if (toChangeName) {
                        map.ChangeName(avatarName);
                    } else {
                        avatarName = "Golden Bear";
                        map.ChangeName(avatarName);
                    }
                    if (this.SEED == 247) {
                        avatarName = "Berkelium";
                        this.toChangeName = true;
                        map.ChangeName(avatarName);
                        this.numHammer = 200;
                        this.hasHammer = true;
                        map.changeHammer(200);

                    }
                    if (showScreen) {
                        ter.initialize(this.mapWidth, this.mapHeight + 2, false, null);
                        ter.renderFrame(world, map.AvatarX(), map.AvatarY(), this.fiatLux, false, null);
                        HUD(world);
                    }
                } else if (!isSubMenu && (command == 'S'|| command == 's')) {
                    this.isSubMenu = true;
                    DrawSetting();
                    while(inputSource.possibleNextInput()) {
                        char choice = inputSource.getNextKey();
                        if (choice == '1') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            int temp = 0;
                            mapSize(String.valueOf(this.mapWidth), 1);
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c != '/') {
                                    if (Character.isDigit(c)) {
                                        temp= temp*10 + Character.getNumericValue(c);
                                        if(showScreen) {
                                            mapSize(Integer.toString(temp),1);
                                        }
                                    }
                                } else {
                                    if (!(temp == 0)) {
                                        this.mapWidth = temp;
                                        this.setWidth = true;
                                        mapSize(Integer.toString(temp),1);
                                    } else {
                                        mapSize(Integer.toString(this.mapWidth),1);
                                    }
                                    break;
                                }
                            }

                        }
                        else if (choice == '2') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            int temp = 0;
                            mapSize(String.valueOf(this.mapHeight), 2);
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c != '/') {
                                    if (Character.isDigit(c)) {
                                        temp = temp *10 + Character.getNumericValue(c);
                                        if(showScreen) {
                                            mapSize(Integer.toString(temp),2);
                                        }
                                    }
                                } else {

                                    if (!(temp == 0)) {
                                        this.mapHeight = temp;
                                        this.setHeight = true;
                                        mapSize(Integer.toString(temp),2);
                                    } else  {
                                        mapSize(Integer.toString(this.mapHeight),2);
                                    }

                                    break;
                                }
                            }
                        }
                        else if (choice == '3') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            numRoom("Random");
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c != '/') {
                                    if (Character.isDigit(c)) {
                                        this.numRooms = this.numRooms *10 + Character.getNumericValue(c);
                                        if(showScreen) {
                                            numRoom(Integer.toString(this.numRooms));
                                        }
                                    }
                                } else {
                                    numRoom(Integer.toString(this.numRooms));
                                    this.setRoom = true;
                                    break;
                                }
                            }
                        }
                        else if (choice == '4') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            winOrLose(String.valueOf(this.winOrLose));
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c == '1') {
                                    this.winOrLose = true;
                                    winOrLose(String.valueOf(c));
                                }
                                else if(c == '2') {
                                    this.winOrLose = false;
                                    winOrLose(String.valueOf(c));
                                }
                                else if (c == '/'){
                                    winOrLose(String.valueOf(this.winOrLose));
                                    break;
                                }
                            }
                        }
                        else if (choice == '5') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            hammer(String.valueOf(this.hasHammer));
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c == '1') {
                                    this.hasHammer = true;
                                    hammer(String.valueOf(c));
                                }
                                else if(c == '2') {
                                    this.hasHammer = false;
                                    hammer(String.valueOf(c));
                                }
                                else if (c == '/'){
                                    hammer(String.valueOf(this.hasHammer));
                                    break;
                                }
                            }
                        } else if (!isSubSubMenu && (choice == 'Q'||choice == 'q')){
                            DrawMenu();
                            this.isSubMenu = false;
                            break;
                        } else if (isSubSubMenu && (choice == 'Q'||choice == 'q')) {
                            DrawSetting();
                            this.isSubSubMenu = false;
                            this.isSubMenu = true;
                            //break;
                        }
                    }
                }
                else if (!isSubMenu && (command == 'A'||command == 'a')){
                    this.isSubMenu = true;
                    DrawAvatar();
                    while(inputSource.possibleNextInput()) {
                        char selector = inputSource.getNextKey();
                        if (selector == '1') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            nameInput(this.avatarName);
                            avatarName = "";
                            while(inputSource.possibleNextInput()) {
                                char name = inputSource.getNextKey();
                                if (name != '/') {
                                    avatarName += Character.toLowerCase(name) ;
                                    nameInput(avatarName);
                                } else {
                                    toChangeName = avatarName.length() > 0;
                                    break;
                                }
                                nameInput(avatarName);
                            }
                        } else if (selector == '2') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            DrawAvatarAppearence(String.valueOf(this.avatarSelector));
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c == '1') {
                                    this.avatarSelector = 1;
                                    DrawAvatarAppearence(String.valueOf(this.avatarSelector));
                                }
                                else if(c == '2') {
                                    this.avatarSelector = 2;
                                    DrawAvatarAppearence(String.valueOf(this.avatarSelector));
                                }else if(c == '3') {
                                    this.avatarSelector = 3;
                                    DrawAvatarAppearence(String.valueOf(this.avatarSelector));
                                }
                                else if (c == '/'){
                                    DrawAvatarAppearence(String.valueOf(this.avatarSelector));
                                    break;
                                }
                            }

                        } else if (!isSubSubMenu && (selector == 'Q'||selector == 'q')){
                            DrawMenu();
                            this.isSubMenu = false;
                            break;
                        } else if (isSubSubMenu && (selector == 'Q'||selector == 'q')) {
                            DrawAvatar();
                            this.isSubSubMenu = false;
                            this.isSubMenu = true;
                            //break;
                        }
                    }
                }
                else if (!isSubMenu && command == 'L'||command == 'l') {
                    if(!load()) {
                        System.exit(0);
                    }
                    isSubMenu = false;
                    this.isMenu = false;
                    this.inGame = true;
                    map = new Map(SEED, this.numRooms, this.setRoom, this.mapHeight, this.setHeight, this.mapWidth, this.setWidth, this.hasHammer, this.winOrLose, this.avatarSelector);
                    world = map.MapGenerator();
                    if (toChangeName) {
                        map.ChangeName(avatarName);
                    }
                    if (hasHammer) {
                        map.changeHammer(this.numHammer);
                    }
                    if (this.SEED == 247) {
                        avatarName = "Berkelium";
                        this.toChangeName = true;
                        map.ChangeName(avatarName);
                        this.numHammer = 200;
                        this.hasHammer = true;
                        map.changeHammer(200);

                    }
                    for (int i = 0; i < this.keys.length(); i++) {
                        move(this.keys.charAt(i));
                        if(!map.isActive() && world[map.PX1()][map.PY1()] != Tileset.ClOSE_PORTAL) {
                            world[map.PX1()][map.PY1()] = Tileset.ClOSE_PORTAL;
                            world[map.PX2()][map.PY2()] = Tileset.ClOSE_PORTAL;
                            this.lenAtTranported = keys.length();
                        }
                        this.numHammer = map.NumHammer();
                        if (keys.length() >= (lenAtTranported + 5) && !map.isActive()) {
                            world[map.PX1()][map.PY1()] = Tileset.OPENED_PORTAL;
                            world[map.PX2()][map.PY2()] = Tileset.OPENED_PORTAL;
                            map.toActive();
                            this.lenAtTranported = 0;
                        }
                        this.numCoin = map.NumCoin();
                        this.health = map.HP();
                        if (this.health == 0) {
                            lose();
                            System.exit(0);
                        }
                        if(this.numCoin == 7) {
                            win();
                            System.exit(0);
                        }

                    }

                    if (showScreen) {
                        ter.initialize(this.mapWidth, this.mapHeight + 2, false,null);
                        ter.renderFrame(world, map.AvatarX(), map.AvatarY(), this.fiatLux, false,null);
                        HUD(world);
                    }

                }
                else if (isSeed){
                    if (Character.isDigit(command)) {
                        SEED = SEED*10 + Character.getNumericValue(command);
                        if(showScreen) {
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
                        this.lenAtTranported = keys.length();
                    }
                    this.numHammer = map.NumHammer();
                    if (keys.length() >= (lenAtTranported + 5) && !map.isActive()) {
                        world[map.PX1()][map.PY1()] = Tileset.OPENED_PORTAL;
                        world[map.PX2()][map.PY2()] = Tileset.OPENED_PORTAL;
                        map.toActive();
                        this.lenAtTranported = 0;
                    }
                }
                else if (!readySave && command == ':') {
                    this.readySave = true;
                } else if (readySave && (command == 'Q'|| command == 'q')) {
                    saveFile();
                    if(showScreen) {
                        System.exit(0);
                    }
                }  else if (command == 'l' || command == 'L') {
                    this.fiatLux = !this.fiatLux;
                }

                this.numCoin = map.NumCoin();
                this.health = map.HP();

                if (showScreen) {
                    ter.renderFrame(world, map.AvatarX(), map.AvatarY(), this.fiatLux, false,null);
                    HUD(world);
                }
                if (this.health == 0) {
                    lose();
                    System.exit(0);
                }
                if(this.numCoin == 7) {
                    win();
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

    public void DrawAvatar() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height /4 * 3, "Avatar Customize");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, height / 2.5, "Change User Name (1)");
        StdDraw.text(width / 2, height / 2.9, "Change Avatar Appearance (2)");
        StdDraw.show();
    }

    public void DrawAvatarAppearence(String input) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);

        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 4 * 3, "Select your avatar: (End with /)");
        StdDraw.text(width / 2, height / 5.5, "Press Q to quit");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(this.width / 2, this.height / 2, input);
        StdDraw.text(width / 2, height / 2.5, "(1) @");
        StdDraw.text(width / 2, height / 2.9, "(2) ☺");
        StdDraw.text(width / 2, height / 3.5, "(3) ♡");

        StdDraw.show();
    }
    public void HUD(TETile[][] input) {
        //StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontSmall = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.textLeft(0 ,this.mapHeight + 1, map.AvatarName());
        StdDraw.textRight(23 ,this.mapHeight + 1, currentTime());
        if(!isBlocked) {
            StdDraw.text(27,this.mapHeight + 1, "Go bears!");

        } else {
            StdDraw.text(27,this.mapHeight + 1, "Go back!");
        }
        StdDraw.text(35,this.mapHeight + 1, "Hammer: " + this.numHammer);
        StdDraw.text(42,this.mapHeight + 1, "Coin: " + this.numCoin);
        StdDraw.text(53,this.mapHeight + 1, "l :open/close light");
        StdDraw.text(65,this.mapHeight + 1, "health: " + this.health);
        int MouseX = (int) StdDraw.mouseX();
        int MouseY = (int) StdDraw.mouseY();
        if (MouseX >=0 && MouseX < this.mapWidth && MouseY >=0 && MouseY < this.mapHeight) {
            if (this.fiatLux) {
                StdDraw.textRight(this.mapWidth, this.mapHeight + 1, input[MouseX][MouseY].description());
            } else {
                if (Math.abs(MouseX - map.AvatarX()) >= 4 || Math.abs(MouseY - map.AvatarY()) >= 4) {
                    StdDraw.textRight(this.mapWidth, this.mapHeight + 1, "???");
                } else {
                    StdDraw.textRight(this.mapWidth, this.mapHeight + 1, input[MouseX][MouseY].description());
                }
            }

        } else {
            StdDraw.textRight(this.mapWidth, this.mapHeight + 1, "???");
        }
        StdDraw.line(0, this.mapHeight + 0.5, this.mapWidth + 0.5, this.mapHeight + 0.5);

        StdDraw.show(100);
    }
    public void win() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.mapWidth / 2, this.mapHeight / 2, "You Win!");
        StdDraw.show();
        StdDraw.pause(3000);

    }
    public void lose() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.mapWidth / 2, this.mapHeight / 2, "You Lose!");
        StdDraw.show();
        StdDraw.pause(3000);

    }

    public String currentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }


    public void nameInput(String input) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 4 * 3, "Enter your name: (End with /)");
        StdDraw.text(width / 2, height / 4, "Press Q to quit");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(this.width / 2, this.height / 2, input);
        StdDraw.show();
    }

    public void move(Character command) {
        if (command == 'D' || command == 'd') {
            this.isBlocked = map.moveAvatar(world, 1);

        }
        if (command == 'A' || command == 'a') {
            this.isBlocked = map.moveAvatar(world, 2);

        }
        if (command == 'W' || command == 'w') {
            this.isBlocked = map.moveAvatar(world, 3);

        }
        if (command == 'S' || command == 's') {
            this.isBlocked = map.moveAvatar(world, 4);

        }

    }

    public boolean isMoveCommand(Character input) {
        return input == 'w' || input == 'W' || input == 's' || input == 'S' || input == 'a' || input == 'A' || input == 'd' || input == 'D';
    }

    public void saveFile() {
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
            writer.println(this.toChangeName);
            writer.println(this.avatarName);
            writer.println(this.setWidth);
            writer.println(this.mapWidth);
            writer.println(this.setHeight);
            writer.println(this.mapHeight);
            writer.println(setRoom);
            writer.println(numRooms);
            writer.println(winOrLose);
            writer.println(hasHammer);
            writer.println(this.avatarSelector);
            writer.println(this.fiatLux);
            writer.close();
        } catch (ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean load() {
        try {
            File save = new File("save.txt");
            if (!save.exists()) {
                return false;
            }
            Scanner myReader = new Scanner(save);
            this.SEED = Long.parseLong(myReader.nextLine());
            this.keys = myReader.nextLine();
            this.toChangeName = Boolean.parseBoolean(myReader.nextLine());
            this.avatarName = myReader.nextLine();
            this.setWidth = Boolean.parseBoolean(myReader.nextLine());
            this.mapWidth = Integer.parseInt(myReader.nextLine());
            this.setHeight = Boolean.parseBoolean(myReader.nextLine());
            this.mapHeight = Integer.parseInt(myReader.nextLine());
            this.setRoom = Boolean.parseBoolean(myReader.nextLine());
            this.numRooms = Integer.parseInt(myReader.nextLine());
            this.winOrLose = Boolean.parseBoolean(myReader.nextLine());
            this.hasHammer = Boolean.parseBoolean(myReader.nextLine());
            this.avatarSelector = Integer.parseInt(myReader.nextLine());
            this.fiatLux = Boolean.parseBoolean(myReader.nextLine());
            myReader.close();
            //generate world with seed
            //for char in game string, process char
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void numRoom(String input) {
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

    public void winOrLose(String input) {
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
    public void hammer(String input) {
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

    public void mapSize(String input, int indicator) {
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
}
