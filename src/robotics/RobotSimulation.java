package robotics;

import java.util.LinkedList;
import java.util.List;
import logging.IActivityLog;
import navitagion.Coordinates;
import navitagion.Direction;
import sensors.ISensorPackage;

public class RobotSimulation {

	final static int DUSTCAP = 150;
	final static int POWERCAP = 250;
	
	IActivityLog log;
	ISensorPackage sensors;
	SweeperHardware hardware;
	Coordinates coord;
	Direction dir = Direction.North;
	List <Coordinates> stations;
	
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
				message = new StringBuilder("Dirt at Location ").append(coord.toString()).append(" - POSITIVE");
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
				message = new StringBuilder("Dirt at Location ").append(coord.toString()).append(" - NEGATIVE");
				log.update(message.toString());
				
				//TODO: log dirt flag on personal map

			}
			
			
			//TODO: acquire location of new candidate tile. move one unit in current direction until implemented, rotate on collision
			switch (dir) {
			case North:
				if (!sensors.collisionNorth(coord))	
					coord.y++;
				else dir = Direction.East;
				break;
			case East:
				if (!sensors.collisionEast(coord))	
					coord.x++;
				else dir = Direction.South;
			case South:
				if (!sensors.collisionSouth(coord))
					coord.y--;
				else dir = Direction.West;
			case West:
				if (!sensors.collisionWest(coord))
					coord.x--;
				else dir = Direction.North;
			default:
				break;
			}
			
			hardware.incrimentBattery(-3);
			message = new StringBuilder("Robot moved to Location ").append(coord.toString()).append(" - 3 power spent");
			log.update(message.toString());
			
		}
		
	}
	
}
