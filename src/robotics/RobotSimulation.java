package robotics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import logging.IActivityLog;
import navitagion.Coordinates;
import navitagion.Direction;
import robotics.map.InternalPath;
import robotics.map.TileGraph;
import WorldSimulator.OutOfFloorMapBoundsException;
import sensors.ISensorPackage;

public class RobotSimulation {

	final static int DUSTCAP = 150;
	final static int POWERCAP = 150;

	protected IActivityLog log;
	protected ISensorPackage sensors;
	protected SweeperHardware hardware;
	protected Coordinates currentPosition;
	protected Coordinates nextCoord;
	protected Coordinates station;
	protected TileGraph internalGraph;
	protected HashMap<Direction, Coordinates> adjacentTiles;
	protected InternalPath path;
	protected Direction prevDir;
	
	
	//Constructor

	public RobotSimulation(IActivityLog log, ISensorPackage sensors, Coordinates start) {
		super();
		this.log = log;
		this.sensors = sensors;
		this.currentPosition = start.clone();
		
		this.station = start;
		
		this.hardware = new SweeperHardware(DUSTCAP, POWERCAP);

		try
		{
			this.internalGraph = new TileGraph(currentPosition, sensors);
		}
		catch(OutOfFloorMapBoundsException e)
		{
			e.printStackTrace();
		}
		
		try {
			//Initialize TileGraph
			this.internalGraph = new TileGraph(start, sensors);
		} catch (OutOfFloorMapBoundsException e) {
			e.printStackTrace();
		}

		adjacentTiles = new HashMap<Direction, Coordinates>();
	}


	
	/**
	 * Old Behavior pattern that changes directions only when collsions are detected. 
	 * Will be obsolete once stateRun()
	 * No pathfinding enabled
	 * @return return code
	 * @throws OutOfFloorMapBoundsException 
	 */
	public int run() throws OutOfFloorMapBoundsException, Exception {
		boolean running = true;
		boolean needToCharge = false;
		Coordinates target = currentPosition;
		while(running) {
			//Check if there are unknown tiles:

			if (!needToCharge) {
				if (internalGraph.hasUnknownCoordinates()) {
					target = internalGraph.getClosestUnknown(currentPosition);
				} else if (internalGraph.hasDirtyTiles()) {
					target = internalGraph.getClosestDirty(currentPosition);
				} else {
					running = false;
				} 
			}
			else 
				target = station;
			
			if(running) {
				//This is where we use the target value;
				/*
				 * This is where movement, and cleaning should take place. 
				 * You can also just change the target if you set the tag, needToCharge, so you set the target coordinate to like
				 * the closest charging station
				 * 
				 * ALSO now, its even more important to 
				 * 			internalGraph.populateSurroundings(currentPosition);
				 */

				//Charging
				if (needToCharge) {
					
					System.out.println("\nCharging at: " + target.toString() + " Power: " + hardware.getBattery() + " #Unknown: " 
					+ internalGraph.getUnknownCoordinates().size() + " #dirty: " + internalGraph.getAllDirty().size());
					
					if (currentPosition.equals(target)) {
						hardware.setBattery(POWERCAP);
						needToCharge = false;
					}
					else {
						moveOnPath(internalGraph.pathTo(currentPosition, target));
					}
				}
				
				//Cleaning
				else {
					
					System.out.println("\nCleaning: " + target.toString() + " Power: " + hardware.getBattery() + " #Unknown: " 
							+ internalGraph.getUnknownCoordinates().size() + " #dirty: " + internalGraph.getAllDirty().size());
					
					//move to dirty/unknown
					double allowance = internalGraph.pathTo(currentPosition, target).getTotalCost()
						+ internalGraph.pathTo(target, station).getTotalCost();
	
					allowance = hardware.getBattery() - allowance;
					
					if (allowance >= 1) {
						needToCharge = !moveOnPath(internalGraph.pathTo(currentPosition, target), allowance);
						
						if (!needToCharge) {
							
							while(sensors.dirtDetector(currentPosition)) {
								sensors.cleanTile(currentPosition);
							}
							internalGraph.markAsClean(currentPosition);
							
						}
						
					}
					else {
						needToCharge = true;
					}
				}
				
			}
		}
		
		System.out.println("System Terminated");
		return 1; //This needs to be changed
	}
	
