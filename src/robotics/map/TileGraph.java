package robotics.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import navitagion.Coordinates;
import navitagion.Direction;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensors.ISensorPackage;

public class TileGraph {
	/*
	 * Goals: 
	 * Create a adjacency list representation of tiles and vertices
	 * Need to:
	 * Create Edges.
	 * Create Vertices.
	 * Populate surrounding area.
	 * 
	 * 
	 * From a starting position [a]:
	 * 1. populateSurrounding
	 * 2. move to the new tile [b]
	 * 3. populate surrounding on that tile
	 * 4. addNewEdge(a,b)
	 * 
	 * 
	 */
	
	//Main graph object
	Map<Coordinates, TileVertex> Graph;
	List<Coordinates> unknownCoordinates;
	ISensorPackage sensor;
	
	//Initializer
	public TileGraph(Coordinates start, ISensorPackage sense) throws OutOfFloorMapBoundsException {
		Graph = new HashMap<Coordinates, TileVertex>();
		unknownCoordinates = new ArrayList<Coordinates>();
		this.sensor = sense;
		
		TileVertex startVertex = new TileVertex(start);
		
		Graph.put(start, startVertex);
		populateSurroundings(start);
	}
	
	//TODO: Work on reducing code reuse.
	public void populateSurroundings(Coordinates pointer) throws OutOfFloorMapBoundsException {
		//Grab the tile vertex in question
		TileVertex center = Graph.get(pointer);
		//Update the tile type sensor.
		center.setTileType(sensor.terrainType(pointer));
		//Since we're populating, we remove the pointer from unknownCoordinates.
		unknownCoordinates.remove(pointer);
		
		
		//IF no collision on northern side...
		if(!sensor.collisionNorth(pointer)) {
			//Check if anything on the NORTH side is in the map already
			TileVertex tempNorth = Graph.get(pointer.northOf());
			if(tempNorth != null) {
				updateEdge(pointer, pointer.northOf());
			}
			//ELSE, initialize a new node.
			else {
				//Create a new unvisited edge
				center.newTileEdge(Direction.North);
				//Add the coordinate to list of available to reach, but unknown
				unknownCoordinates.add(pointer.northOf());
				Graph.put(pointer.northOf(), new TileVertex(pointer.northOf()));
			}
		};
		if(!sensor.collisionEast(pointer)) {
			TileVertex tempEast = Graph.get(pointer.eastOf());
			if(tempEast != null) {
				updateEdge(pointer, pointer.eastOf());
			}
			else {
				center.newTileEdge(Direction.East);
				unknownCoordinates.add(pointer.eastOf());
				Graph.put(pointer.eastOf(), new TileVertex(pointer.eastOf()));
			}
		};
		if(!sensor.collisionWest(pointer)) {
			TileVertex tempWest = Graph.get(pointer.westOf());
			if(tempWest != null) {
				updateEdge(pointer, pointer.westOf());
			}
			else {
				center.newTileEdge(Direction.West);
				unknownCoordinates.add(pointer.westOf());
				Graph.put(pointer.westOf(), new TileVertex(pointer.westOf()));
			}
		};
		if(!sensor.collisionSouth(pointer)) {
			TileVertex tempSouth = Graph.get(pointer.southOf());
			if(tempSouth != null) {
				updateEdge(pointer, pointer.southOf());
			}
			else {
				center.newTileEdge(Direction.South);
				unknownCoordinates.add(pointer.southOf());
				Graph.put(pointer.southOf(), new TileVertex(pointer.southOf()));
			}
		};	
	}
	
	/*
	 * addNewEdge updates the TileEdge values of both nodes, and updates costs for traversing them.
	 */
	public void updateEdge(Coordinates existingCoord, Coordinates newCoord) {
		//Check if coordinates are next to each other.
		if(!existingCoord.isAdjacent(newCoord)) {
			throw new IllegalArgumentException("Coordinates are not adjacent");
			}
		if(!unknownCoordinates.contains(newCoord)) {
			throw new IllegalArgumentException("No current path to specific coordinate");
		}
		
		TileVertex existVert = Graph.get(existingCoord);
		TileVertex newVert   = Graph.get(newCoord);
		
		//Given that they are adjacent, find corresponding cardinal direction
		//edgeDir is from existingVertex -E--> newVertex.  newVertex <--W- existingVertex.
		Direction edgeDir = existVert.getCoordinates().getSide(newVert.getCoordinates());

		Direction up = null;
		Direction down = null;
		switch(edgeDir) {
		case North: {
			up = Direction.North;
			down = Direction.South;
			break;
			}
		case East: {
			up = Direction.East;
			down = Direction.West;
			break;
		}
		case West: {
			up = Direction.West;
			down = Direction.East;
			break;
		}
		case South: {
			up = Direction.South;
			down = Direction.North;
			break;
			}
		}
		//Calculating edge cost
		double edgeCost = (existVert.getTileType().getCost() + newVert.getTileType().getCost())/2;
		
		existVert.setTileEdge(up, new TileEdge(edgeCost, newVert));
		newVert.setTileEdge(down, new TileEdge(edgeCost,existVert));
		
		Graph.replace(existingCoord, existVert);
		Graph.replace(newCoord, newVert);
	}
	
	public List<TileVertex> getNeighbors(Coordinates center){
		return Graph.get(center).getAllNeighbors();
	}
	
	

}
