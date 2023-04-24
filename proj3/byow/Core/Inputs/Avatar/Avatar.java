package byow.Core.Inputs.Avatar;

import byow.Core.World;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

public class Avatar {
    int x;
    int y;
    TETile avatar = Tileset.AVATAR;
    World world;
    TERenderer ter;
    public Avatar(int x, int y, World world) {
        //initialize coordinates and world
        this.x = x;
        this.y = y;
        this.world = world;

        //place avatar in world
        this.world.alterTiles(x, y, avatar);
    }

    public void move(String direction) {
        int nextX = x;
        int nextY = y;

        //check for direction and depending on case alter the next position
        switch (direction) {
            case "up" -> nextY++;
            case "down" -> nextY--;
            case "right" -> nextX++;
            case "left" -> nextX--;
        }
        //if the next tile is not a wall, we can move and update the current position
        if (world.returnWorldArr()[nextX][nextY] != Tileset.WALL) {
            world.alterTiles(x, y, Tileset.GRASS);
            world.alterTiles(nextX, nextY, avatar);
            x = nextX;
            y = nextY;
        }
    }

    //possibly use to teleport if needed as a helper for any functions
    public void moveDirect(int newX, int newY) {
        world.alterTiles(x, y, Tileset.GRASS);
        world.alterTiles(newX, newY, avatar);
    }

    public int[] getCoordinates() {
        return new int[]{x, y};
    }

}
