package byow.Core;

public class Room {
    private final int height;
    private final int length;
    private final int[] coordinate;
    private boolean hasCoin;
    private boolean hasHammer;

    private boolean Connected;
    public Room (int height, int length, int X, int Y) {
        this.height = height;
        this.length = length;
        this.Connected = false;
        this.hasCoin = false;
        this.hasHammer = false;
        coordinate = new int[]{X, Y};
    }

    public int getHeight() {
        return height;
    }
    public int getLength() {
        return length;
    }

    public int getXCoordinate() {
        return coordinate[0];
    }
    public int getYCoordinate() {
        return coordinate[1];
    }

    public boolean isConnected() {
        return Connected;
    }

    public void connect() {
        this.Connected = true;
    }
    public boolean hasCoin() {
        return this.hasCoin;
    }
    public boolean hasHammer(){
        return this.hasHammer;
    }


}
