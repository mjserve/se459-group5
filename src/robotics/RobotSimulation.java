package robotics;

import java.util.HashMap;
import java.util.Iterator;
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
	protected Coordinates currentPosition;
	protected LinkedList <Coordinates> stations;
	protected TileGraph internalGraph;
	protected HashMap<Direction, Coordinates> adjacentTiles;
	protected InternalPath path;
	
	
	//Constructor
	public RobotSimulation(IActivityLog log, ISensorPackage sensors, Coordinates start, Coordinates[] stations) {
		super();
		this.log = log;
		this.sensors = sensors;
		this.currentPosition = start.clone();
		
		this.stations = new LinkedList <Coordinates> ();
		
		for (int i = 0; i < stations.length; i++) {
			this.stations.add(stations[i]);
		}
		this.hardware = new SweeperHardware(DUSTCAP, POWERCAP);
		
		try {
			//Initialize TileGraph
			this.internalGraph = new TileGraph(start, sensors);
		} catch (OutOfFloorMapBoundsException e) {
			e.printStackTrace();
		}
	}
	
	public RobotSimulation(IActivityLog log, ISensorPackage sensors, Coordinates start) {
		super();
		this.log = log;
		this.sensors = sensors;
		this.currentPosition = start.clone();
		
		this.stations = new LinkedList <Coordinates> ();
		
		this.stations.add(start);
		
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
	public int run() throws OutOfFloorMapBoundsException {
		boolean running = true;
		boolean needToCharge = false;
		Coordinates target = currentPosition;
		while(running) {
			//Check if there are unknown tiles:

			if(internalGraph.hasUnknownCoordinates()) {
				target = internalGraph.getClosestUnknown(currentPosition);
			} else if(internalGraph.hasDirtyTiles()) {
				target = internalGraph.getClosestDirty(currentPosition);
			} else {
				running = false;
			}
			
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
				internalGraph.pathTo(currentPosition, target);			//This can get the path
				internalGraph.populateSurroundings(currentPosition);	//This is a necessary step!
			}
		}
		return 1; //This needs to be changed
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
		if (!path.peek().equals(currentPosition))
			throw new Exception("Path does not start on the current tile");
		
		//Move on path feeling for walls & dirt
		ListIterator<Direction> instructions = path.dirHistory().listIterator();
		
		if(sensors.dirtDetector(currentPosition)) {
			//TODO: switch to cleaning mode until tile is clean then re assess power situation.
		}
		
		while (instructions.hasNext()) {
			Direction next = instructions.next();
			
			switch (next) {
			case North:
				if (sensors.collisionNorth(currentPosition)) {
					//map.updateCollision(Direction.North, coord);
					return false;
				}
				currentPosition.y++;
				break;
			case East:
				if (sensors.collisionEast(currentPosition)) {
					//map.updateCollision(Direction.East, coord);
					return false;
				}
				currentPosition.x++;
				break;
			case South:
				if (sensors.collisionSouth(currentPosition)) {
					//map.updateCollision(Direction.South, coord);
					return false;
				}
				currentPosition.y--;
				break;
			case West:
				if (sensors.collisionWest(currentPosition)) {
					//map.updateCollision(Direction.West, coord);
					return false;
				}
				currentPosition.x--;
				break;
			default:
				throw new IllegalArgumentException("Illegal argument given in instructions");		
			}
			
		}
		
		//Successful execution
		return true;
	}
	
}
