package byow.Core.Inputs.Avatar;

import byow.Core.RandomUtils;

import byow.Core.Rooms.Room;
import byow.Core.World;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Random;

public class Avatar {
    int x;
    int y;
    Random seed;
    TETile avatar = Tileset.AVATAR;
    World world;
    TERenderer ter;
    public Avatar(int x, int y, World world, TERenderer ter) {
        this.x = x;
        this.y = y;
        this.world = world;
        this.ter = ter;

        this.world.alterTiles(x, y, avatar);
        ter.renderFrame(world.returnWorldArr());
    }

    public void move(String direction) {
        int nextX = x;
        int nextY = y;

        switch (direction) {
            case "up" -> nextY++;
            case "down" -> nextY--;
            case "right" -> nextX++;
            case "left" -> nextX--;
        }
        if (world.returnWorldArr()[nextX][nextY] != Tileset.WALL) {
            world.alterTiles(x, y, Tileset.GRASS);
            world.alterTiles(nextX, nextY, avatar);
            x = nextX;
            y = nextY;
        }
    }

    public void moveDirect(int newX, int newY) {
        world.alterTiles(x, y, Tileset.GRASS);
        world.alterTiles(newX, newY, avatar);
    }

    public int[] getCoordinates() {
        return new int[]{x, y};
    }

}
