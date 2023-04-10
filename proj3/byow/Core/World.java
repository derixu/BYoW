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
    public HashSet<Room> Rooms = new HashSet<>();
    public World(String input, int width, int height) {

        //initialize World as empty
        WorldArr = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                WorldArr[x][y] = Tileset.NOTHING;
            }
        }

        //decide seed
        // TODO: implement parsing through input to decide seed
        Random seed = new Random(123);


        //parameterize the world (important part is setting numRooms to an integer, process is adaptable
        int minRooms = 15;
        int maxRooms = 20;
        int numRooms = RandomUtils.uniform(seed, minRooms, maxRooms);

        //create rooms based on numRooms
        for (int n = 0; n <= numRooms; n++) {

            //parameterize the room and create it
            int roomWidth = RandomUtils.uniform(seed,3,  10);
            int roomHeight = RandomUtils.uniform(seed,3,  10);

            int x = RandomUtils.uniform(seed,0,  width-roomWidth);
            int y = RandomUtils.uniform(seed,0,  height-roomHeight);
            RectangleRoom room = new RectangleRoom(x, y, roomWidth, roomHeight);

            //if room overlaps with any previous rooms in the world, redo parameterization and initialization
            while (room.Overlap(Rooms)) {
                x = RandomUtils.uniform(seed,0,  width-roomWidth);
                y = RandomUtils.uniform(seed,0,  height-roomHeight);
                room = new RectangleRoom(x, y, roomWidth, roomHeight);
            }

            //add new room walls to world array with unique tile
            for (ArrayList<Integer> wallCoordinates : room.Walls) {
                WorldArr[wallCoordinates.get(0)][wallCoordinates.get(1)] = Tileset.WALL;
            }

            //add new room floors to world array with unique tile
            for (ArrayList<Integer> floorCoordinates : room.Floors) {
                WorldArr[floorCoordinates.get(0)][floorCoordinates.get(1)] = Tileset.GRASS;
            }

            //add room to set of rooms to prevent future overlaps
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
