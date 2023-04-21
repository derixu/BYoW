package byow.Core.Inputs;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class KeyboardInputs implements Inputs{
    private static final boolean PRINT_TYPED_KEYS = false;
    private int width;
    private int height;

    public KeyboardInputs(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void initializeUI() {
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

    public void promptSeed(String seed) {
        StdDraw.clear(Color.BLACK);
        Font fontMid = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontMid);
        StdDraw.text(width/2, height * 1/2, "Enter Seed (Press S to complete)");
        StdDraw.text(width/2, (height * 1/2) - 2, seed);

        StdDraw.show();
    }

    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (PRINT_TYPED_KEYS) {
                    System.out.print(c);
                }
                return c;
            }
        }
    }

    public boolean possibleNextInput() {
        return true;
    }
}
