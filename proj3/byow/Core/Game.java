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

    private boolean EnterSeed;

    private boolean inSetting;
    private InputSource inputSource;

    private TERenderer ter;

    private long SEED;

    private Map map;


    public Game() {
        this.width = 60;
        this.height = 45;
        this.isMenu = true;
        this.EnterSeed = false;
        this.inSetting = false;
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
                if (inSetting && (command == 'Q'||command == 'q')) {
                    if(WithKeyboard) {
                        DrawMenu();
                        this.inSetting = false;
                    }
                }
                else if (!inSetting && (command == 'Q'||command == 'q')) {
                    System.exit(0);
                }

                if (command == 'N'||command == 'n') {
                    SEED = 0;
                    EnterSeed = true;
                    if(WithKeyboard) {
                        DrawSeedInput();
                    }
                }
                else if (EnterSeed && (command == 'S'||command == 's')) {
                    EnterSeed = false;
                    this.isMenu = false;
                    map = new Map(SEED);
                    world = map.MapGenerator();
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(world);
                    //break;
                }
                else if (!EnterSeed && (command == 'S'||command == 's')) {
                    this.inSetting = true;
                    DrawSetting();
                    //break;
                }
                else {
                    SEED = SEED*10 + Integer.parseInt(String.valueOf(command));
                    if(WithKeyboard) {
                        SeedInput(String.valueOf(SEED));
                    }
                }
            }
            else {
                if (command == 'D'||command == 'd') {
                    map.moveAvatar(world,1);
                }
                if (command == 'A'||command == 'a') {
                    map.moveAvatar(world,2);
                }
                if (command == 'W'||command == 'w') {
                    map.moveAvatar(world,3);
                }
                if (command == 'S'||command == 's') {
                    map.moveAvatar(world,4);
                }
                ter.renderFrame(world);
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
        StdDraw.text(width / 2, height / 3, "Load Game (L)");
        StdDraw.text(width / 2, height / 3.7, "Setting (S)");
        StdDraw.text(width / 2, height / 4.8, "Quit (Q)");
        StdDraw.show();

    }
    public void DrawSeedInput() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height /4 * 3, "Enter the seed: (End with S)");
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



}
