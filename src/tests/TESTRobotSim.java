package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;

import WorldSimulator.OutOfFloorMapBoundsException;
import logging.IActivityLog;
import navitagion.Coordinates;
import robotics.RobotSimulation;
import sensors.ISensorPackage;

public class TESTRobotSim {

	public void completeRun() throws OutOfFloorMapBoundsException{
		IActivityLog log = new MockActivityLog();
		ISensorPackage sensors = new MockSensors();
		Coordinates start = new Coordinates(0, 0);
		
		RobotSimulation robot = new RobotSimulation(log, sensors, start);
		
		try {
			assertEquals(robot.run(), 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws OutOfFloorMapBoundsException {
		IActivityLog log = new MockActivityLog();
		ISensorPackage sensors = new MockSensors();
		Coordinates start = new Coordinates(0, 0);
		
		RobotSimulation robot = new RobotSimulation(log, sensors, start);
		
		try {
			System.out.println("Robot runtime ended with condition (" + robot.run() + ")");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
