package main;

import navitagion.Coordinates;
import sensorSimulator.SensorSim;
import sensorSimulator.TileType;
import sensors.Sensor;

public class Main {

    public static void main(String[] args){

        //Sample call of how using SensorSim will be used to implement ISensorPackage Interface



        TileType typeHolder;

        SensorSim.getInstance().loadFloorPlan();
        Sensor sensor = new Sensor();
        System.out.print(sensor.collisionBelow(new Coordinates(0, 0)));


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
