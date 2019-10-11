package robotics;

import logging.IActivityLog;
import navitagion.Coordinates;
import navitagion.Direction;
import sensors.ISensorPackage;

public class RobotSimulation {

	IActivityLog log;
	ISensorPackage sensors;
	SweeperHardware hardware;
	Coordinates coord;
	Direction dir = Direction.North;
	Coordinates [] stations = new Coordinates[10];//TODO: need a better way to keep track of power stations.
	
	
	//Constructor
	public RobotSimulation(IActivityLog log, ISensorPackage sensors, Coordinates coord, Coordinates[] stations) {
		super();
		this.log = log;
		this.sensors = sensors;
		this.coord = coord;
		this.stations = stations;
		
		hardware = new SweeperHardware(150, 250);
	}



	public void run() {
		
		
		while (true) {
			
			//TODO: detect current batter level & handle when critical
			
			StringBuilder message;
			
			//Note if current tile is dirty
			if (sensors.dirtDetector(coord)) {
				//Log result
				message = new StringBuilder("Dirt at Location (").append(coord.x).append(", ").append(coord.y).append(") - POSITIVE");
				System.out.println(message.toString());
				log.update(message.toString());
				
				//TODO: log dirt flag on map
				
				sensors.cleanTile(coord);
				
				//clean 1 unit of dirt & check capacity
				if (hardware.incrimentDust(1)) {
					log.update("Dirt capacity reached - Powering Down");
					return;
				}
				
				hardware.incrementBattery(-1);
				
				continue;
			} 
			else {
				//Log result
				message = new StringBuilder("Dirt at Location (").append(coord.x).append(", ").append(coord.y).append(") - NEGATIVE");
				System.out.println(message.toString());
				log.update(message.toString());
				
				//TODO: log dirt flag on map

			}
			
			
			//TODO: acquire location of new candidate tile. move one unit in current direction until implemented
			switch (dir) {
			case North:
				if (!sensors.collisionAbove(coord))	
					coord.y++;
				else dir = Direction.East;
				break;
			case East:
				if (!sensors.collisionRight(coord))	
					coord.x++;
				else dir = Direction.South;
			case South:
				if (!sensors.collisionBelow(coord))
					coord.y--;
				else dir = Direction.West;
			case West:
				if (!sensors.collisionLeft(coord))
					coord.x--;
				else dir = Direction.North;
			default:
				break;
			}
			
			hardware.incrementBattery(-3);
			message = new StringBuilder("Robot moved to Location (").append(coord.x).append(", ").append(coord.y).append(") - 3 power spent");
			System.out.println(message.toString());
			log.update(message.toString());
			
		}
		
		
	}
	
}
