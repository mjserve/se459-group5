package robotics.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import navitagion.Coordinates;
import navitagion.Direction;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensors.ISensorPackage;

public class TileGraph {
	/*
	 * USAGE:
	 * How to populate the tile graph:
	 *
	 * 1. Everytime you move the robot one tile to a place that is UNKNOWN:
	 * 			Call populateSurroundings
	 * 3. populate surrounding on that tile
	 * 4. Edges should be build automatically
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
	
	/**
	 * <b>Most important function that has to be consistently called while moving.</b>
	 * 
	 * Grows the graph by adding and updating all adjacent edges on a given pointer.
	 * 
	 * @param pointer
	 * @throws OutOfFloorMapBoundsException
	 */
	public void populateSurroundings(Coordinates pointer) throws OutOfFloorMapBoundsException {
		//Needs to repeat twice, to double check the edges.
		int i = 0;
			while(i < 2) {
			
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
			i++;
		}
	}
	
	/**
	 * Updates the TileEdge values of both nodes, and updates costs for traversing them.
	 * @param existingCoord The current coordinate
	 * @param newCoord		The coordinate you want to bind to
	 */
	public void updateEdge(Coordinates existingCoord, Coordinates newCoord) {
		//Check if coordinates are next to each other.
		if(!existingCoord.isAdjacent(newCoord)) {
			throw new IllegalArgumentException("Coordinates are not adjacent");
			}
		if(!unknownCoordinates.contains(newCoord) && !Graph.containsKey(newCoord)) {
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
		
		existVert.setTileEdge(down, new TileEdge(edgeCost, newVert));
		newVert.setTileEdge(up, new TileEdge(edgeCost,existVert));
		Graph.put(existingCoord, existVert);
		Graph.put(newCoord, newVert);
	}
	/**
	 * 
	 * @param center is the current coordinate
	 * @return a list of all accessible adjacent vertices, known or unknown.
	 */
	public List<TileVertex> getNeighbors(Coordinates center){
		return Graph.get(center).getAllNeighbors();
	}
	
	/**
	 * Used to get accessible, but currently unknown coordinates
	 * @return List of Coordinates
	 */
	public List<Coordinates> getUnknownCoordinates(){
		return this.unknownCoordinates;
	}
	
	
	public boolean hasUnknownCoordinates() {
		return !unknownCoordinates.isEmpty();
	}
	
	/**
	 * Returns vertex at given coordinate
	 */
	public TileVertex getVertex(Coordinates k) {
		return Graph.get(k);
	}
	
	@Override
	public String toString() {
		StringBuilder textGraph = new StringBuilder("");
		Graph.forEach((coord, vert)-> {
			List<TileVertex> neighbs = vert.getAllNeighbors();
			StringBuilder connections = new StringBuilder("");
			if(!neighbs.isEmpty() || neighbs != null ) {
				//neighbs.forEach((vertex)-> {connections.append(" -> " +vertex.getCoordinates().toString());});
				neighbs.forEach((vertex)-> {connections.append(" -> " +vertex.toString());});
			}	
			
			textGraph.append("[" + coord.toString() + "]"  + connections.toString() + "\n");
		});
		return textGraph.toString();
	}
	

}
