package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

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
    	sensorSim.loadAltFloorPlan();
    	
    	//Testing simple conditionals
    	assertTrue(sensorSim.checkForBarrier(new Coordinates(0,1), Direction.North));
    	assertTrue(sensorSim.checkForBarrier(new Coordinates(2,1), Direction.North));
    	assertTrue(sensorSim.checkForBarrier(new Coordinates(2,1), Direction.East));
    	Coordinates robotStartCoord = new Coordinates(0,1);
    	
    	TileGraph internalGraph = new TileGraph(robotStartCoord, sensors);
    	//Note, when we move, this is when we populate surroundings
    	
    	
    	//THIS IS A CHEAT WAY TO FILL OUT THE GRAPH, DO NOT DO IN FINAL VERSION:
    	Coordinates[] unvisited = internalGraph.getUnknownCoordinates().toArray(new Coordinates[0]);
    	while(unvisited.length != 0) {
    		for(int i = 0; i < unvisited.length; i++) {
    			internalGraph.populateSurroundings(unvisited[i]);
    		}
    		unvisited = internalGraph.getUnknownCoordinates().toArray(new Coordinates[0]);
    	}
    	
    	//Testing for different tile version
    	assertEquals(2.5, (internalGraph.getVertex(new Coordinates(2,0)).getTileEdge(Direction.East).getEdgeCost()));
    	
    	System.out.print(internalGraph.toString());
    	
    	Coordinates start = new Coordinates(0,1);
    	Coordinates end = new Coordinates(5,1);
    	internalGraph.pathTo(start,end);
    	
	}

}