package byow.Core.Rooms;

import byow.Core.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RectangleRoom implements Room {
    private ArrayList<ArrayList<Integer>> walls; //coordinates of all wall tiles
    private ArrayList<ArrayList<Integer>> floors; //coordinates of all floor tiles

    public int width;
    public int height;
    private Random seed;
    int x;
    int y;

    public RectangleRoom(Random seed, int x, int y, int width, int height) {
        // (x, y) coordinates of bottom left corner
        walls = new ArrayList<>();
        floors = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.seed = seed;
        this.height = height;
        this.width = width;

        //iterate through all coordinates of tiles covered by the room
        for (int currX = x; currX < x + width; currX++) {
            for (int currY = y; currY < y + height; currY++) {
                //if the tile is on the edge, add to wall set
                if (currX == x || currY == y || currX == x + width - 1 || currY == y + height - 1) {
                    walls.add(new ArrayList<>(Arrays.asList(currX, currY)));
                } else { //else add to floor set
                    floors.add(new ArrayList<>(Arrays.asList(currX, currY)));
                }
            }
        }
    }

    @Override
    public ArrayList<ArrayList<Integer>> getWalls() {
        return walls;
    }

    @Override
    public ArrayList<ArrayList<Integer>> getFloors() {
        return floors;
    }

    @Override
    public Boolean overlap(ArrayList<Room> rooms) {
        for (Room room : rooms) {
            //check for overlaps
            // 3 possible cases:
            // only walls overlap (rooms share border)
            // walls AND floors overlap (rooms cross)
            // only floors overlap (one room contains the entire other room)
            //to check we must make sure neither walls overlap each other and neither floors overlap each other

            for (ArrayList coordinates : room.getWalls()) {
                if (this.walls.contains(coordinates)) {
                    return true;
                }
            }
            for (ArrayList coordinates : room.getFloors()) {
                if (this.floors.contains(coordinates)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getWidth() {
        return  width;
    }

    public int getHeight() {
        return  height;
    }

    public boolean inRoom(int x, int y) {
        if (x <= this.x || x >= this.x + width - 1) {
            return false;
        }
        if (y <= this.y || y >= this.y + height - 1) {
            return false;
        }
        return true;
    }

    public ArrayList<Integer> randomFloor() {
        int tileIndex = RandomUtils.uniform(seed, 0, floors.size());
        return floors.get(tileIndex);
    }
}
