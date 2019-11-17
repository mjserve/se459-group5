package sensors;

import WorldSimulator.WorldSim;
import navitagion.Coordinates;
import navitagion.Direction;
import WorldSimulator.OutOfFloorMapBoundsException;
import WorldSimulator.TileType;

public class Sensor implements ISensorPackage {
    private int x, y;
    private Coordinates temp = new Coordinates(x, y);
    private TileType currentTile;


    @Override
    public boolean collisionWest(Coordinates coord) {
        return WorldSim.getInstance().checkForBarrier(coord, Direction.West);
    }

    @Override
    public boolean collisionNorth(Coordinates coord) {
        return WorldSim.getInstance().checkForBarrier(coord, Direction.North);
    }

    @Override
    public boolean collisionEast(Coordinates coord) {
        return WorldSim.getInstance().checkForBarrier(coord, Direction.East);
    }

    @Override
    public boolean collisionSouth(Coordinates coord) {
        return WorldSim.getInstance().checkForBarrier(coord, Direction.South);
    }

    @Override
    public boolean dirtDetector(Coordinates coord) {
        return false;
    }

    @Override
    public TileType terrainType(Coordinates coord) throws OutOfFloorMapBoundsException{
        return WorldSim.getInstance().retrieveTileType(coord);
    }

    @Override
    public Coordinates[] stationInRange(Coordinates coord) {
        return new Coordinates[0];
    }

    @Override
    public void cleanTile(Coordinates coord) {
    	WorldSim.getInstance().removeDirt(coord);
    }
}
