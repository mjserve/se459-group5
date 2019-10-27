package robotics;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import logging.IActivityLog;
import navitagion.Coordinates;
import navitagion.Direction;
import robotics.map.InternalMap;
import robotics.map.InternalPath;
import sensors.ISensorPackage;

public class RobotSimulation {

	final static int DUSTCAP = 150;
	final static int POWERCAP = 250;
	
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
	
	//Clean at the current tile until battery reserve or dust capacity is critical. return code indicates how to proceed when returning
	// 0 = Cleaning successful, 1 = low power , 2 = dust capacity reached
	protected int cleaningLoop(int allowance) {

		while (sensors.dirtDetector(coord)) {
			if (allowance <= 0) return 1;
			else {
				//clean 1 unit of dirt & check capacity
				if (hardware.incrimentDust(1)) {
					log.update("Dirt capacity reached - Powering Down");
					return 2;
				}
				
				log.update("1 unit of dust cleaned");
				sensors.cleanTile(coord);
			}
		}
		
		return 0;
	}
	
	//move along a provided path returns true if pathing is successful returns false if issue is encountered
	protected boolean moveOnPath(InternalPath path) throws Exception{
		
		//Validate Path
		if (path.isEmpty()) 
			throw new Exception("Provided Interal Path object contains no instructions");
		if (path.peek().equals(coord))
			throw new Exception("Path starts on the current tile");
		if (!coord.adjacentTo(path.peek()))
			throw new Exception("Path does not connect to current tile");
		
		//Move on path feeling for walls & dirt
		ListIterator<Direction> instructions = path.history().listIterator();
		
		if(sensors.dirtDetector(coord)) {
			//TODO: switch to cleaning mode until tile is clean then re assess power situation.
		}
		
		while (instructions.hasNext()) {
			Direction next = instructions.next();
			
			switch (next) {
			case North:
				if (sensors.collisionNorth(coord)) {
					map.updateCollision(Direction.North, coord);
					return false;
				}
				coord.y++;
				break;
			case East:
				if (sensors.collisionEast(coord)) {
					map.updateCollision(Direction.East, coord);
					return false;
				}
				coord.x++;
				break;
			case South:
				if (sensors.collisionSouth(coord)) {
					map.updateCollision(Direction.South, coord);
					return false;
				}
				coord.y--;
				break;
			case West:
				if (sensors.collisionWest(coord)) {
					map.updateCollision(Direction.West, coord);
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
	
}
