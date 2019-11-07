package main;

import logging.IActivityLog;
import sensors.ISensorPackage;
import sensors.Sensor;
import tests.MockActivityLog;
import sensorSimulator.SensorSim;
import navitagion.Coordinates;
import robotics.RobotSimulation;

public class Main {

    public static void main(String[] args){
        //Sample call of how using SensorSim will be used to implement ISensorPackage Interface
    	ISensorPackage sensors = new Sensor();
    	IActivityLog log = new MockActivityLog();

    	//Loads default map. Change map used here
		SensorSim.getInstance().loadJSONFloorPlan("FloorMapA");
    	
    	RobotSimulation notRoomba = new RobotSimulation(log, sensors, new Coordinates(0,0));
    	
    	notRoomba.stateRun();
    	
    	/*

        SensorSim.getInstance().loadFloorPlan();
        Sensor sensor = new Sensor();
        System.out.print(sensor.collisionBelow(new Coordinates(0, 0)));
    	 */

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