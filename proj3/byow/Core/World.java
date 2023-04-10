package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import byow.Core.RandomUtils;

import java.awt.*;
import java.util.Random;

public class World {
    public TETile[][] WorldArr;
    public World(String input, int width, int height) {
        WorldArr = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                WorldArr[x][y] = Tileset.NOTHING;
            }
        }
        int minRooms = 15;
        int maxRooms = 20;
        int numRooms = RandomUtils.uniform(new Random(123),minRooms,  maxRooms);

        for (int n = 0; n <= numRooms; n++) {

            int roomWidth = RandomUtils.uniform(new Random(123+n),3,  20);
            int roomHeight = RandomUtils.uniform(new Random(123+n*2),3,  20);

            int x = RandomUtils.uniform(new Random(123+n),0,  width-roomWidth);
            int y = RandomUtils.uniform(new Random(123+n*2),0,  height-roomHeight);

            RectangleRoom room = new RectangleRoom(x, y, roomWidth, roomHeight);

            for (int[] wallCoordinates : room.Walls) {
                WorldArr[wallCoordinates[0]][wallCoordinates[1]] = Tileset.WALL;
            }

            for (int[] floorCoordinates : room.Floors) {
                WorldArr[floorCoordinates[0]][floorCoordinates[1]] = Tileset.GRASS;
            }
        }
    }

    public TETile[][] ReturnWorldArr() {
        return WorldArr;
    }

    private class RectangleRoom {
        public int[][] Walls; //coordinates of all wall tiles
        public int[][] Floors; //coordinates of all floor tiles
        public RectangleRoom(int x, int y, int width, int height) {
            // (x1, y1) coordinates of bottom left corner
            Walls = new int[width*2 + height*2 - 4][2];//each coordinate has two values, there are width*2 + height*2 - 4 coordinates (subtract 4 corners due to overlap)
            Floors = new int[width*height - Walls.length][2];
            int wall_index = 0;
            int floor_index = 0;
            for (int curr_x = x; curr_x < x + width; curr_x++) {
                for (int curr_y = y; curr_y < y + height; curr_y++) {
                    if (curr_x == x || curr_y == y || curr_x == x+width-1 || curr_y == y+height-1) {
                        Walls[wall_index] = new int[]{curr_x, curr_y};
                        wall_index++;
                    }
                    else {
                        Floors[floor_index] = new int[]{curr_x, curr_y};
                        floor_index++;
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(100, 100);

        World world = new World("123", 100, 100);
        TETile[][] worldArr = world.ReturnWorldArr();

        ter.renderFrame(worldArr);

    }
}
