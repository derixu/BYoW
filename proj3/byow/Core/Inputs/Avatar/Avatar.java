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
    TETile[][] worldArr;
    TERenderer ter;
    public Avatar(Long seedNum, World world, TERenderer ter) {
        seed = new Random(seedNum);
        worldArr = world.returnWorldArr();
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

        avatar.draw(x, y);
        StdDraw.show();
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
        if (worldArr[nextX][nextY] != Tileset.WALL) {
            worldArr[x][y] = Tileset.GRASS;
            worldArr[nextX][nextY] = avatar;
            x = nextX;
            y = nextY;
        }
    }
}
