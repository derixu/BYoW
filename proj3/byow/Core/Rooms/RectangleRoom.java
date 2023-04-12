package byow.Core.Rooms;

import java.util.ArrayList;
import java.util.Arrays;

public class RectangleRoom implements Room {
    private ArrayList<ArrayList<Integer>> walls; //coordinates of all wall tiles
    private ArrayList<ArrayList<Integer>> floors; //coordinates of all floor tiles
    public RectangleRoom(int x, int y, int width, int height) {
        // (x, y) coordinates of bottom left corner
        walls = new ArrayList<>();
        floors = new ArrayList<>();

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
}
