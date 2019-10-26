package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import logging.IActivityLog;
import navitagion.Coordinates;
import navitagion.Direction;
import robotics.RobotSimulation;
import robotics.Mocks.MockActivityLog;
import robotics.map.TileGraph;
import robotics.map.TileVertex;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensorSimulator.SensorSim;
import sensors.ISensorPackage;
import sensors.Sensor;

class TESTTileGraph {

	@Test
	void floorBuildingTest() throws OutOfFloorMapBoundsException {
    	ISensorPackage sensors = new Sensor();
    	IActivityLog log = new MockActivityLog();
    	
    	SensorSim sensorSim = SensorSim.getInstance();
    	sensorSim.loadFloorPlan();
    	assertTrue(sensorSim.checkForBarrier(new Coordinates(0,1), Direction.North));
    	
    	assertTrue(sensorSim.checkForBarrier(new Coordinates(2,1), Direction.North));
    	assertTrue(sensorSim.checkForBarrier(new Coordinates(2,1), Direction.East));
    	Coordinates robotStartCoord = new Coordinates(0,1);
    	
    	TileGraph internalGraph = new TileGraph(robotStartCoord, sensors);
    	//Note, when we move, this is when we populate surroundings
    	internalGraph.populateSurroundings(new Coordinates(1,1));
    	internalGraph.populateSurroundings(new Coordinates(0,0));
    	System.out.print(internalGraph.toString());
    	
    	TileVertex temp = internalGraph.getVertex(new Coordinates(1,0));
    	//System.out.print("Neighbors" + internalGraph.getNeighbors(new Coordinates(1 ,0)));
    	System.out.print(temp.getAllNeighbors());
	}

}
