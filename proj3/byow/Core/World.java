package byow.Core;

import byow.Core.Rooms.RectangleRoom;
import byow.Core.Rooms.Room;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class World {
    private TETile[][] worldArray;
    private ArrayList<Room> rooms = new ArrayList<>();

    private Random seed;
    public World(int seedNum, int width, int height) {

        //initialize World as empty
        worldArray = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                worldArray[x][y] = Tileset.NOTHING;
            }
        }

        //decide seed
        seed = new Random(seedNum);


        //parameterize the world (important part is setting numRooms to an integer, process is adaptable
        //int minRooms = 20;
        //int maxRooms = 25;
        //int numRooms = RandomUtils.uniform(seed, minRooms, maxRooms);
        int numRooms = width*height/100;

        //create rooms based on numRooms
        for (int n = 0; n <= numRooms; n++) {

            //parameterize the room and create it
            int roomWidth = RandomUtils.uniform(seed, 4,  10);
            int roomHeight = RandomUtils.uniform(seed, 4,  10);

            int x = RandomUtils.uniform(seed, 0,  width - roomWidth);
            int y = RandomUtils.uniform(seed, 0,  height - roomHeight);
            RectangleRoom room = new RectangleRoom(x, y, roomWidth, roomHeight);

            //if room overlaps with any previous rooms in the world, redo parameterization and initialization
            while (room.overlap(rooms)) {
                x = RandomUtils.uniform(seed, 0,  width - roomWidth);
                y = RandomUtils.uniform(seed, 0,  height - roomHeight);
                room = new RectangleRoom(x, y, roomWidth, roomHeight);
            }

            //add new room walls to world array with unique tile
            for (ArrayList<Integer> wallCoordinates : room.getWalls()) {
                worldArray[wallCoordinates.get(0)][wallCoordinates.get(1)] = Tileset.WALL;
            }

            //add new room floors to world array with unique tile
            for (ArrayList<Integer> floorCoordinates : room.getFloors()) {
                worldArray[floorCoordinates.get(0)][floorCoordinates.get(1)] = Tileset.GRASS;
            }

            //add room to set of rooms to prevent future overlaps
            rooms.add(room);
        }

        ArrayList<Room> mutableRooms = new ArrayList<>(rooms);
        for (int i = 0; i < rooms.size() - 1; i++) {
            double x = Double.valueOf(rooms.get(i).getWalls().get(0).get(0));
            double y = Double.valueOf(rooms.get(i).getWalls().get(0).get(1));

            Room closest = new RectangleRoom(0, 0, 0, 0);
            double distance = Double.MAX_VALUE;

            for (Room possible : mutableRooms) {
                double posX = Double.valueOf(possible.getWalls().get(0).get(0));
                double posY = Double.valueOf(possible.getWalls().get(0).get(1));
                double posDist = Math.sqrt(Math.pow(x - posX, 2) + Math.pow(y - posY, 2));

                if (posDist != 0 && posDist < distance) {
                    closest = possible;
                    distance = posDist;
                }
            }
            mutableRooms.remove(rooms.get(i));
            hallwayHelper(worldArray, rooms.get(i), closest);
        }
    }

    private void hallwayHelper(TETile[][] worldArr, Room r1, Room r2) {

        int x1 = r1.getFloors().get(RandomUtils.uniform(seed, 0, r1.getFloors().size())).get(0);
        int y1 = r1.getFloors().get(RandomUtils.uniform(seed, 0, r1.getFloors().size())).get(1);

        int x2 = r2.getFloors().get(RandomUtils.uniform(seed, 0, r2.getFloors().size())).get(0);
        int y2 = r2.getFloors().get(RandomUtils.uniform(seed, 0, r2.getFloors().size())).get(1);


        while (x1 != x2) {
            worldArr[x1][y1] = Tileset.GRASS;
            if (worldArr[x1][y1 + 1] == Tileset.NOTHING) {
                worldArr[x1][y1 + 1] = Tileset.WALL;
            }
            if (worldArr[x1][y1 - 1] == Tileset.NOTHING) {
                worldArr[x1][y1 - 1] = Tileset.WALL;
            }
            if (x1 < x2) {
                x1++;
            } else {
                x1--;
            }
        }
        if (worldArr[x1][y1 + 1] == Tileset.NOTHING) {
            worldArr[x1][y1 + 1] = Tileset.WALL;
        }
        if (worldArr[x1][y1 - 1] == Tileset.NOTHING) {
            worldArr[x1][y1 - 1] = Tileset.WALL;
        }

        while (y1 != y2) {
            worldArr[x1][y1] = Tileset.GRASS;
            if (worldArr[x1 + 1][y1] == Tileset.NOTHING) {
                worldArr[x1 + 1][y1] = Tileset.WALL;
            }
            if (worldArr[x1 - 1][y1] == Tileset.NOTHING) {
                worldArr[x1 - 1][y1] = Tileset.WALL;
            }
            if (y1 < y2) {
                y1++;
            } else {
                y1--;
            }
        }
        if (worldArr[x1 + 1][y1] == Tileset.NOTHING) {
            worldArr[x1 + 1][y1] = Tileset.WALL;
        }
        if (worldArr[x1 - 1][y1] == Tileset.NOTHING) {
            worldArr[x1 - 1][y1] = Tileset.WALL;
        }
    }

    public TETile[][] returnWorldArr() {
        return worldArray;
    }

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(20, 20);

        World world = new World(123, 20, 20);
        TETile[][] worldArr = world.returnWorldArr();

        ter.renderFrame(worldArr);

    }
}
