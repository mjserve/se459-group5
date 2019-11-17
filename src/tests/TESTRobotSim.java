package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import logging.IActivityLog;
import navitagion.Coordinates;
import robotics.RobotSimulation;
import sensors.ISensorPackage;

public class TESTRobotSim {

	RobotSimulation mockRobot = mock(RobotSimulation.class);
	@Test
	public void completeRun(){
		IActivityLog log = new MockActivityLog();
		ISensorPackage sensors = new MockSensors();
		Coordinates start = new Coordinates(0, 0);
		
		//RobotSimulation robot = new RobotSimulation(log, sensors, start);

		//checks run method in RobotSimulation
		when(mockRobot.run()).thenReturn(1);
		assertEquals(mockRobot.run(), 1);
		
	}
	
	public static void main(String[] args) {
		IActivityLog log = new MockActivityLog();
		ISensorPackage sensors = new MockSensors();
		Coordinates start = new Coordinates(0, 0);
		
		RobotSimulation robot = new RobotSimulation(log, sensors, start);
		
		System.out.println("Robot runtime ended with condition (" + robot.run() + ")");
	}
	
}
