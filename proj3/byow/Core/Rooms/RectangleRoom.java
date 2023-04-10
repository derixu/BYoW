package byow.Core.Rooms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class RectangleRoom implements Room {
    public HashSet<ArrayList<Integer>> Walls; //coordinates of all wall tiles
    public HashSet<ArrayList<Integer>> Floors; //coordinates of all floor tiles
    public RectangleRoom(int x, int y, int width, int height) {
        // (x1, y1) coordinates of bottom left corner
        Walls = new HashSet<>();//each coordinate has two values, there are width*2 + height*2 - 4 coordinates (subtract 4 corners due to overlap)
        Floors = new HashSet<>();
        for (int curr_x = x; curr_x < x + width; curr_x++) {
            for (int curr_y = y; curr_y < y + height; curr_y++) {
                if (curr_x == x || curr_y == y || curr_x == x+width-1 || curr_y == y+height-1) {
                    Walls.add(new ArrayList<>(Arrays.asList(curr_x, curr_y)));
                }
                else {
                    Floors.add(new ArrayList<>(Arrays.asList(curr_x, curr_y)));
                }
            }
        }
    }

    @Override
    public HashSet<ArrayList<Integer>> GetWalls() {
        return Walls;
    }

    @Override
    public HashSet<ArrayList<Integer>> GetFloors() {
        return Floors;
    }

    @Override
    public Boolean Overlap(HashSet<Room> rooms) {
        for (Room room : rooms) {
            for (ArrayList coordinates : room.GetWalls()) {
                if (this.Walls.contains(coordinates)) {
                    return true;
                }
                if (this.Floors.contains(coordinates)) {
                    return true;
                }
            }
        }
        return false;
    }
}