	/**
	 * move along a provided path. No cleaning enabled.
	 * @param path
	 * @return true - successfully walked path, false - issue encountered walking path
	 * @throws Exception"Invalid inputs"
	 */
	protected boolean moveOnPath(InternalPath path) throws Exception{
		
		//Validate Path
		if (path.isEmpty()) 
			throw new Exception("Provided Interal Path object contains no instructions");
		if (!path.peek().equals(currentPosition))
			throw new Exception("Path does not start on the current tile");
		
		//Move on path feeling for walls & dirt
		ListIterator<Direction> instructions = path.dirHistory().listIterator();
		
		while (instructions.hasNext()) {
			Direction next = instructions.next();
			
			switch (next) {
			case North:
				if (sensors.collisionNorth(currentPosition)) {
					//add wall for North edge 
					return false;
				}
				hardware.incrimentBattery(-internalGraph.getVertex(currentPosition).getTileEdge(Direction.North).getEdgeCost());			
				currentPosition.y++;
				break;
			case East:
				if (sensors.collisionEast(currentPosition)) {
					//add wall for East edge 
					return false;
				}
				hardware.incrimentBattery(-internalGraph.getVertex(currentPosition).getTileEdge(Direction.East).getEdgeCost());
				currentPosition.x++;
				break;
			case South:
				if (sensors.collisionSouth(currentPosition)) {
					//add wall for South edge 
					return false;
				}
				hardware.incrimentBattery(-internalGraph.getVertex(currentPosition).getTileEdge(Direction.South).getEdgeCost());
				currentPosition.y--;
				break;
			case West:
				if (sensors.collisionWest(currentPosition)) {
					//add wall for West edge 
					return false;
				}
				hardware.incrimentBattery(-internalGraph.getVertex(currentPosition).getTileEdge(Direction.West).getEdgeCost());
				currentPosition.x--;
				break;
			default:
				throw new IllegalArgumentException("Illegal argument given in instructions");		
			}
			
		}
		
		//Successful execution
		return true;
	}
	
	/**
	 * Move along provided path while cleaning. Will clean all tiles along path using the given energy allowance.
	 * @param path - path of directions that the robot follows to get to it's location.
	 * @param allowance - The amount of energy the robot is allowed to use to clean tiles.
	 * @return Successful or unsuccessful run| True - Success | False - out of power/collision detected
	 * @throws Exception
	 */
	protected boolean moveOnPath(InternalPath path, double allowance) throws Exception {
		
		//Validate Path
		if (path.isEmpty()) 
			return true;
		if (!path.peek().equals(currentPosition))
			throw new Exception("Path does not start on the current tile");
		
		//Base state not enough power.
		if (allowance < 1) 
			throw new Exception ("Not enough power provided for cleaning.");
				
		//Move on path feeling for walls & dirt
		ListIterator<Direction> instructions = path.dirHistory().listIterator();
				
		while (instructions.hasNext()) {
					
			//Clean the current tile and move once done. returns false if energy allowance is exceeded.
			while(sensors.dirtDetector(currentPosition)) {
				
				sensors.cleanTile(currentPosition);
				allowance--;
				if (allowance < 1)
					return false;
				
			}
			
			internalGraph.markAsClean(currentPosition);
					
			Direction next = instructions.next();
			
			switch (next) {
			case North:
				if (sensors.collisionNorth(currentPosition)) {
					//add wall for North edge 
					return false;
				}
				System.out.print("N>");
				hardware.incrimentBattery(-internalGraph.getVertex(currentPosition).getTileEdge(Direction.North).getEdgeCost());			
				currentPosition.y++;
				break;
			case East:
				if (sensors.collisionEast(currentPosition)) {
					//add wall for East edge 
					return false;
				}
				System.out.print("E>");
				hardware.incrimentBattery(-internalGraph.getVertex(currentPosition).getTileEdge(Direction.East).getEdgeCost());
				currentPosition.x++;
				break;
			case South:
				if (sensors.collisionSouth(currentPosition)) {
					//add wall for South edge 
					return false;
				}
				System.out.print("S>");
				hardware.incrimentBattery(-internalGraph.getVertex(currentPosition).getTileEdge(Direction.South).getEdgeCost());
				currentPosition.y--;
				break;
			case West:
				if (sensors.collisionWest(currentPosition)) {
					//add wall for West edge 
					return false;
				}
				System.out.print("W>");
				hardware.incrimentBattery(-internalGraph.getVertex(currentPosition).getTileEdge(Direction.West).getEdgeCost());
				currentPosition.x--;
				break;
			default:
				throw new IllegalArgumentException("Illegal argument given in instructions");		
			}
			
			internalGraph.populateSurroundings(currentPosition);
			
		}
				
		//Successful execution
		return true;
	}
	
	
	/**
	 * Return to a charger and charge to full
	 * @return AquireTarget
	 */
	protected RobotState returnToCharge() {
		return null;
	}
	
	/**
	 * Clean at the current tile until battery reserve or dust capacity is critical. return code indicates how to proceed when returning
	 * @param allowance
	 * @return RobotState - AquireTarget, ReturnToCharger, ReturnHome
	 */
	protected RobotState cleanDestination() {
		return null;
	}
	
