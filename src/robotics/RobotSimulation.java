package robotics;

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
	public RobotSimulation(IActivityLog log, ISensorPackage sensors, Coordinates coord, Coordinates[] stations) {
		super();
		this.log = log;
		this.sensors = sensors;
		this.coord = coord;
		
		for (int i = 0; i < stations.length; i++) {
			this.stations.add(stations[i]);
		}
		
		this.hardware = new SweeperHardware(DUSTCAP, POWERCAP);
	}



	public void run() {
		
		
		while (true) {
			
			//TODO: detect current batter level & handle when critical
			if (hardware.batteryCritical(3)) {
				log.update("Battery levels critical (" + hardware.getBattery()+ ") - Shutting Down");
				return;
			}
			
			StringBuilder message;
			
			//Note if current tile is dirty
			if (sensors.dirtDetector(coord)) {
				//Log result
				message = new StringBuilder("Dirt at Location (").append(coord.x).append(", ").append(coord.y).append(") - POSITIVE");
				System.out.println(message.toString());
				log.update(message.toString());
				
				//TODO: log dirt flag on personal map
				
				
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
				
				//TODO: log dirt flag on personal map

			}
			
			
			//TODO: acquire location of new candidate tile. move one unit in current direction until implemented, rotate on collision
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
			message = new StringBuilder("Robot moved to Location (" + coord.x +  ", " + coord.y + ") - 3 power spent");
			System.out.println(message.toString());
			log.update(message.toString());
			
		}
		
		
	}
	
}
