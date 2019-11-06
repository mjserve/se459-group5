package robotics;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import logging.IActivityLog;
import navitagion.Coordinates;
import navitagion.Direction;
import robotics.map.InternalPath;
import sensors.ISensorPackage;

public class RobotSimulation {

	final static int DUSTCAP = 150;
	final static int POWERCAP = 250;
	
	private RobotState state = RobotState.Startup;
	
	protected IActivityLog log;
	protected ISensorPackage sensors;
	protected SweeperHardware hardware;
	protected Coordinates coord;
	protected Direction dir = Direction.North;
	protected List <Coordinates> stations;
	
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
	protected RobotState aquiringTarget(){
		
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
		return null;
	}
	
	
	
}
