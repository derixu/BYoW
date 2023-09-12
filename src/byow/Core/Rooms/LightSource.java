package byow.Core.Rooms;

import byow.Core.Inputs.Avatar.Avatar;
import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import net.sf.saxon.expr.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


public class LightSource {
    private World world;
    private ArrayList<Integer> light;
    private HashMap<Integer[], TETile> affected = new HashMap();
    boolean on = true;
    private int range = 4;
    private int dr;
    private int dg;
    private int db;
    private int xCoord;
    private int yCoord;

    public LightSource(Room room, World world, Color startingColor) {
        ArrayList<Integer> tile = room.randomFloor();
        light = tile;
        this.world = world;

        this.xCoord = tile.get(0);
        this.yCoord = tile.get(1);

        world.alterTiles(xCoord, yCoord, Tileset.LIGHT);

        TETile centerTile = TETile.changeBackground(Tileset.FLOOR, startingColor);
        dr = -startingColor.getRed() / range;
        dg = -startingColor.getGreen() / range;
        db = -startingColor.getBlue() / range;

        int leftX = xCoord;
        int rightX = xCoord;
        int botY = yCoord;
        int topY = yCoord;

        for (int dist = 1; dist < range; dist++) {
            TETile gradient = TETile.gradientVariant(centerTile, dr * dist, dg * dist, db * dist);
            leftX--;
            rightX++;
            botY--;
            topY++;

            for (int x = leftX; x <= rightX; x++) {
                if (world.getTile(x, botY) == Tileset.FLOOR && room.inRoom(x, botY)) {
                    affected.put(new Integer[] {x, botY}, gradient);
                    world.alterTiles(x, botY, gradient);
                }
                if (world.getTile(x, topY) == Tileset.FLOOR && room.inRoom(x, topY)) {
                    affected.put(new Integer[] {x, topY}, gradient);
                    world.alterTiles(x, topY, gradient);
                }
            }
            for (int y = botY; y <= topY; y++) {
                if (world.getTile(leftX, y) == Tileset.FLOOR && room.inRoom(leftX, y)) {
                    affected.put(new Integer[] {leftX, y}, gradient);
                    world.alterTiles(leftX, y, gradient);
                }
                if (world.getTile(rightX, y) == Tileset.FLOOR && room.inRoom(rightX, y)) {
                    affected.put(new Integer[] {rightX, y}, gradient);
                    world.alterTiles(rightX, y, gradient);
                }
            }
        }
    }

    public void toggleLight(Avatar avi) {
        if (on) {
            world.alterTiles(light.get(0), light.get(1), Tileset.LOGS);
            for (Integer[] coordinate : affected.keySet()) {
                if (coordinate[0] == avi.getCoordinates()[0] && coordinate[1] == avi.getCoordinates()[1]) {
                    avi.updateHiddenFloor(Tileset.FLOOR);
                    avi.backgroundHelper();
                    avi.updateAvatar();
                    continue;
                }
                world.alterTiles(coordinate[0], coordinate[1], Tileset.FLOOR);
            }
            on = false;
        }
        else {
            world.alterTiles(light.get(0), light.get(1), Tileset.LIGHT);
            for (Integer[] coordinate : affected.keySet()) {
                if (coordinate[0] == avi.getCoordinates()[0] && coordinate[1] == avi.getCoordinates()[1]) {
                    avi.updateHiddenFloor(affected.get(coordinate));
                    avi.backgroundHelper();
                    avi.updateAvatar();
                    continue;
                }
                world.alterTiles(coordinate[0], coordinate[1], affected.get(coordinate));
            }
            on = true;
        }
    }
    public int returnX() {
        return xCoord;
    }
    public int returnY() {
        return yCoord;
    }

}
