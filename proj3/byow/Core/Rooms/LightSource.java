package byow.Core.Rooms;

import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;

import java.awt.*;


public class LightSource {
    private int range = 4;
    private int dr;
    private int dg;
    private int db;

    public LightSource(Room room, World world, Color startingColor) {
        int xCoord = room.randomFloor().get(0);
        int yCoord = room.randomFloor().get(1);

        world.alterTiles(xCoord, yCoord, Tileset.LIGHT);

        TETile centerTile = TETile.changeBackground(Tileset.FLOOR, startingColor);
        dr = -startingColor.getRed() / range;
        dg = -startingColor.getGreen() / range;
        db = -startingColor.getBlue() / range;

        int leftX = xCoord;
        int rightX = xCoord;
        int botY = yCoord;
        int topY = yCoord;

        for (int dist = 1; dist < range; dist++) {
            TETile gradient = TETile.gradientVariant(centerTile, dr * dist, dg * dist, db * dist);
            leftX--;
            rightX++;
            botY--;
            topY++;

            for (int x = leftX; x <= rightX; x++) {
                if (world.getTile(x, botY) == Tileset.FLOOR && room.inRoom(x, botY)) {
                    world.alterTiles(x, botY, gradient);
                }
                if (world.getTile(x, topY) == Tileset.FLOOR && room.inRoom(x, topY)) {
                    world.alterTiles(x, topY, gradient);
                }
            }
            for (int y = botY; y <= topY; y++) {
                if (world.getTile(leftX, y) == Tileset.FLOOR && room.inRoom(leftX, y)) {
                    world.alterTiles(leftX, y, gradient);
                }
                if (world.getTile(rightX, y) == Tileset.FLOOR && room.inRoom(rightX, y)) {
                    world.alterTiles(rightX, y, gradient);
                }
            }
        }
    }
}
