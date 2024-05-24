package game;

public class Room {
    public int x;
    public int y;

    public Node[][] board;

    public Room(Node[][] boardIn, int xIn, int yIn){
        board = boardIn;
        x = xIn;
        y = yIn;
    }

}
