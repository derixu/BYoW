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
    public Avatar(Long seedNum, World world, TERenderer ter) {
        seed = new Random(seedNum);
        this.world = world;
        this.ter = ter;

        //find starting room
        ArrayList<Room> rooms = world.returnRooms();
        int roomIndex = RandomUtils.uniform(seed, 0, rooms.size());
        Room startRoom  = rooms.get(roomIndex);

        //find starting tile
        int tileIndex = RandomUtils.uniform(seed, 0, startRoom.getFloors().size());
        ArrayList<Integer> coordinates = startRoom.getFloors().get(tileIndex);

        x = coordinates.get(0);
        y = coordinates.get(1);

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
}
