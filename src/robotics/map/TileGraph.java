package robotics.map;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import navitagion.Coordinates;
import navitagion.Direction;
import WorldSimulator.OutOfFloorMapBoundsException;
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
	Map<Coordinates, Boolean> cleanStatus;
	
	ISensorPackage sensor;
	
	//Initializer
	public TileGraph(Coordinates start, ISensorPackage sense) throws OutOfFloorMapBoundsException {
		Graph = new HashMap<Coordinates, TileVertex>();
		unknownCoordinates = new ArrayList<Coordinates>();
		cleanStatus = new HashMap<Coordinates,Boolean>();
		this.sensor = sense;
		
		TileVertex startVertex = new TileVertex(start);
		
		Graph.put(start, startVertex);
		cleanStatus.put(start, false);
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
			cleanStatus.put(pointer, false);
			
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
					cleanStatus.put(pointer.northOf(), false);
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
					cleanStatus.put(pointer.eastOf(), false);
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
					cleanStatus.put(pointer.westOf(), false);
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
					cleanStatus.put(pointer.southOf(), false);
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
	private void updateEdge(Coordinates existingCoord, Coordinates newCoord) {
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
		double edgeCost = (existVert.getTileType().getCost() + newVert.getTileType().getCost())/2.0;
		
		existVert.setTileEdge(up, new TileEdge(edgeCost, newVert));
		newVert.setTileEdge(down, new TileEdge(edgeCost,existVert));
		Graph.put(existingCoord, existVert);
		Graph.put(newCoord, newVert);
	}
	/**
	 * 
	 * @param center is the current coordinate
	 * @return a list of all accessible adjacent vertices, known or unknown.
	 */
	public List<Pair<Direction,TileVertex>> getNeighbors(Coordinates center){
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
	
	public void markAsClean(Coordinates pointer) {
		cleanStatus.put(pointer, true);
	}
	
	public void markAsDirty(Coordinates pointer) {
		cleanStatus.put(pointer, false);
	}
	
	public List<Coordinates> getAllDirty(){
		List<Coordinates> dirty = new ArrayList<Coordinates>();
		cleanStatus.forEach((k,v) -> {if(v != true) dirty.add(k);});
		return dirty;
	}
	
	public Coordinates getClosestUnknown(Coordinates curPosition) {
		Iterator<Coordinates> ourList = unknownCoordinates.iterator();
		double min = Double.MAX_VALUE;
		Coordinates minCoord = new Coordinates(0,0);
		while(ourList.hasNext()) {
			Coordinates next = ourList.next();
			double dist = curPosition.getDistanceTo(next);
			if( dist < min) {
				min = dist;
				minCoord = next;
			}
		}
		return minCoord;
	}
	
	public Coordinates getClosestDirty(Coordinates curPosition) {
		Iterator<Coordinates> ourList = getAllDirty().iterator();
		double min = Double.MAX_VALUE;
		Coordinates minCoord = new Coordinates(0,0);
		while(ourList.hasNext()) {
			Coordinates next = ourList.next();
			double dist = curPosition.getDistanceTo(next);
			if( dist < min) {
				min = dist;
				minCoord = next;
			}
		}
		return minCoord;
	}
	
	public void markAllDirty() {
		cleanStatus.forEach((k,v)-> {v= false;});
	}
	
	public boolean hasDirtyTiles() {
		return cleanStatus.values().contains(false);
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
			List<TileVertex> neighbs = vert.getAllNeighborsVert();
			StringBuilder connections = new StringBuilder("");
			if(!neighbs.isEmpty() || neighbs != null ) {
				neighbs.forEach((vertex)-> {connections.append(" -> " +vertex.toString());});
			}	
			
			textGraph.append("[" + coord.toString() + "]"  + connections.toString() + "\n");
		});
		return textGraph.toString();
	}

	public InternalPath pathTo(Coordinates start, Coordinates end) {
		
		//TODO: Error Checking for invalid coordinates
		if(!Graph.containsKey(start) || !Graph.containsKey(end)) {
			throw new IllegalArgumentException("Invalid Coordinates");
		}
		
		InternalPath curPath = new InternalPath();
		//Holds Distances
		Map<Coordinates, Double> dist = new HashMap<Coordinates, Double>();
		//Holds Parents
		Map<Coordinates, Coordinates> parents = new HashMap<Coordinates,Coordinates>();
		//Holds already visited nodes
		List<Coordinates> visited = new ArrayList<Coordinates>();
		
		//Holds queue of nodes to visit
		Queue<Coordinates> queue = new PriorityQueue<Coordinates>(
				new Comparator<Coordinates>() {
					@Override
					public int compare(Coordinates o1, Coordinates o2) {
						return (int) (dist.get(o1) - dist.get(o2));
					}
				}
				);

		
		//Initialize source, and all vertices
		Graph.forEach((coord, vert)->{
			dist.put(coord, Double.MAX_VALUE);
		});
		dist.put(start, (double) 0);
		queue.add(start);
		
		
		
		while(!queue.isEmpty()) {
			//Grabs minimum value.
			Coordinates current = queue.poll();
			if(current == null) break;
			
			//Mark node as visited
			visited.add(current);
			
			//For each neighbor of queue that has not been visited
			Iterator<Pair<Direction, TileVertex>> neighborsList  = this.getNeighbors(current).iterator();
			while(neighborsList.hasNext()){
				Pair<Direction, TileVertex> neighbor = neighborsList.next();
				//Check if this has been processesed yet
				if(!visited.contains(neighbor.getSecond().getCoordinates())){
					//Calculate edge weights and update costs
					
					
					//Get the direction of the neighbor
					Direction dir = neighbor.getFirst();
					TileVertex neighborVert = neighbor.getSecond();
					//Update distance value by pulling edgcost from current to neighbor
					double tempCost = dist.get(current) + Graph.get(current).getTileEdge(dir).getEdgeCost();
					
					if(tempCost < dist.get(neighborVert.getCoordinates())) {
						dist.put(neighborVert.getCoordinates(), tempCost);
						parents.put(neighborVert.getCoordinates(), current);
						//If we find the path to the end:
						if(neighborVert.getCoordinates().equals(end)) {
							Coordinates head = end;
							curPath.push(head);
							while(parents.get(head)!= null) {
								Coordinates item = parents.get(head);
								curPath.push(item);
								head = item;
							}					
						}
					}
					queue.add(neighborVert.getCoordinates());
				};
			}
			
		}

		curPath.setTotalCost(dist.get(end));
		return curPath;
	}
	

	

}
