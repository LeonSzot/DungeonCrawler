package game;

import java.awt.*;
import java.awt.event.KeyEvent;
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

    public Room room;

    public Player(Room roomIn) {
        // load the assets
        loadImage();

        room = roomIn;

        // initialize the state
        pos = randomizePos();
        

        
        
    }

    private Point randomizePos(){
        Random generator = new Random();
        int x = generator.nextInt(COLUMNS);
        int y = generator.nextInt(ROWS);

        while (room.board[x][y] != 0){
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
                if (room.board[pos.x][pos.y - 1] == 0) {
                    pos.translate(0, -1);
                }
            }
        }
        if (key == KeyEvent.VK_RIGHT) {
            if (pos.x < COLUMNS - 1) {
                if (room.board[pos.x + 1][pos.y] == 0) {
                    pos.translate(1, 0);
                }
            }
        }
        if (key == KeyEvent.VK_DOWN) {
            if (pos.y < ROWS - 1) {
                if (room.board[pos.x][pos.y + 1] == 0) {
                    pos.translate(0, +1);
                }
            }
        }
        if (key == KeyEvent.VK_LEFT) {
            if(pos.x > 0) {
                if (room.board[pos.x - 1][pos.y] == 0) {
                    pos.translate(-1, 0);
                }
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



    public Point getPos() {
        return pos;
    }

}