	/**
	 * Acquire a new candidate tile & determine if tile is reachable
	 * @return RobotState - ReturnToCharger, MoveToTile, ReturnHome
	 */
	protected RobotState aquiringTarget()
	{
		/*
		* Here is what I'm thinking about 'smart' movement...
		* The robot starts with a list of unknown adjacent tiles. Even though we could continuously move
		* by calculating shortest path on the unknown tiles for every movement but that would be really expensive.
		* So an alternative is this...
		*
		* The robot begins with the list of unknown adjacent tiles, it then chooses any one of them and
		* moves in the direction to it.  Once on this tile, the robot again calls populateSurroundings() to
		* get the unknown adjacent tiles.  If there is an unknown tile in same direction in which the robot last moved,
		* the robot moves to this tile.  The robot would keep moving in the same direction until an obstacle in that
		* direction is met.  Upon hitting the obstacle the robot will again look at adjacent, unknown tiles and choose
		* to move to one of them and continue moving in that direction as long as tiles in that direction are unknown.
		* Eventually the case where all adjacent tiles to the robot are known will occur.  In this case the robot can
		* calculate shortest path to an unknown tile on its global list of unknown tiles that its been building up as it
		* moves. The robot then moves to that unknown tile and repeats the pattern above until all tiles are known.
		*
		* Now I'm not totally sure this will work but it seems to make sense to me.  The stuff I've implemented was super
		* hasty and I don't want to refactor or really continue with it unless it makes sense to you guys.
		*
		* */

		//Populate AdjacentTiles with all unknown, adjacent tiles
		//Used to determine if robot can move in particular direction
		for (int i=0; i < this.internalGraph.getUnknownCoordinates().size(); i++)
		{
			if (this.internalGraph.getUnknownCoordinates().get(i).x == this.currentPosition.x)
			{
				if (this.internalGraph.getUnknownCoordinates().get(i).y == (this.currentPosition.y+1))
				{
					adjacentTiles.put(Direction.North, this.internalGraph.getUnknownCoordinates().get(i));
				}
				if (this.internalGraph.getUnknownCoordinates().get(i).y == (this.currentPosition.y-1))
				{
					adjacentTiles.put(Direction.South, this.internalGraph.getUnknownCoordinates().get(i));
				}
			}
			if (this.internalGraph.getUnknownCoordinates().get(i).y == this.currentPosition.y)
			{
				if (this.internalGraph.getUnknownCoordinates().get(i).x == (this.currentPosition.x+1))
				{
					adjacentTiles.put(Direction.East, this.internalGraph.getUnknownCoordinates().get(i));
				}
				if (this.internalGraph.getUnknownCoordinates().get(i).x == (this.currentPosition.x-1))
				{
					adjacentTiles.put(Direction.West, this.internalGraph.getUnknownCoordinates().get(i));
				}
			}
		}

		if (adjacentTiles.isEmpty())
		{
			//Case where all adjacent tiles have been visited already
			//acquire target via Dijkstra's
		}
		else
		{
			//Startup condition
			//Finds any available direction for initial movement
			if (prevDir == null)
			{
				if (adjacentTiles.containsKey(Direction.North))
				{
					prevDir = Direction.North;
					nextCoord = new Coordinates(this.currentPosition.x, this.currentPosition.y+1);
				}
				else if (adjacentTiles.containsKey(Direction.South))
				{
					prevDir = Direction.South;
					nextCoord = new Coordinates(this.currentPosition.x, this.currentPosition.y-1);
				}
				else if (adjacentTiles.containsKey(Direction.East))
				{
					prevDir = Direction.East;
					nextCoord = new Coordinates(this.currentPosition.x+1, this.currentPosition.y);
				}
				else if (adjacentTiles.containsKey(Direction.West))
				{
					prevDir = Direction.West;
					nextCoord = new Coordinates(this.currentPosition.x-1, this.currentPosition.y);
				}

			}
			//Case where robot can continue moving the direction it last moved
			else if (adjacentTiles.containsKey(prevDir))
			{
				//adjust future coordinates based on direction
			}
		}

		return null;
	}
	
	/**
	 * Return to the nearest charging station and power down
	 * @return RobotState - Exit
	 */
	protected RobotState returnHome() {
		return null;
	}
	
	/**
	 * Startup sequence for robot. Does hardware checks and populates home tile
	 * @return RobotSTate - Exit, AquireTarget
	 */
	protected RobotState startUp() {
		//Don't even need to call populateSurroundings on start up
		//Just implemented to get to AcquireTarget
		try
		{
			this.internalGraph.populateSurroundings(this.currentPosition);
		}
		catch(OutOfFloorMapBoundsException e)
		{
			e.printStackTrace();
		}
		return RobotState.AquireTarget;
	}
	
}
