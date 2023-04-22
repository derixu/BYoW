package byow.Core;

import byow.Core.Inputs.Avatar.Avatar;
import byow.Core.Inputs.Inputs;
import byow.Core.Inputs.KeyboardInputs;
import byow.Core.Inputs.StringInputs;
import byow.Core.Inputs.UserInterface;
import byow.Core.Rooms.Room;
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
        String seedStr;
        String inputTracker;
        String[] loadDimensions = new String[3];
        boolean loaded = false;

        Inputs inputDev = new KeyboardInputs();
        UI = new UserInterface(WIDTH, HEIGHT);

        UI.startScreen();
        while (true) {
            char c = inputDev.getNextKey();
            if (Character.toTitleCase(c) == 'N') {
                UI.seedPrompt("");
                seedStr = solicitSeed(inputDev, true);
                break;
            }
            if (Character.toTitleCase(c) == 'L') {
                if (!readFile(saveFile).equals("")) {
                    String savedInput = readFile(saveFile);
                    loadDimensions = savedInput.split(",");
                    seedStr = loadDimensions[0];
                    loaded = true;
                    break;
                }
            }
            if (Character.toTitleCase(c) == 'Q') {
                System.exit(0);
            }
        }

        long seedNum = Long.valueOf(seedStr);

        Random seed = new Random(seedNum);

        World world = new World(seed, WIDTH, HEIGHT);
        ter.renderFrame(world.returnWorldArr());

        ArrayList<Integer> coordinates = world.randomRoom().randomFloor();

        int x = coordinates.get(0);
        int y = coordinates.get(1);
        if (loaded) {
            x = Integer.valueOf(loadDimensions[1]);
            y = Integer.valueOf(loadDimensions[2]);
        }

        Avatar avi = new Avatar(x, y, world, ter);

        boolean colonPress = false;

        while(true) {
            char c = inputDev.getNextKey();
            solicitMovements(avi, c);
            ter.renderFrame(world.returnWorldArr());

            if (Character.toTitleCase(c) == ':') {
                colonPress = true;
                continue;
            }
            if (colonPress && Character.toTitleCase(c) == 'Q') {
                String currX = Integer.toString(avi.getCoordinates()[0]);
                String currY = Integer.toString(avi.getCoordinates()[1]);
                inputTracker = seedStr + "," + currX + "," + currY;
                saveFile = writeSaveFile(inputTracker);
                System.exit(0);
            }
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
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        String seedStr = "";
        String inputTracker = "";
        Inputs inputDev = new StringInputs(input);

        //retrieve seed from user input
        while (true) {
            char c = inputDev.getNextKey();
            if (Character.toTitleCase(c) == 'N') {
                seedStr = solicitSeed(inputDev, false);

                //update input tracker after seed is solicited
                inputTracker += seedStr;
                inputTracker += 'S';
                break;
            }
            if (Character.isDigit(c)) {
                seedStr += c;
            }
        }
        //initialize world based on seed
        Long seedNum = Long.valueOf(seedStr);
        Random seed = new Random(seedNum);

        World world = new World(seed, WIDTH, HEIGHT);

        ArrayList<Integer> coordinates = world.randomRoom().randomFloor();
        int x = coordinates.get(0);
        int y = coordinates.get(1);
        Avatar avi = new Avatar(x, y, world, ter);

        boolean colonPress = false;
        while(inputDev.possibleNextInput()) {
            char c = inputDev.getNextKey();
            solicitMovements(avi, c);

            if (Character.toTitleCase(c) == ':') {
                colonPress = true;
                continue;
            }
            if (colonPress && Character.toTitleCase(c) == 'Q') {
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

    private void createRandomAvatar() {

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
