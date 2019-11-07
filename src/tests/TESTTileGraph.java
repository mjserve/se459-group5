package tests;

import static org.junit.jupiter.api.Assertions.*;

import WorldSimulator.WorldSim;
import org.junit.jupiter.api.Test;
import logging.IActivityLog;
import navitagion.Coordinates;
import navitagion.Direction;
import robotics.map.InternalPath;
import robotics.map.TileGraph;
import WorldSimulator.OutOfFloorMapBoundsException;
import sensors.ISensorPackage;
import sensors.Sensor;

class TESTTileGraph {

	@Test
	void floorBuildingTest() throws OutOfFloorMapBoundsException {
    	ISensorPackage sensors = new Sensor();
    	IActivityLog log = new MockActivityLog();
    	WorldSim worldSim = WorldSim.getInstance();
    	worldSim.loadAltFloorPlan();
    	
    	//Testing simple conditionals
    	assertTrue(worldSim.checkForBarrier(new Coordinates(0,1), Direction.North));
    	assertTrue(worldSim.checkForBarrier(new Coordinates(2,1), Direction.North));
    	assertTrue(worldSim.checkForBarrier(new Coordinates(2,1), Direction.East));
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
    	
    	System.out.print("Internal Graph Representation \n" + internalGraph.toString());
    	
    	Coordinates start = new Coordinates(0,1);
    	Coordinates end = new Coordinates(5,1);
    	InternalPath ourPath = internalGraph.pathTo(start,end);
    	System.out.println("Total Cost: " + ourPath.getTotalCost());
    	System.out.print(ourPath.dirHistory());
    	
	}

}
