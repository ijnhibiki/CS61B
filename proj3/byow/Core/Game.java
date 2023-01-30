package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.Core.Map;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.lang.*;

import byow.Core.Engine;
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

    private InputSource inputSource;

    private TERenderer ter;

    private long SEED;
    private boolean toChangeName;

    private Map map;
    private boolean moved;
    private String NewName;


    public Game() {
        this.width = 60;
        this.height = 45;
        this.isMenu = true;
        this.isSubMenu = false;
        this.moved = false;
        this.isSeed = false;
        this.ter = new TERenderer();
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
                    map = new Map(SEED);
                    world = map.MapGenerator();
                    if (toChangeName) {
                        map.ChangeName(NewName);
                    }
                    ter.initialize(WIDTH, HEIGHT + 2);
                    ter.renderFrame(world);
                    HUD(world);
                }
                else if (!isSubMenu && (command == 'S'||command == 's')) {
                    this.isSubMenu = true;
                    DrawSetting();
                }
                else if (!isSubMenu && (command == 'A'||command == 'a')){
                    this.isSubMenu = true;
                    Nameinput("");
                    NewName = "";
                    while(inputSource.possibleNextInput()) {
                        char name = inputSource.getNextKey();
                        if (name != '/') {
                            NewName += Character.toLowerCase(name) ;
                            Nameinput(NewName);
                        } else {
                            toChangeName = NewName.length() > 0;
                            break;
                        }
                        Nameinput(NewName);
                    }
                }
                else if (!isSubMenu && command == 'L'||command == 'l') {
                    System.out.println("fiat lux");
                    if(WithKeyboard) {
                        System.out.println("light");
                    }
                }
                else if (isSeed){
                    SEED = SEED*10 + Integer.parseInt(String.valueOf(command));
                    if(WithKeyboard) {
                        SeedInput(String.valueOf(SEED));
                    }
                }
            }
            else {
                moved = false;
                if (command == 'D'||command == 'd') {
                    moved = map.moveAvatar(world,1);
                }
                if (command == 'A'||command == 'a') {
                    moved = map.moveAvatar(world,2);
                }
                if (command == 'W'||command == 'w') {
                    moved = map.moveAvatar(world,3);
                }
                if (command == 'S'||command == 's') {
                    moved = map.moveAvatar(world,4);
                }if (WithKeyboard) {
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
        StdDraw.text(width / 2, height /4, "Press Q to quit");
        StdDraw.show();
    }
    public void HUD(TETile[][] input) {
        //StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontSmall = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.textLeft(0 ,HEIGHT + 1, map.AvatarName());
        if(moved) {
            StdDraw.text(WIDTH/2,HEIGHT + 1, "Go bears!");

        } else {
            StdDraw.text(WIDTH/2,HEIGHT + 1, "Try another direction!");

        }
        int MouseX = (int) StdDraw.mouseX();
        int MouseY = (int) StdDraw.mouseY();
        if (MouseX >=0 && MouseX < WIDTH && MouseY >=0 && MouseY < HEIGHT) {
            StdDraw.textRight(WIDTH ,HEIGHT + 1, input[MouseX][MouseY].description());
        }
        StdDraw.show();
    }



    public void Nameinput(String input) {
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









}
