package robotics;

import org.junit.Test;
import logging.IActivityLog;
import logging.TESTActivityLog;
import navitagion.Coordinates;
import sensors.ISensorPackage;
import sensors.TESTSensors;

public class TESTRobotSim {

	@Test
	public void completeRun(){
		IActivityLog log = new TESTActivityLog();
		ISensorPackage sensors = new TESTSensors();
		Coordinates start = new Coordinates(0, 0);
		
		RobotSimulation robot = new RobotSimulation(log, sensors, start);
		
		robot.run();
		
	}
	
	public static void main(String[] args) {
		IActivityLog log = new TESTActivityLog();
		ISensorPackage sensors = new TESTSensors();
		Coordinates start = new Coordinates(0, 0);
		
		RobotSimulation robot = new RobotSimulation(log, sensors, start);
		
		robot.run();
	}
	
}
