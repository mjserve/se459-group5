package main;

import navitagion.Coordinates;
import sensorSimulator.SensorSim;
import sensorSimulator.TileType;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensors.Sensor;

public class Main {

    public static void main(String[] args){

        //Sample call of how using SensorSim will be used to implement ISensorPackage Interface



        TileType typeHolder;

        //Testing terrainType
        SensorSim.getInstance().loadFloorPlan();
        Sensor sensor = new Sensor();
        try{
            typeHolder = sensor.terrainType(new Coordinates(0, 0));
            System.out.println(typeHolder);
        }catch(OutOfFloorMapBoundsException e){
            System.out.println(e.getMessage());
        }



        /*
        try{
            typeHolder = SensorSim.getInstance().retrieveTileType(new Coordinates(3, 1));
            System.out.println(typeHolder);
        }
        catch(OutOfFloorMapBoundsException e){
            System.out.println(e.getMessage());
        }
        */

    }


	
}
