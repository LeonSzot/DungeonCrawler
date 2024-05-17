package game;

public class Room {
    public int x;
    public int y;

    public Integer[][] board;

    public Room(Integer[][] boardIn, int xIn, int yIn){
        board = boardIn;
        x = xIn;
        y = yIn;
    }

}
