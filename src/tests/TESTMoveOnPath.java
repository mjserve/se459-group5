package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import WorldSimulator.OutOfFloorMapBoundsException;
import WorldSimulator.WorldSim;
import logging.IActivityLog;
import navitagion.Coordinates;
import navitagion.Direction;
import robotics.map.InternalPath;
import robotics.map.TileGraph;
import sensors.ISensorPackage;
import sensors.Sensor;

public class TESTMoveOnPath {
	//setup
	MockRobotSimulation mockRobotSim = mock(MockRobotSimulation.class);
	@Test
	public void testPathing() throws OutOfFloorMapBoundsException{
    	ISensorPackage sensors = new Sensor();
    	IActivityLog log = new MockActivityLog();
    	
		WorldSim sensorSim = WorldSim.getInstance();
    	sensorSim.loadAltFloorPlan();
    	
    	Coordinates robotStartCoord = new Coordinates(0,1);
    	
    	MockRobotSimulation robot = new MockRobotSimulation(log, sensors, robotStartCoord);


    	
    	
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
    	

    	Coordinates end = new Coordinates(5,1);

    	//checks position method in MockRobotSimulation
		doReturn(end).when(mockRobotSim).position();
    	
    	InternalPath path = internalGraph.pathTo(robotStartCoord,end);
		
		try {
			mockRobotSim.runPath(path);
		} catch (Exception e) {
			fail("Pathing Failed on Exception: " + e.toString());
		}
		//position mock
		assertTrue(end.equals(mockRobotSim.position()));
	}

	
}
