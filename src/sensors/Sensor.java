package sensors;

import navitagion.Coordinates;
import navitagion.Direction;
import sensorSimulator.SensorSim;
import sensorSimulator.TileType;

public class Sensor implements ISensorPackage {
    int x, y;
    private Coordinates temp = new Coordinates(x, y);
    private TileType currentTile;


    @Override
    public boolean collisionLeft(Coordinates coord) {
        return SensorSim.getInstance().checkForBarrier(coord, Direction.West);
    }

    @Override
    public boolean collisionAbove(Coordinates coord) {
        return SensorSim.getInstance().checkForBarrier(coord, Direction.North);
    }

    @Override
    public boolean collisionRight(Coordinates coord) {
        return SensorSim.getInstance().checkForBarrier(coord, Direction.East);
    }

    @Override
    public boolean collisionBelow(Coordinates coord) {
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
