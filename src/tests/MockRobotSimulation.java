package tests;

import java.util.Queue;
import logging.IActivityLog;
import navitagion.Coordinates;
import robotics.RobotSimulation;
import robotics.map.InternalPath;
import sensors.ISensorPackage;

public class MockRobotSimulation extends RobotSimulation{

	public MockRobotSimulation(IActivityLog log, ISensorPackage sensors, Coordinates start) {
		super(log, sensors, start);
	}

	public MockRobotSimulation(IActivityLog log, ISensorPackage sensors, Coordinates start, Coordinates[] stations) {
		super(log, sensors, start, stations);
	}
	
	
	public Coordinates runPath(InternalPath path) throws Exception {
	//Test running of robot along specified path.	
		moveOnPath(path);
		
		return currentPosition;
		
	}
	
	public Coordinates position() {
		return currentPosition;
	}

}
