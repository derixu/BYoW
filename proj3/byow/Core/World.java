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
    public World(long seedNum, int width, int height) {

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
        int minRooms = 5;
        int maxRooms = 25;
        int numRooms = RandomUtils.uniform(seed, minRooms, maxRooms);

        //create rooms based on numRooms
        for (int n = 0; n <= numRooms; n++) {

            //parameterize the room and create it
            int roomWidth = RandomUtils.uniform(seed, 4,  12);
            int roomHeight = RandomUtils.uniform(seed, 4,  12);

            int x = RandomUtils.uniform(seed, 0,  width - roomWidth);
            int y = RandomUtils.uniform(seed, 0,  height - roomHeight);
            RectangleRoom room = new RectangleRoom(x, y, roomWidth, roomHeight);

            //if room overlaps with any previous rooms in the world, redo parameterization and initialization
            int tries = 0;
            while (room.overlap(rooms)) {
                if (tries > 20) {
                    break;
                }
                x = RandomUtils.uniform(seed, 0,  width - roomWidth);
                y = RandomUtils.uniform(seed, 0,  height - roomHeight);
                room = new RectangleRoom(x, y, roomWidth, roomHeight);
                tries++;
            }

            if (tries > 20) {
                continue;
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

        //create a copy of the rooms list so that we can mutate it without risks
        ArrayList<Room> mutableRooms = new ArrayList<>(rooms);

        //iterate through rooms and connect them with their closest room that has not been iterated on yet
        //this algorithm could potentially use improvement to prevent excessive overlaps
        //also horrible runtime in theory
        for (int i = 0; i < rooms.size() - 1; i++) {
            double x = Double.valueOf(rooms.get(i).getWalls().get(0).get(0));
            double y = Double.valueOf(rooms.get(i).getWalls().get(0).get(1));

            //initialize closest to an arbitrary room with max distance
            Room closest = new RectangleRoom(0, 0, 0, 0);
            double distance = Double.MAX_VALUE;

            for (Room possible : mutableRooms) {
                double posX = Double.valueOf(possible.getWalls().get(0).get(0));
                double posY = Double.valueOf(possible.getWalls().get(0).get(1));
                //calculate Euclidean distance
                double posDist = Math.sqrt(Math.pow(x - posX, 2) + Math.pow(y - posY, 2));

                //replace if closer than current closest
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

        //get coordinates of each room (random floor coordinate)
        int x1 = r1.getFloors().get(RandomUtils.uniform(seed, 0, r1.getFloors().size())).get(0);
        int y1 = r1.getFloors().get(RandomUtils.uniform(seed, 0, r1.getFloors().size())).get(1);

        int x2 = r2.getFloors().get(RandomUtils.uniform(seed, 0, r2.getFloors().size())).get(0);
        int y2 = r2.getFloors().get(RandomUtils.uniform(seed, 0, r2.getFloors().size())).get(1);


        //create a path from coordinate 1 to 2 starting with x, as we step through the tiles, we change the tiles to floors
        while (x1 != x2) {
            worldArr[x1][y1] = Tileset.GRASS;
            //if the adjacent tiles are nothing (are not walls or floors since we can pass through other rooms on our path)
            //change nothing to walls to enclose the hallway
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
        //handle corner walls
        if (worldArr[x1][y1 + 1] == Tileset.NOTHING) {
            worldArr[x1][y1 + 1] = Tileset.WALL;
        }
        if (worldArr[x1][y1 - 1] == Tileset.NOTHING) {
            worldArr[x1][y1 - 1] = Tileset.WALL;
        }

        //repeat for y
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
        //handle corner walls
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
        ter.initialize(80, 30);


        World world = new World( 800421773788498935L, 80, 30);
        TETile[][] worldArr = world.returnWorldArr();

        ter.renderFrame(worldArr);

    }
}
