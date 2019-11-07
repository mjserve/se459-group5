package robotics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
	final static int POWERCAP = 250;
	
	private RobotState state = RobotState.Startup;

	protected IActivityLog log;
	protected ISensorPackage sensors;
	protected SweeperHardware hardware;
	protected Coordinates coord;
	protected Coordinates nextCoord;
	protected Direction dir = Direction.North;
	protected Direction prevDir;
	protected List <Coordinates> stations;
	protected TileGraph internalGraph;
	protected HashMap<Direction, Coordinates> adjacentTiles;

	//Constructor
	public RobotSimulation(IActivityLog log, ISensorPackage sensors, Coordinates start, Coordinates[] stations) {
		super();
		this.log = log;
		this.sensors = sensors;
		this.coord = start.clone();
		
		this.stations = new LinkedList <Coordinates> ();
		
		for (int i = 0; i < stations.length; i++) {
			this.stations.add(stations[i]);
		}

		this.hardware = new SweeperHardware(DUSTCAP, POWERCAP);
	}
	
	public RobotSimulation(IActivityLog log, ISensorPackage sensors, Coordinates start) {
		super();
		this.log = log;
		this.sensors = sensors;
		this.coord = start.clone();
		
		this.stations = new LinkedList <Coordinates> ();
		
		this.stations.add(start);
		
		this.hardware = new SweeperHardware(DUSTCAP, POWERCAP);

		try
		{
			this.internalGraph = new TileGraph(coord, sensors);
		}
		catch(OutOfFloorMapBoundsException e)
		{
			e.printStackTrace();
		}

		adjacentTiles = new HashMap<Direction, Coordinates>();
	}

	
	/**
	 * New behavior simulator that utilizing changing states to manage decision flow
	 * @return
	 */
	public int stateRun() {
	
		while (state != RobotState.Exit) {
			switch (state) {
			case AquireTarget:
				state = aquiringTarget();
				break;
			case CleanDestination:
				state = cleanDestination();
				break;
			case MoveToTile:
				state = moveToTile();
				break;
			case ReturnHome:
				state = returnHome();
				break;
			case ReturnToCharger:
				state = returnToCharge();
				break;
			case Startup:
				state = startUp();
				break;
			default:
				throw new IllegalArgumentException("Unknown State Reached");
			}
		}
		
		return 0;
	}

	
	/**
	 * Old Behavior pattern that changes directions only when collsiions are detected. 
	 * Will be obsolete once stateRun()
	 * No pathfinding enabled
	 * @return return code
	 */
	public int run() {
		
		while (true) {
			StringBuilder message;
			
			//TODO: detect current batter level & handle when critical

			if (hardware.batteryCritical(3)) {	
				message = new StringBuilder("Battery levels critical (" + hardware.getBattery()+ ") - Shutting Down");
				log.update(message.toString());
				return 1;
			}
			
			//Note if current tile is dirty
			if (sensors.dirtDetector(coord)) {
				//Log result
				message = new StringBuilder("Dirt at Location (").append(coord.x).append(", ").append(coord.y).append(") - POSITIVE");
				log.update(message.toString());
				
				//TODO: log dirt flag on personal map
				
				log.update("1 unit of dust cleaned");
				sensors.cleanTile(coord);
				
				//clean 1 unit of dirt & check capacity
				if (hardware.incrimentDust(1)) {
					log.update("Dirt capacity reached - Powering Down");
					return 2;
				}
				hardware.incrimentBattery(-1);
				
				continue;
			} 
			else {
				//Log result
				message = new StringBuilder("Dirt at Location (").append(coord.x).append(", ").append(coord.y).append(") - NEGATIVE");
				log.update(message.toString());
				
				//TODO: log dirt flag on personal map

			}
			
			
			//TODO: acquire location of new candidate tile. move one unit in current direction until implemented, rotate on collision
			switch (dir) {
			case North:
				if (!sensors.collisionNorth(coord))	
					coord.y++;
				else {
					dir = Direction.East;
					log.update("Collision Detected... Changing Direction (East)");
				}
				break;
			case East:
				if (!sensors.collisionEast(coord))	
					coord.x++;
				else {
					dir = Direction.South;
					log.update("Collision Detected... Changing Direction (South)");
				}
				break;
			case South:
				if (!sensors.collisionSouth(coord))
					coord.y--;
				else {
					dir = Direction.West;
					log.update("Collision Detected... Changing Direction (West)");
				}
				break;
			case West:
				if (!sensors.collisionWest(coord))
					coord.x--;
				else {
					dir = Direction.North;
					log.update("Collision Detected... Changing Direction (North)");
				}
				break;
			default:
				break;
			}
			
			hardware.incrimentBattery(-3);
			message = new StringBuilder("Robot moved to Location (" + coord.x +  ", " + coord.y + ") - 3 power spent");
			log.update(message.toString());
			
		}
		
	}
	
	/**
	 * move along a provided path
	 * @param path
	 * @return true - successfully walked path, false - issue encountered walking path
	 * @throws Exception"Invalid inputs"
	 */
	protected boolean moveOnPath(InternalPath path) throws Exception{
		
		//Validate Path
		if (path.isEmpty()) 
			throw new Exception("Provided Interal Path object contains no instructions");
		if (!path.peek().equals(coord))
			throw new Exception("Path does not start on the current tile");
		
		//Move on path feeling for walls & dirt
		ListIterator<Direction> instructions = path.dirHistory().listIterator();
		
		if(sensors.dirtDetector(coord)) {
			//TODO: switch to cleaning mode until tile is clean then re assess power situation.
		}
		
		while (instructions.hasNext()) {
			Direction next = instructions.next();
			
			switch (next) {
			case North:
				if (sensors.collisionNorth(coord)) {
					//map.updateCollision(Direction.North, coord);
					return false;
				}
				coord.y++;
				break;
			case East:
				if (sensors.collisionEast(coord)) {
					//map.updateCollision(Direction.East, coord);
					return false;
				}
				coord.x++;
				break;
			case South:
				if (sensors.collisionSouth(coord)) {
					//map.updateCollision(Direction.South, coord);
					return false;
				}
				coord.y--;
				break;
			case West:
				if (sensors.collisionWest(coord)) {
					//map.updateCollision(Direction.West, coord);
					return false;
				}
				coord.x--;
				break;
			default:
				throw new IllegalArgumentException("Illegal argument given in instructions");		
			}
			
		}
		
		//Successful execution
		return true;
	}
	
	/**
	 * Move to the next tile while cleaning
	 * @return RobotState - CleanDestination, AquireTarget, ReturnHome, 
	 */
	protected RobotState moveToTile() {
		return null;
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
			if (this.internalGraph.getUnknownCoordinates().get(i).x == this.coord.x)
			{
				if (this.internalGraph.getUnknownCoordinates().get(i).y == (this.coord.y+1))
				{
					adjacentTiles.put(Direction.North, this.internalGraph.getUnknownCoordinates().get(i));
				}
				if (this.internalGraph.getUnknownCoordinates().get(i).y == (this.coord.y-1))
				{
					adjacentTiles.put(Direction.South, this.internalGraph.getUnknownCoordinates().get(i));
				}
			}
			if (this.internalGraph.getUnknownCoordinates().get(i).y == this.coord.y)
			{
				if (this.internalGraph.getUnknownCoordinates().get(i).x == (this.coord.x+1))
				{
					adjacentTiles.put(Direction.East, this.internalGraph.getUnknownCoordinates().get(i));
				}
				if (this.internalGraph.getUnknownCoordinates().get(i).x == (this.coord.x-1))
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
					nextCoord = new Coordinates(this.coord.x, this.coord.y+1);
				}
				else if (adjacentTiles.containsKey(Direction.South))
				{
					prevDir = Direction.South;
					nextCoord = new Coordinates(this.coord.x, this.coord.y-1);
				}
				else if (adjacentTiles.containsKey(Direction.East))
				{
					prevDir = Direction.East;
					nextCoord = new Coordinates(this.coord.x+1, this.coord.y);
				}
				else if (adjacentTiles.containsKey(Direction.West))
				{
					prevDir = Direction.West;
					nextCoord = new Coordinates(this.coord.x-1, this.coord.y);
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
			this.internalGraph.populateSurroundings(this.coord);
		}
		catch(OutOfFloorMapBoundsException e)
		{
			e.printStackTrace();
		}
		return RobotState.AquireTarget;
	}
	
	
	
}
