package byow.TileEngineRemote;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class TilesetRemote {
    public static final TETileRemote AVATAR1 = new TETileRemote('@', Color.white, Color.black, "you");
    public static final TETileRemote AVATAR2 = new TETileRemote('☺', Color.white, Color.black, "you");
    public static final TETileRemote AVATAR3 = new TETileRemote('♡', Color.white, Color.black, "you");
    public static final TETileRemote WALL = new TETileRemote('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETileRemote FLOOR = new TETileRemote('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETileRemote NOTHING = new TETileRemote(' ', Color.black, Color.black, "nothing");
    public static final TETileRemote GRASS = new TETileRemote('"', Color.green, Color.black, "grass");
    public static final TETileRemote WATER = new TETileRemote('≈', Color.blue, Color.black, "water");
    public static final TETileRemote FLOWER = new TETileRemote('❀', Color.magenta, Color.pink, "flower");
    public static final TETileRemote LOCKED_DOOR = new TETileRemote('█', Color.orange, Color.black,
            "locked door");
    public static final TETileRemote UNLOCKED_DOOR = new TETileRemote('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETileRemote SAND = new TETileRemote('▒', Color.yellow, Color.black, "sand");
    public static final TETileRemote MOUNTAIN = new TETileRemote('▲', Color.gray, Color.black, "mountain");
    public static final TETileRemote TREE = new TETileRemote('♠', Color.green, Color.black, "tree");

    public static final TETileRemote HAMMER = new TETileRemote('T', Color.black, Color.blue, "hammer");

    public static final TETileRemote COIN = new TETileRemote('⍟', Color.YELLOW, Color.BLACK, "coin");
    public static final TETileRemote OPENED_PORTAL = new TETileRemote('ⵔ', Color.BLUE, Color.BLACK, "opened portal");
    public static final TETileRemote ClOSE_PORTAL = new TETileRemote('⃠', Color.BLUE, Color.BLACK, "closed portal");


}


