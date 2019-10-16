package sensors;

import navitagion.Coordinates;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensorSimulator.SensorSim;
import sensorSimulator.TileType;

import static sensorSimulator.TileType.OBSTACLE;

public class Sensor implements ISensorPackage {
    int x, y;
    private Coordinates temp = new Coordinates(x, y);
    private TileType currentTile;


    @Override
    public boolean collisionLeft(Coordinates coord) {
        temp.x = coord.x - 1;
        temp.y = coord.y;
        try{
            currentTile = SensorSim.getInstance().retrieveTileType(new Coordinates(temp.x, temp.y));
            System.out.println(currentTile);
        }
        //should never reach here but returns true if out of bounds
        catch(OutOfFloorMapBoundsException e){
            return true;
            //System.out.println(e.getMessage());
        }
        //returns true if collision detected and false if it can turn there
        if(currentTile.equals(OBSTACLE)){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean collisionAbove(Coordinates coord) {
        temp.x = coord.x;
        temp.y = coord.y + 1;
        try{
            currentTile = SensorSim.getInstance().retrieveTileType(new Coordinates(temp.x, temp.y));
            System.out.println(currentTile);
        }
        //should never reach here but returns true if out of bounds
        catch(OutOfFloorMapBoundsException e){
            return true;
            //System.out.println(e.getMessage());
        }
        //returns true if collision detected and false if it can turn there
        if(currentTile.equals(OBSTACLE)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean collisionRight(Coordinates coord) {
        temp.x = coord.x + 1;
        temp.y = coord.y;
        try{
            currentTile = SensorSim.getInstance().retrieveTileType(new Coordinates(temp.x, temp.y));
            System.out.println(currentTile);
        }
        //should never reach here but returns true if out of bounds
        catch(OutOfFloorMapBoundsException e){
            return true;
            //System.out.println(e.getMessage());
        }
        //returns true if collision detected and false if it can turn there
        if(currentTile.equals(OBSTACLE)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean collisionBelow(Coordinates coord) {
        temp.x = coord.x;
        temp.y = coord.y - 1;
        try{
            currentTile = SensorSim.getInstance().retrieveTileType(new Coordinates(temp.x, temp.y));
            System.out.println(currentTile);
        }
        //should never reach here but returns true if out of bounds
        catch(OutOfFloorMapBoundsException e){
            return true;
            //System.out.println(e.getMessage());
        }
        //returns true if collision detected and false if it can turn there
        if(currentTile.equals(OBSTACLE)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean dirtDetector(Coordinates coord) {
        return false;
    }

    @Override
    public TileType terrainType(Coordinates coord) {
        return 0;
    }

    @Override
    public Coordinates[] stationInRange(Coordinates coord) {
        return new Coordinates[0];
    }

    @Override
    public void cleanTile(Coordinates coord) {

    }
}
