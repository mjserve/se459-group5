package robotics;

//import org.junit.Test;
import logging.IActivityLog;
import navitagion.Coordinates;
import robotics.Mocks.MockActivityLog;
import robotics.Mocks.MockSensors;
import sensors.ISensorPackage;

public class TESTRobotSim {

	//@Test
	public void completeRun(){
		IActivityLog log = new MockActivityLog();
		ISensorPackage sensors = new MockSensors();
		Coordinates start = new Coordinates(0, 0);
		
		RobotSimulation robot = new RobotSimulation(log, sensors, start);
		
		System.out.println("Robot runtime ended with condition (" + robot.run() + ")");
		
	}
	
	public static void main(String[] args) {
		IActivityLog log = new MockActivityLog();
		ISensorPackage sensors = new MockSensors();
		Coordinates start = new Coordinates(0, 0);
		
		RobotSimulation robot = new RobotSimulation(log, sensors, start);
		
		System.out.println("Robot runtime ended with condition (" + robot.run() + ")");
	}
	
}
