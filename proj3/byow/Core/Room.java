package byow.Core;

public class Room {
    private int height;
    private int length;
    private int[] coordinate;
    public Room (int height, int length, int RX, int RY, int LX, int LY) {
        this.height = height;
        this.length = length;
        coordinate = new int[]{RX, RY, LX, LY};
    }

    public int getHeight() {
        return height;
    }
    public int getLength() {
        return length;
    }

    public int getCoordinte(int index) {
        return coordinate[index];
    }


}
