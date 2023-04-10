package byow.Core.Rooms;

import java.util.ArrayList;
import java.util.HashSet;

public interface Room {

    public HashSet<ArrayList<Integer>> GetWalls();

    public HashSet<ArrayList<Integer>> GetFloors();

    public Boolean Overlap(HashSet<Room> rooms);

}
