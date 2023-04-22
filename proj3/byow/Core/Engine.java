package byow.Core;

import byow.Core.Inputs.Avatar.Avatar;
import byow.Core.Inputs.Inputs;
import byow.Core.Inputs.KeyboardInputs;
import byow.Core.Inputs.StringInputs;
import byow.Core.Inputs.UserInterface;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Engine {
    TERenderer ter = new TERenderer();
    UserInterface UI;
    File saveFile  = new File("saveFile.txt");

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {

        Inputs inputDev = new KeyboardInputs();

        //necessary variables to create the saveFile
        String seedStr;
        String[] loadScript = new String[3];
        boolean loaded = false;

        //create UI
        UI = new UserInterface(WIDTH, HEIGHT);
        UI.startScreen();

        //loop until one of the conditions breaks the loop
        while (true) {
            //get the next input
            char c = inputDev.getNextKey();

            //if the input is N, switch to the UI for seed prompting and get the seed (saved in seedStr)
            if (Character.toTitleCase(c) == 'N') {
                UI.seedPrompt("");
                seedStr = solicitSeed(inputDev, true);
                break;
            }

            //if the input is L, load previous save if it exists
            if (Character.toTitleCase(c) == 'L') {
                //check that the saveFile returns a non-empty String (any error is caught by readFile and returned as "")
                if (!readFile(saveFile).equals("")) {
                    //load script has three elements: 1 - seed, 2 - x coordinate of avatar, 3 - y coordinate of avatar
                    loadScript = readFile(saveFile).split(",");
                    seedStr = loadScript[0];

                    //set loaded = true so that we can update the avatar's location later
                    loaded = true;
                    break;
                }
            }
            //quit method
            if (Character.toTitleCase(c) == 'Q') {
                System.exit(0);
            }
        }

        //convert string seed to Random
        Random seed = new Random(Long.valueOf(seedStr));
        World world = new World(seed, WIDTH, HEIGHT);
        ter.renderFrame(world.returnWorldArr());

        //find random coordinates for avatar if no load, if load set coordinates to saved coordinates
        ArrayList<Integer> coordinates = world.randomRoom().randomFloor();
        int x = coordinates.get(0);
        int y = coordinates.get(1);
        if (loaded) {
            x = Integer.valueOf(loadScript[1]);
            y = Integer.valueOf(loadScript[2]);
        }

        //add avatar to the world
        Avatar avi = new Avatar(x, y, world);
        ter.renderFrame(world.returnWorldArr());

        //checks if previous key pressed was a colon
        boolean colonPress = false;

        //while we don't quit with :Q, game keeps running
        while(true) {
            char c = inputDev.getNextKey();

            //use helper to move avatar accordingly
            solicitMovements(avi, c);
            ter.renderFrame(world.returnWorldArr());

            //if we get a colon we set colonPress to true and check the next input
            if (Character.toTitleCase(c) == ':') {
                colonPress = true;
                continue;
            }

            //if previous input was colon, and this input is Q, save the current seed and avatar location and quit
            if (colonPress && Character.toTitleCase(c) == 'Q') {
                String currX = Integer.toString(avi.getCoordinates()[0]);
                String currY = Integer.toString(avi.getCoordinates()[1]);
                String inputTracker = seedStr + "," + currX + "," + currY;
                saveFile = writeSaveFile(inputTracker);
                System.exit(0);
            }
            //reset colonPress after every loop except if colon has just been pressed (continue skips this)
            colonPress = false;
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    //Same as previous essentially, except the final while loop does not go on infinitely (goes on until string ends)
    //and there is no UI or graphical element so no interactivity
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        Inputs inputDev = new StringInputs(input);

        String[] loadScript = new String[3];
        boolean loaded = false;
        String seedStr;

        //retrieve seed from user input
        while (true) {
            char c = inputDev.getNextKey();
            if (Character.toTitleCase(c) == 'N') {
                seedStr = solicitSeed(inputDev, false);
                break;
            }
            if (Character.toTitleCase(c) == 'L') {
                if (!readFile(saveFile).equals("")) {
                    loadScript = readFile(saveFile).split(",");
                    seedStr = loadScript[0];
                    loaded = true;
                    break;
                }
            }
        }
        Random seed = new Random(Long.valueOf(seedStr));
        World world = new World(seed, WIDTH, HEIGHT);

        ArrayList<Integer> coordinates = world.randomRoom().randomFloor();
        int x = coordinates.get(0);
        int y = coordinates.get(1);
        if (loaded) {
            x = Integer.valueOf(loadScript[1]);
            y = Integer.valueOf(loadScript[2]);
        }

        Avatar avi = new Avatar(x, y, world);

        boolean colonPress = false;

        while(inputDev.possibleNextInput()) {
            char c = inputDev.getNextKey();
            solicitMovements(avi, c);

            if (Character.toTitleCase(c) == ':') {
                colonPress = true;
                continue;
            }
            if (colonPress && Character.toTitleCase(c) == 'Q') {
                String currX = Integer.toString(avi.getCoordinates()[0]);
                String currY = Integer.toString(avi.getCoordinates()[1]);
                String inputTracker = seedStr + "," + currX + "," + currY;
                saveFile = writeSaveFile(inputTracker);
                System.exit(0);
            }
            colonPress = false;
        }
        return world.returnWorldArr();
    }

    private String solicitSeed(Inputs inputDev, boolean interactive) {
        String seed = "";
        while (true) {
            char c = inputDev.getNextKey();
            if (Character.isDigit(c)) {
                seed += c;
                if (interactive) {
                    UI.seedPrompt(seed);
                }
            }
            if (Character.toTitleCase(c) == 'S' && !seed.isEmpty()) {
                return seed;
            }
        }
    }

    private void solicitMovements(Avatar avi, char c) {
        if (Character.toTitleCase(c) == 'W') {
            avi.move("up");
        }
        if (Character.toTitleCase(c) == 'A') {
            avi.move("left");
        }
        if (Character.toTitleCase(c) == 'S') {
            avi.move("down");
        }
        if (Character.toTitleCase(c) == 'D') {
            avi.move("right");
        }
    }

    private File writeSaveFile(String input) {
        try {
            FileWriter myWriter = new FileWriter("saveFile.txt");

            saveFile.createNewFile();
            myWriter.write(input);
            myWriter.close();
            return saveFile;

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    private String readFile(File saveFile) {
        try {
            Scanner myReader = new Scanner(saveFile);
            String data = "";
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }
            myReader.close();
            return data;
        } catch (FileNotFoundException e) {
            return "";
        }
    }

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        Engine engine = new Engine();
        engine.interactWithKeyboard();
    }

}
