package byow.Core.Rooms;

import java.util.ArrayList;

//created interface for new room types in the future (different shapes maybe)
public interface Room {

    public ArrayList<ArrayList<Integer>> getWalls();

    public ArrayList<ArrayList<Integer>> getFloors();

    public Boolean overlap(ArrayList<Room> rooms);

    public ArrayList<Integer> randomFloor();

    public int getHeight();

    public int getWidth();

    public boolean inRoom(int x, int y);

}
