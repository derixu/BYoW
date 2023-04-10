package byow.Core;

import byow.Core.Rooms.RectangleRoom;
import byow.Core.Rooms.Room;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import byow.Core.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class World {
    public TETile[][] WorldArr;
    public HashSet<Room> Rooms = new HashSet<Room>();
    public World(String input, int width, int height) {
        WorldArr = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                WorldArr[x][y] = Tileset.NOTHING;
            }
        }
        int minRooms = 15;
        int maxRooms = 20;

        Random seed = new Random(123);

        int numRooms = RandomUtils.uniform(seed, minRooms, maxRooms);

        for (int n = 0; n <= numRooms; n++) {
            int roomWidth = RandomUtils.uniform(seed,3,  20);
            int roomHeight = RandomUtils.uniform(seed,3,  20);

            int x = RandomUtils.uniform(seed,0,  width-roomWidth);
            int y = RandomUtils.uniform(seed,0,  height-roomHeight);
            RectangleRoom room = new RectangleRoom(x, y, roomWidth, roomHeight);

            while (room.Overlap(Rooms)) {
                x = RandomUtils.uniform(seed,0,  width-roomWidth);
                y = RandomUtils.uniform(seed,0,  height-roomHeight);
                room = new RectangleRoom(x, y, roomWidth, roomHeight);
            }

            for (ArrayList<Integer> wallCoordinates : room.Walls) {
                WorldArr[wallCoordinates.get(0)][wallCoordinates.get(1)] = Tileset.WALL;
            }

            for (ArrayList<Integer> floorCoordinates : room.Floors) {
                WorldArr[floorCoordinates.get(0)][floorCoordinates.get(1)] = Tileset.GRASS;
            }

            Rooms.add(room);

        }
    }

    public TETile[][] ReturnWorldArr() {
        return WorldArr;
    }

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(100, 100);

        World world = new World("123", 100, 100);
        TETile[][] worldArr = world.ReturnWorldArr();

        ter.renderFrame(worldArr);

    }
}
