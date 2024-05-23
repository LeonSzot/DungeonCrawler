package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Board extends JPanel implements ActionListener, KeyListener {

    // controls the delay between each tick in ms
    private final int DELAY = 25;
    // controls the size of the board
    public static final int TILE_SIZE = 60;
    public static final int ROWS = 15;
    public static final int COLUMNS = 25;
    // suppress serialization warning

    public static final int ROOMS = 10;
    private static final long serialVersionUID = 490905409104883233L;

    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;
    // objects that appear on the game board
    private Player player;
    public ArrayList<Room> rooms;
    public Room currentRoom;



    public Board() {
        // set the game board size
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        setBackground(new Color(232, 232, 232));

        rooms = generateRooms(ROOMS);
        createDoors(rooms);
        currentRoom = rooms.get(0);

        // initialize the game state
        player = new Player(this);

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void createDoors(ArrayList<Room> roomsTemp){
        for (Room room : roomsTemp){
            if (roomExists(roomsTemp, room.x - 1, room.y)){
                room.board[0][(int) Math.floor(ROWS / 2)] = 2;
                room.board[0][(int) Math.floor(ROWS / 2) + 1] = 2;
                room.board[0][(int) Math.floor(ROWS / 2) - 1] = 2;
            }
            if (roomExists(roomsTemp, room.x + 1, room.y)){
                room.board[COLUMNS - 1][(int) Math.floor(ROWS / 2)] = 2;
                room.board[COLUMNS - 1][(int) Math.floor(ROWS / 2) + 1] = 2;
                room.board[COLUMNS - 1][(int) Math.floor(ROWS / 2) - 1] = 2;
            }
            if (roomExists(roomsTemp, room.x, room.y + 1)){
                room.board[(int) Math.floor(COLUMNS / 2)][ROWS - 1] = 2;
                room.board[(int) Math.floor(COLUMNS / 2) + 1][ROWS - 1] = 2;
                room.board[(int) Math.floor(COLUMNS / 2) - 1][ROWS - 1] = 2;
            }
            if (roomExists(roomsTemp, room.x, room.y - 1)){
                room.board[(int) Math.floor(COLUMNS / 2)][0] = 2;
                room.board[(int) Math.floor(COLUMNS / 2) + 1][0] = 2;
                room.board[(int) Math.floor(COLUMNS / 2) - 1][0] = 2;
            }
        }
    }

    private Boolean roomExists(ArrayList<Room> roomsTemp, int x, int y){
        for (Room room : roomsTemp){
            if (x == room.x && y == room.y){
                return true;
            }
        }

        return false;
    }

    public Room getRoom(int x, int y){
        for (Room room : rooms){
            if (x == room.x && y == room.y){
                return room;
            }
        }

        return null;
    }

    private ArrayList<Room> generateRooms(int roomsNum){
        ArrayList<Room> roomsTemp = new ArrayList<>();
        int previousX = 0, previousY = 0;
        Random generator = new Random();
        boolean moveX;
        boolean moveNegative;

        int i = 0;
        while (i < roomsNum){
            if (i == 0){
                roomsTemp.add(new Room(createRoom(), previousX, previousY));
                i++;
            }
            else{
                moveX = generator.nextBoolean();
                moveNegative = generator.nextBoolean();

                if (moveX){
                    if (moveNegative){
                        if (roomExists(roomsTemp, previousX - 1, previousY)){
                            previousX -= 1;
                        }
                        else{
                            roomsTemp.add(new Room(createRoom(), previousX - 1, previousY));
                            previousX -= 1;
                            i++;
                        }
                    }
                    else{
                        if (roomExists(roomsTemp, previousX + 1, previousY)){
                            previousX += 1;
                        }
                        else{
                            roomsTemp.add(new Room(createRoom(), previousX + 1, previousY));
                            previousX += 1;
                            i++;
                        }
                    }
                }
                else{
                    if (moveNegative){
                        if (roomExists(roomsTemp, previousX , previousY - 1)){
                            previousY -= 1;
                        }
                        else{
                            roomsTemp.add(new Room(createRoom(), previousX, previousY - 1));
                            previousY -= 1;
                            i++;
                        }
                    }
                    else{
                        if (roomExists(roomsTemp, previousX , previousY + 1)){
                            previousY += 1;
                        }
                        else{
                            roomsTemp.add(new Room(createRoom(), previousX, previousY + 1));
                            previousY += 1;
                            i++;
                        }
                    }
                }
            }
        }

        return roomsTemp;
    }


    private static Integer[][] createRoom(){
        Integer[][] board = new Integer[COLUMNS][ROWS];
        ArrayList <Integer> options = new ArrayList<>();
        options.add(0);
        options.add(0);
        options.add(0);
        options.add(0);
        options.add(1);
        Random generator = new Random();

        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLUMNS; j++){
                if (j == 0 || j == COLUMNS - 1 || i == 0 || i == ROWS - 1){
                    board[j][i] = 1;
                }
                else {
                    board[j][i] = options.get(generator.nextInt(5));
                }
            }
        }

        return board;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        // prevent the player from disappearing off the board
        player.tick();

        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver
        // because Component implements the ImageObserver interface, and JPanel
        // extends from Component. So "this" Board instance, as a Component, can
        // react to imageUpdate() events triggered by g.drawImage()

        // draw our graphics.
        drawBackground(g, currentRoom);
        player.draw(g, this);

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // react to key down events
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // react to key up events
    }

    private void drawBackground(Graphics g, Room room) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                // only color every other tile
                if(room.board[col][row]==1){
                    g.setColor(new Color(0, 0, 0));
                    g.fillRect(
                            col * TILE_SIZE,
                            row * TILE_SIZE,
                            TILE_SIZE,
                            TILE_SIZE
                    );
                }else if(room.board[col][row]==2){
                    g.setColor(new Color(0, 200, 255));
                    g.fillRect(
                            col * TILE_SIZE,
                            row * TILE_SIZE,
                            TILE_SIZE,
                            TILE_SIZE
                    );
                }
                else if ((row + col) % 2 == 1) {
                    // draw a square tile at the current row/column position
                    g.setColor(new Color(214, 214, 214));
                    g.fillRect(
                            col * TILE_SIZE,
                            row * TILE_SIZE,
                            TILE_SIZE,
                            TILE_SIZE
                    );
                }

            }
        }
    }


}