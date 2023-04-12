package byow.Core.Rooms;

import java.util.ArrayList;

//created interface for new room types in the future (different shapes maybe)
public interface Room {

    public ArrayList<ArrayList<Integer>> getWalls();

    public ArrayList<ArrayList<Integer>> getFloors();

    public Boolean overlap(ArrayList<Room> rooms);

}
