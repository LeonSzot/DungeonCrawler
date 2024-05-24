package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;

public class Board extends JPanel implements ActionListener, KeyListener, MouseListener{

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
                getNode(0, (int) Math.floor(ROWS / 2), room).value = 2;
                getNode(0, (int) Math.floor(ROWS / 2) + 1, room).value = 2;
                getNode(0, (int) Math.floor(ROWS / 2) - 1, room).value = 2;
            }
            if (roomExists(roomsTemp, room.x + 1, room.y)){
                getNode(COLUMNS - 1, (int) Math.floor(ROWS / 2), room).value = 2;
                getNode(COLUMNS - 1, (int) Math.floor(ROWS / 2) + 1, room).value = 2;
                getNode(COLUMNS - 1, (int) Math.floor(ROWS / 2) - 1, room).value = 2;
            }
            if (roomExists(roomsTemp, room.x, room.y + 1)){
                getNode((int) Math.floor(COLUMNS / 2), ROWS - 1, room).value = 2;
                getNode((int) Math.floor(COLUMNS / 2) + 1, ROWS - 1, room).value = 2;
                getNode((int) Math.floor(COLUMNS / 2) - 1, ROWS - 1, room).value = 2;
            }
            if (roomExists(roomsTemp, room.x, room.y - 1)){
                getNode((int) Math.floor(COLUMNS / 2), 0, room).value = 2;
                getNode((int) Math.floor(COLUMNS / 2) + 1, 0, room).value = 2;
                getNode((int) Math.floor(COLUMNS / 2) - 1, 0, room).value = 2;
            }
        }
    }

    public Node getNode(int x, int y, Room room){
        return room.board[x][y];
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


    private static Node[][] createRoom(){
        Node[][] board = new Node[COLUMNS][ROWS];
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
                    board[j][i] = new Node(j, i, 1);
                }
                else {
                    board[j][i] = new Node(j, i, options.get(generator.nextInt(5)));
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
                if(room.board[col][row].value ==1){
                    g.setColor(new Color(0, 0, 0));
                    g.fillRect(
                            col * TILE_SIZE,
                            row * TILE_SIZE,
                            TILE_SIZE,
                            TILE_SIZE
                    );
                }else if(room.board[col][row].value ==2){
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


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private int calculateDistance(Node node1, Node node2){
        int xDistance = Math.abs(node1.x - node2.x);
        int yDistance = Math.abs(node1.y - node2.y);
        int remaining = Math.abs(xDistance - yDistance);
        return remaining;
    }

    private ArrayList<Node> getPath(Node end){
        ArrayList<Node> path = new ArrayList<>();
        path.add(end);
        Node current = end;

        while (current.cameFrom != null){
            path.add(current.cameFrom);
            current = current.cameFrom;
        }

        Collections.reverse(path);
        return path;
    }


    public ArrayList<Node> pathfinder(Room room, int playerX, int playerY, int mouseX, int mouseY){
        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();

        // Create the start node and assign g, h, and f value
        Node startNode = getNode(playerX, playerY, room);
        startNode.g = 0;
        startNode.h = calculateDistance(startNode, getNode(mouseX, mouseY, room));
        startNode.f = startNode.g + startNode.h;

        // Append start node to the open list
        open.add(startNode);

        while (!open.isEmpty()){
            for (Node node1 : open) {
                if (!node1.start && !node1.end) {
                    node1.inOpen = true;
                }
            }

            for (Node node2 : closed){
                if (!node2.start && !node2.end){
                    node2.inClosed = true;
                }

            }

             // Get node with the lowest f score in the open list
            Node current = open.get(0);
            for (int i = 0; i < open.size(); i++){
                Node node = open.get(i);
                if (node.f < current.f){
                    current = node;
                }
            }

            // Remove current from open list
            open.remove(current);

            // Add current to closed
            closed.add(current);

            // Check if current is the end node
            if (current.end){
                return getPath(current);
            }

            // TODO
            // convert to java
            // Get neighbours list of the current node
            neighbours = get_neighbours(map, current, closed);

            // For each neighbour of the current node
            for (neighbour in neighbours){
                tentative_g = current.g + calc_dist(current, neighbour);
                if (tentative_g < neighbour.g){
                    neighbour.came_from = current
                    neighbour.g = tentative_g
                    neighbour.h = calc_dist(neighbour, map.get_end())
                    neighbour.f = neighbour.g + neighbour.h

                    if (neighbour not in open){
                        open.append(neighbour);
                    }

                }

            }


        }

        return null;
    }
}