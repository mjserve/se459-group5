package main;

import WorldSimulator.OutOfFloorMapBoundsException;
import logging.IActivityLog;
import sensors.ISensorPackage;
import sensors.Sensor;
import tests.MockActivityLog;
import WorldSimulator.WorldSim;
import navitagion.Coordinates;
import robotics.RobotSimulation;

public class Main {

    public static void main(String[] args){
        //Sample call of how using WorldSim will be used to implement ISensorPackage Interface
    	ISensorPackage sensors = new Sensor();
    	IActivityLog log = new MockActivityLog();

    	//Loads default map. Change map used here
		WorldSim.getInstance().loadJSONFloorPlan("FloorMapA");
    	
    	RobotSimulation notRoomba = new RobotSimulation(log, sensors, new Coordinates(0,0));

    	try
		{
			notRoomba.run();
		}
    	catch (OutOfFloorMapBoundsException e)
		{
			e.printStackTrace();
		}

    	
    	/*

        WorldSim.getInstance().loadFloorPlan();
        Sensor sensor = new Sensor();
        System.out.print(sensor.collisionBelow(new Coordinates(0, 0)));
    	 */

        /*
        try{
            typeHolder = WorldSim.getInstance().retrieveTileType(new Coordinates(3, 1));
            System.out.println(typeHolder);
        }
        catch(OutOfFloorMapBoundsException e){
            System.out.println(e.getMessage());
        }
        */

    }
}