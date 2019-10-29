package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import logging.IActivityLog;
import navitagion.Coordinates;
import robotics.map.InternalPath;
import sensors.ISensorPackage;

public class TESTMoveOnPath {

	@Test
	public void testPathing(){
		IActivityLog log = new MockActivityLog();
		ISensorPackage sensors = new MockSensors();
		Coordinates start = new Coordinates(0, 0);
		
		MockRobotSimulation robot = new MockRobotSimulation(log, sensors, start);
		
		InternalPath path = new InternalPath();
		
		path.push(new Coordinates(5,0));
		path.push(new Coordinates(4,0));
		path.push(new Coordinates(3,0));
		path.push(new Coordinates(2,0));
		path.push(new Coordinates(1,0));
		
		try {
			robot.runPath(path);
		} catch (Exception e) {
			fail("Pathing Failed on Exception: " + e.toString());
		}
		
		assertTrue(new Coordinates(5,0).equals(robot.position()));
	}

	
}
