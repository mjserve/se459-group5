package sensors;

import navitagion.Coordinates;
import sensorSimulator.TileType;

public interface ISensorPackage {

	//Detect obstacle to the left of the given coordinates
	boolean collisionLeft (Coordinates coord);
	
	//Detect obstacle above the given coordinates
	boolean collisionAbove(Coordinates coord);
	
	//Detect obstacle to the right of the given coordinates
	boolean collisionRight(Coordinates coord);
	
	//Detect obstacle below the given coordinates
	boolean collisionBelow(Coordinates coord);
	
	//Detect if there is dirt in the given tile.
	boolean dirtDetector(Coordinates coord);
	
	//Detect current tile's terrain type as a movement cost
	int terrainType(Coordinates coord);
	
	//Detect charging Station(s) within 2 units
	Coordinates [] stationInRange(Coordinates coord);
	
	//Inform the sensor's Map that the current tile has been cleaned for 1 unit of dirt
	void cleanTile(Coordinates coord);
	
}
