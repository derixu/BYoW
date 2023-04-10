package byow.Core.Rooms;

import java.util.ArrayList;
import java.util.HashSet;

//created interface for new room types in the future (different shapes maybe)
public interface Room {

    public HashSet<ArrayList<Integer>> GetWalls();

    public HashSet<ArrayList<Integer>> GetFloors();

    public Boolean Overlap(HashSet<Room> rooms);

}
