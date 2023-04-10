package byow.Core.Rooms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class RectangleRoom implements Room {
    public HashSet<ArrayList<Integer>> Walls; //coordinates of all wall tiles
    public HashSet<ArrayList<Integer>> Floors; //coordinates of all floor tiles
    public RectangleRoom(int x, int y, int width, int height) {
        // (x, y) coordinates of bottom left corner
        Walls = new HashSet<>();
        Floors = new HashSet<>();

        //iterate through all coordinates of tiles covered by the room
        for (int curr_x = x; curr_x < x + width; curr_x++) {
            for (int curr_y = y; curr_y < y + height; curr_y++) {
                //if the tile is on the edge, add to wall set
                if (curr_x == x || curr_y == y || curr_x == x+width-1 || curr_y == y+height-1) {
                    Walls.add(new ArrayList<>(Arrays.asList(curr_x, curr_y)));
                }
                //else add to floor set
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
            //check for overlaps
            // 3 possible cases:
            // only walls overlap (rooms share border)
            // walls AND floors overlap (rooms cross)
            // only floors overlap (one room contains the entire other room)
            //to check we must make sure neither walls overlap each other and neither floors overlap each other

            for (ArrayList coordinates : room.GetWalls()) {
                if (this.Walls.contains(coordinates)) {
                    return true;
                }
            }
            for (ArrayList coordinates : room.GetFloors()) {
                if (this.Floors.contains(coordinates)) {
                    return true;
                }
            }
        }
        return false;
    }
}
