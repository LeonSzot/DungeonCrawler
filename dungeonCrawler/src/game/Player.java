package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

import static game.Board.*;

public class Player {

    // image that represents the player's position on the room
    private BufferedImage image;
    // current position of the player on the room grid
    private Point pos;

    public Board board;

    public Room room;

    public Player(Board boardIn) {
        // load the assets
        loadImage();

        board = boardIn;
        room = board.currentRoom;

        // initialize the state
        pos = randomizePos();

    }

    private Point randomizePos(){
        Random generator = new Random();
        int x = generator.nextInt(COLUMNS);
        int y = generator.nextInt(ROWS);

        while (room.board[x][y].value != 0){
            x = generator.nextInt(COLUMNS);
            y = generator.nextInt(ROWS);
        }
        return new Point(x, y);
    }
    private void loadImage() {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(new File("dungeonCrawler/images/player.png"));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        // with the Point class, note that pos.getX() returns a double, but 
        // pos.x reliably returns an int. https://stackoverflow.com/a/30220114/4655368
        // this is also where we translate room grid position into a canvas pixel
        // position by multiplying by the tile size.
        g.drawImage(
                image,
                pos.x * TILE_SIZE,
                pos.y * TILE_SIZE,
                observer
        );
    }

    public void keyPressed(KeyEvent e) {
        // every keyroom get has a certain code. get the value of that code from the
        // keyroom event so that we can compare it to KeyEvent constants
        int key = e.getKeyCode();

        // depending on which arrow key was pressed, we're going to move the player by
        // one whole tile for this input
        if (key == KeyEvent.VK_UP) {
            if(pos.y > 0 ) {
                if (room.board[pos.x][pos.y - 1].value != 1) {
                    pos.translate(0, -1);
                }
            }
            else if(room.board[pos.x][pos.y].value == 2 && pos.y == 0){
                room = board.getRoom(room.x, room.y - 1);
                board.currentRoom = room;
                pos.y = ROWS - 1;
            }
        }
        if (key == KeyEvent.VK_RIGHT) {
            if (pos.x < COLUMNS - 1) {
                if (room.board[pos.x + 1][pos.y].value != 1) {
                    pos.translate(1, 0);
                }
            }
            else if(room.board[pos.x][pos.y].value == 2 && pos.x == COLUMNS - 1) {
                room = board.getRoom(room.x + 1, room.y);
                board.currentRoom = room;
                pos.x = 0;
            }
        }
        if (key == KeyEvent.VK_DOWN) {
            if (pos.y < ROWS - 1) {
                if (room.board[pos.x][pos.y + 1].value != 1) {
                    pos.translate(0, +1);
                }
            }else if(room.board[pos.x][pos.y].value == 2 && pos.y == ROWS - 1){
                room = board.getRoom(room.x, room.y + 1);
                board.currentRoom = room;
                pos.y = 0;
            }
        }
        if (key == KeyEvent.VK_LEFT) {
            if(pos.x > 0) {
                if (room.board[pos.x - 1][pos.y].value != 1) {
                    pos.translate(-1, 0);
                }
            }else if(room.board[pos.x][pos.y].value == 2 && pos.x == 0){
                    room = board.getRoom(room.x - 1, room.y);
                    board.currentRoom = room;
                    pos.x = COLUMNS - 1;
            }
        }
    }

    public void tick() {
        // this gets called once every tick, before the repainting process happens.
        // so we can do anything needed in here to update the state of the player.

        // prevent the player from moving off the edge of the room sideways
        if (pos.x < 0) {
            pos.x = 0;
        } else if (pos.x >= COLUMNS) {
            pos.x = COLUMNS - 1;
        }
        // prevent the player from moving off the edge of the room vertically
        if (pos.y < 0) {
            pos.y = 0;
        } else if (pos.y >= ROWS) {
            pos.y = ROWS - 1;
        }
    }


    public void mousePressed(MouseEvent e) {
        int mouseBtn = e.getButton();

        if (mouseBtn == MouseEvent.BUTTON1){
            int x = (int) Math.floor((e.getX() - 7) / TILE_SIZE);
            int y = (int) Math.floor((e.getY() - 30) / TILE_SIZE);

            System.out.println(x);
            System.out.println(y);
        }
    }
}