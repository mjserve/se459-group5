package sensors;

import navitagion.Coordinates;
import navitagion.Direction;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensorSimulator.SensorSim;
import sensorSimulator.TileType;

import static sensorSimulator.TileType.OBSTACLE;

public class Sensor implements ISensorPackage {
    int x, y;
    private Coordinates temp = new Coordinates(x, y);
    private TileType currentTile;


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
        return false;
    }

    @Override
    public TileType terrainType(Coordinates coord) {
        return TileType.HIGH;
    }

    @Override
    public Coordinates[] stationInRange(Coordinates coord) {
        return new Coordinates[0];
    }

    @Override
    public void cleanTile(Coordinates coord) {

    }
}
