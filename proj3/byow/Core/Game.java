package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.Scanner;
import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    private boolean lost;
    private boolean inGame;

    private InputSource inputSource;

    private TERenderer ter;

    private long SEED;
    private boolean toChangeName;

    private Map map;
    private boolean isBlocked;
    private String NewName;

    private boolean readySave;

    private String keys;

    private boolean isSubSubMenu;

    private boolean WinOrLose;
    private boolean Hammer;

    private int Numrooms;
    private boolean setRoom;
    private boolean setHeight;
    private boolean setwidth;
    private int MapHeight;
    private int MapWidth;





    public Game() {
        this.width = 60;
        this.height = 45;
        this.MapHeight = HEIGHT;
        this.MapWidth = WIDTH;
        this.isMenu = true;
        this.isSubMenu = false;
        this.isBlocked = false;
        this.isSeed = false;
        this.lost = false;
        this.readySave = false;
        this.inGame = false;
        this.ter = new TERenderer();
        this.keys = "";
        this.Numrooms = 0;
        this.setRoom = false;
        this.setwidth = false;
        this.setHeight = false;
        this.isSubSubMenu = false;
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
            if(this.isMenu) {
                if (isSubMenu && (command == 'Q'||command == 'q')) {
                    if(WithKeyboard) {
                        DrawMenu();
                        this.isSubMenu = false;
                    }
                }
                else if (!isSubMenu && (command == 'Q'||command == 'q')) {
                    System.exit(0);
                }

                else if (!isSubMenu && command == 'N'||command == 'n') {
                    SEED = 0;
                    isSubMenu = true;
                    isSeed = true;
                    if(WithKeyboard) {
                        SeedInput("");
                    }
                }
                else if (isSubMenu && (command == 'S'||command == 's')) {
                    isSubMenu = false;
                    this.isMenu = false;
                    this.inGame = true;
                    map = new Map(SEED, this.Numrooms, this.setRoom, this.MapHeight, this.setHeight, this.MapWidth, this.setwidth);
                    world = map.MapGenerator();
                    if (toChangeName) {
                        map.ChangeName(NewName);
                    }
                    if (WithKeyboard) {
                        ter.initialize(this.MapWidth, this.MapHeight + 2);
                        ter.renderFrame(world);
                        HUD(world);
                    }
                }
                else if (!isSubMenu && (command == 'S'||command == 's')) {
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
                                    MapSize(Integer.toString(temp),1);
                                    this.setwidth = true;
                                    this.MapWidth = temp;
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
                                    MapSize(Integer.toString(temp),2);
                                    this.setHeight = true;
                                    this.MapHeight = temp;
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
                                        this.Numrooms = this.Numrooms *10 + Character.getNumericValue(c);
                                        if(WithKeyboard) {
                                            NumRoom(Integer.toString(this.Numrooms));
                                        }
                                    }
                                } else {
                                    NumRoom(Integer.toString(this.Numrooms));
                                    this.setRoom = true;
                                    break;
                                }
                            }
                        }
                        else if (choice == '4') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            boolean indicator = this.WinOrLose;
                            WinOrLose(String.valueOf(indicator));
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
                                    indicator = this.WinOrLose;
                                    WinOrLose(String.valueOf(indicator));
                                    break;
                                }
                            }
                        }
                        else if (choice == '5') {
                            this.isSubSubMenu = true;
                            this.isSubMenu = false;
                            boolean indicator = this.Hammer;
                            Hammer(String.valueOf(indicator));
                            while(inputSource.possibleNextInput()) {
                                char c = inputSource.getNextKey();
                                if (c == '1') {
                                    this.WinOrLose = true;
                                    Hammer(String.valueOf(c));
                                }
                                else if(c == '2') {
                                    this.WinOrLose = false;
                                    Hammer(String.valueOf(c));
                                }
                                else if (c == '/'){
                                    indicator = this.Hammer;
                                    Hammer(String.valueOf(indicator));
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
                    NewName = "";
                    while(inputSource.possibleNextInput()) {
                        char name = inputSource.getNextKey();
                        if (name != '/') {
                            NewName += Character.toLowerCase(name) ;
                            NameInput(NewName);
                        } else {
                            toChangeName = NewName.length() > 0;
                            break;
                        }
                        NameInput(NewName);
                    }

                }
                else if (!isSubMenu && command == 'L'||command == 'l') {
                    if(!Load()) {
                        System.exit(0);
                    }
                    isSubMenu = false;
                    this.isMenu = false;
                    this.inGame = true;
                    map = new Map(SEED, this.Numrooms, this.setRoom, this.MapHeight, this.setHeight, this.MapWidth, this.setwidth);
                    world = map.MapGenerator();
                    if (toChangeName) {
                        map.ChangeName(NewName);
                    }
                    for (int i = 0; i < this.keys.length(); i++) {
                        move(this.keys.charAt(i));
                    }
                    if (WithKeyboard) {
                        ter.initialize(this.MapWidth, this.MapHeight + 2);
                        ter.renderFrame(world);
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
                }
                else if (!readySave && command == ':') {
                    this.readySave = true;
                } else if (readySave && (command == 'Q'|| command == 'q')) {
                    SaveFile();
                    System.exit(0);

                }
                if (WithKeyboard) {
                    ter.renderFrame(world);
                    HUD(world);
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
        StdDraw.textRight(this.MapWidth/3 ,this.MapHeight + 1, CurrentTime());
        if(!isBlocked) {
            StdDraw.text(this.MapWidth/3 * 2,this.MapHeight + 1, "Go bears!");

        } else {
            StdDraw.text(this.MapWidth/3 * 2,this.MapHeight + 1, "Try another direction!");
        }
        int MouseX = (int) StdDraw.mouseX();
        int MouseY = (int) StdDraw.mouseY();
        if (MouseX >=0 && MouseX < this.MapWidth && MouseY >=0 && MouseY < this.MapHeight) {
            StdDraw.textRight(this.MapWidth ,this.MapHeight + 1, input[MouseX][MouseY].description());
        }
        StdDraw.line(0,this.MapHeight + 0.5, this.MapWidth + 0.5, this.MapHeight + 0.5);

        StdDraw.show(100);
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
        return input == 'w' || input == 'W' || input == 's' ||input == 'S' || input == 'a' || input == 'A' ||input == 'd' ||input == 'D';
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
            String keys = myReader.nextLine();
            myReader.close();
            //generate world with seed
            this.keys = keys;
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
        StdDraw.text(width / 2, height /4 * 3, "Enter the number of room you want: (End with /)");
        StdDraw.text(width / 2, height /4, "Press Q to quit");
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
        StdDraw.text(width / 2, height /4 * 3, "Press 1 to enable win or lose 2 to disable : (End with /)");
        StdDraw.text(width / 2, height /4, "Press Q to quit");
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
        StdDraw.text(width / 2, height /4 * 3, "Press 1 to enable win or lose 2 to disable : (End with /)");
        StdDraw.text(width / 2, height /4, "Press Q to quit");
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
        StdDraw.text(width / 2, height /4 * 3, "Enter the " +type+ " you want: (End with /)");
        StdDraw.text(width / 2, height /4, "Press Q to quit");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(fontSmall);
        StdDraw.text(this.width / 2, this.height / 2, input);
        StdDraw.show();
    }


    //Add a menu option or randomly determine what the environment/theme of the world will be.

    //Win or lose

    //Add portals to your world which teleport the avatar.
    //Add a hammer
    //Add some neat easter eggs or cheat codes to your game which do something fun
}
