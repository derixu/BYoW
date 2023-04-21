package byow.Core.Inputs;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class KeyboardInputs implements Inputs{
    private static final boolean PRINT_TYPED_KEYS = false;

    public KeyboardInputs() {
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
