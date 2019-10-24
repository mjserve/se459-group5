package robotics.map;
import java.util.HashMap;
import java.util.Map;
import navitagion.Coordinates;
import navitagion.Direction;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensorSimulator.Tile;
import sensorSimulator.TileType;
import sensors.ISensorPackage;

public class InternalMap {
	
	private Map<Coordinates, Tile> map;
	
	public InternalMap(Coordinates start, ISensorPackage query) throws OutOfFloorMapBoundsException{
		map = new HashMap <Coordinates, Tile>();
		Tile tile;
		
		//start tile
		if (query.dirtDetector(start)) {
			tile = new Tile(1,query.terrainType(start), start.x, start.y);
		}
		else {
			tile = new Tile(0,query.terrainType(start), start.x, start.y);
		}
		
		map.put(start, tile);
		
		//Surrounding tiles
		if(!query.collisionNorth(start)) {
			Coordinates north = start.northOf();
			map.put(north, new Tile(1, TileType.UNKNOWN, north.x, north.y));
		}
		
		if(!query.collisionEast(start)) {
			Coordinates east = start.eastOf();
			map.put(east,  new Tile(1, TileType.UNKNOWN, east.x, east.y));
		}
		
		if(!query.collisionSouth(start)) {
			Coordinates south = start.southOf();
			map.put(south,  new Tile(1, TileType.UNKNOWN, south.x, south.y));
		}
		
		if(!query.collisionWest(start)) {
			Coordinates west = start.westOf();
			map.put(west,  new Tile(1, TileType.UNKNOWN, west.x, west.y));
		}
	}
	
	public void updateCollision(Direction direc, Coordinates loc) throws IllegalArgumentException{
		
	}
	
	public void updateTerrain (Coordinates loc, TileType terrain) throws IllegalArgumentException{
		
	}
	
	public double moveCost(Coordinates start, Coordinates end) throws IllegalArgumentException{
		return 0;
	}
	
	private void populateAround(Coordinates start) {
		
	}
}
