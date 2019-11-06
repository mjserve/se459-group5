package sensors;

import navitagion.Coordinates;
import navitagion.Direction;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensorSimulator.SensorSim;
import sensorSimulator.Tile;
import sensorSimulator.TileType;

import static sensorSimulator.TileType.OBSTACLE;

public class Sensor implements ISensorPackage {
    private int x, y;
    private Coordinates temp = new Coordinates(x, y);
    private TileType currentTile;
    private Tile tile;


    @Override
    public boolean collisionWest(Coordinates coord) {
        return SensorSim.getInstance().checkForBarrier(coord, Direction.West);
    }

    @Override
    public boolean collisionNorth(Coordinates coord) {
        return SensorSim.getInstance().checkForBarrier(coord, Direction.North);
    }

    @Override
    public boolean collisionEast(Coordinates coord) {
        return SensorSim.getInstance().checkForBarrier(coord, Direction.East);
    }

    @Override
    public boolean collisionSouth(Coordinates coord) {
        return SensorSim.getInstance().checkForBarrier(coord, Direction.South);
    }

    @Override
    public boolean dirtDetector(Coordinates coord) {
        tile = SensorSim.getInstance().getCurrentTile(coord);
        if (tile.getDirtVal() != 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public TileType terrainType(Coordinates coord) throws OutOfFloorMapBoundsException{
        return SensorSim.getInstance().retrieveTileType(coord);
    }

    @Override
    public Coordinates[] stationInRange(Coordinates coord) {
        return new Coordinates[0];
    }

    @Override
    public void cleanTile(Coordinates coord) {
        /*
        tile = SensorSim.getInstance().getCurrentTile(coord);
        int currentDirtVal = tile.getDirtVal();
        int newDirtVal;
        if(currentDirtVal != 0){
            currentDirtVal--;
        }
        */
    }
}
