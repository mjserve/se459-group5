package main;

import navitagion.Coordinates;
import sensorSimulator.SensorSim;
import sensorSimulator.TileType;
import sensorSimulator.OutOfFloorMapBoundsException;

public class Main {

    public static void main(String[] args){

        //Sample call of how using SensorSim will be used to implement ISensorPackage Interface

        /*
        TileType typeHolder;

        SensorSim.getInstance().loadFloorPlan();

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
