package byow.Core;

public class Room {
    private int height;
    private int length;
    private int[] coordinate;

    private boolean Connected;
    public Room (int height, int length, int X, int Y) {
        this.height = height;
        this.length = length;
        this.Connected = false;
        coordinate = new int[]{X, Y};
    }

    public int getHeight() {
        return height;
    }
    public int getLength() {
        return length;
    }

    public int getXCoordinte() {
        return coordinate[0];
    }
    public int getYCoordinte() {
        return coordinate[1];
    }

    public boolean isConnected() {
        return Connected;
    }

    public void connect() {
        this.Connected = true;
    }


}
