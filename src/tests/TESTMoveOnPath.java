package tests;

import org.junit.jupiter.api.Test;
import logging.IActivityLog;
import navitagion.Coordinates;
import robotics.RobotSimulation;
import sensors.ISensorPackage;

public class TESTMoveOnPath {

	@Test
	public void testPathing(){
		IActivityLog log = new MockActivityLog();
		ISensorPackage sensors = new MockSensors();
		Coordinates start = new Coordinates(0, 0);
		
		RobotSimulation robot = new RobotSimulation(log, sensors, start);
		
		
	}

	
}
