package byow.Core.Inputs;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class UserInterface {
    private int height;
    private int width;
    public UserInterface(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void startScreen() {
        StdDraw.setCanvasSize(width*17, height*17);
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.enableDoubleBuffering();

        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        Font fontSmall = new Font("Monaco", Font.BOLD, 15);

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontBig);
        StdDraw.text(width/2, height * 3/4, "CS61B: THE GAME");

        StdDraw.setFont(fontSmall);
        StdDraw.text(width/2, height * 1/4, "NEW GAME (N)");
        StdDraw.text(width/2, (height * 1/4) - 1, "LOAD GAME (L)");
        StdDraw.text(width/2, (height * 1/4) - 2, "QUIT GAME (Q)");

        StdDraw.show();
    }

    public void seedPrompt(String seed) {
        StdDraw.clear(Color.BLACK);
        Font fontMid = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontMid);
        StdDraw.text(width/2, height * 1/2, "Enter Seed (Press S to complete)");
        StdDraw.text(width/2, (height * 1/2) - 2, seed);

        StdDraw.show();
    }
}
