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

    private int width;
    private int height;
    private Random seed;
    public World(Random seed, int width, int height) {

        this.width = width;
        this.height = height;

        //initialize World as empty
        worldArray = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                worldArray[x][y] = Tileset.NOTHING;
            }
        }

        //decide seed
        this.seed = seed;


        //parameterize the world (important part is setting numRooms to an integer, process is adaptable
        int minRooms = 5;
        int maxRooms = 25;
        int numRooms = RandomUtils.uniform(seed, minRooms, maxRooms);

        int minLength = 5;
        int maxLength = 10;

        //create rooms using parameters
        roomHelper(numRooms, minLength, maxLength);

        //create a copy of the rooms list so that we can mutate it without risks
        ArrayList<Room> mutableRooms = new ArrayList<>(rooms);

        //iterate through rooms and connect them with their closest room that has not been iterated on yet
        //this algorithm could potentially use improvement to prevent excessive overlaps
        //also horrible runtime in theory
        for (int i = 0; i < rooms.size() - 1; i++) {
            double x = Double.valueOf(rooms.get(i).getWalls().get(0).get(0));
            double y = Double.valueOf(rooms.get(i).getWalls().get(0).get(1));

            //initialize closest to an arbitrary room with max distance
            Room closest = new RectangleRoom(seed, 0, 0, 0, 0);
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
            hallwayHelper(rooms.get(i), closest);
        }
    }

    private void roomHelper(int numRooms, int minLength, int maxLength) {
        for (int n = 0; n <= numRooms; n++) {

            //parameterize the room and create it
            int roomWidth = RandomUtils.uniform(seed, minLength,  maxLength);
            int roomHeight = RandomUtils.uniform(seed, minLength,  maxLength);

            int x = RandomUtils.uniform(seed, 0,  width - roomWidth);
            int y = RandomUtils.uniform(seed, 0,  height - roomHeight);
            RectangleRoom room = new RectangleRoom(seed, x, y, roomWidth, roomHeight);

            //if room overlaps with any previous rooms in the world, redo parameterization and initialization
            int tries = 0;
            while (room.overlap(rooms)) {
                if (tries > 20) {
                    break;
                }
                x = RandomUtils.uniform(seed, 0,  width - roomWidth);
                y = RandomUtils.uniform(seed, 0,  height - roomHeight);
                room = new RectangleRoom(seed, x, y, roomWidth, roomHeight);
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
    }

    private void hallwayHelper(Room r1, Room r2) {

        //get coordinates of each room (random floor coordinate)
        int x1 = r1.randomFloor().get(0);
        int y1 = r1.randomFloor().get(1);

        int x2 = r2.randomFloor().get(0);
        int y2 = r2.randomFloor().get(1);

        //create a path from coordinate 1 to 2 starting with x, as we step through the tiles, we change the tiles to floors

        int xDirection = 1;
        int yDirection = 1;

        if (x1 > x2) {
            xDirection = -1;
        }

        if (y1 > y2) {
            yDirection = -1;
        }

        //if our start x is not equal to our end x, increment until we reach the end x value
        while (x1 != x2) {
            worldArray[x1][y1] = Tileset.GRASS;
            //add adjacent walls if necessary
            addXAdjacents(x1, y1);
            x1 += xDirection;
        }
        //final adjacent add for x1 = x2
        addXAdjacents(x1, y1);
        //ensure that the corners are filled
        addXAdjacents(x1 + xDirection, y1);


        //if our start y not equal to end y, increment until we reach our end y
        while (y1 != y2) {
            worldArray[x1][y1] = Tileset.GRASS;
            //add adjacent walls if necessary
            addYAdjacents(x1, y1);
            y1 += yDirection;
        }
    }

    private void addXAdjacents(int x, int y) {
        if (worldArray[x][y + 1] == Tileset.NOTHING) {
            worldArray[x][y + 1] = Tileset.WALL;
        }
        if (worldArray[x][y - 1] == Tileset.NOTHING) {
            worldArray[x][y - 1] = Tileset.WALL;
        }
    }

    private void addYAdjacents(int x, int y) {
        if (worldArray[x + 1][y] == Tileset.NOTHING) {
            worldArray[x + 1][y] = Tileset.WALL;
        }
        if (worldArray[x - 1][y] == Tileset.NOTHING) {
            worldArray[x - 1][y] = Tileset.WALL;
        }
    }

    public TETile[][] returnWorldArr() {
        return worldArray;
    }

    public ArrayList<Room> returnRooms() {
        return rooms;
    }

    public void alterTiles(int x, int y, TETile tile) {
        worldArray[x][y] = tile;
    }

    public Room randomRoom() {
        int roomIndex = RandomUtils.uniform(seed, 0, rooms.size());
        return rooms.get(roomIndex);
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);


        World world = new World(new Random(8764478), 80, 30);
        TETile[][] worldArr = world.returnWorldArr();

        ter.renderFrame(worldArr);
    }
}
