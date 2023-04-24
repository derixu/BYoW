package byow.Core.Inputs;

import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserInterface {
    private int height;
    private int width;

    private String pointerTile = "";

    public UserInterface(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void startScreen() {

        //set up canvas
        StdDraw.setCanvasSize(width*17, height*17);
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.enableDoubleBuffering();

        //font options
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        Font fontSmall = new Font("Monaco", Font.BOLD, 15);

        //draw title
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontBig);
        StdDraw.text(width/2, height * 3/4, "CS61B: THE GAME");

        //draw menu options
        StdDraw.setFont(fontSmall);
        StdDraw.text(width/2, height * 1/4, "NEW GAME (N)");
        StdDraw.text(width/2, (height * 1/4) - 1, "LOAD GAME (L)");
        StdDraw.text(width/2, (height * 1/4) - 2, "QUIT GAME (Q)");

        StdDraw.show();
    }

    public void seedPrompt(String seed) {
        StdDraw.clear(Color.BLACK);

        //new font option
        Font fontMid = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontMid);

        //draw seed prompt and the seed given so far
        StdDraw.text(width / 2, height * 1 / 2, "Enter Seed (Press S to complete)");
        StdDraw.text(width / 2, (height * 1 / 2) - 2, seed);

        StdDraw.show();
    }

    public void setPointerTile(String tileType) {
        pointerTile = tileType;
    }

    public void setHealth(int health) {
    }

    public void drawHUD() {
        Font fontSmall = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontSmall);
        StdDraw.textLeft(2, height, "Tile: " + pointerTile);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        StdDraw.textLeft(10, height, "Date and time:" + now.format(dtf));

        StdDraw.show();
    }
}
