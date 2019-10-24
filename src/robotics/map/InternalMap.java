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
		Coordinates next = start.northOf();
		map.put(next, new Tile(1, TileType.UNKNOWN, next.x, next.y));
		
		next = start.eastOf();
		map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y));
		
		next = start.southOf();
		map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y));
		
		next = start.westOf();
		map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y));

	}
	
	public void updateCollision(Direction direc, Coordinates loc) throws IllegalArgumentException{
		
	}
	
	public void updateTerrain (Coordinates loc, TileType terrain) throws IllegalArgumentException{
		
	}
	
	public double moveCost(Coordinates start, Coordinates end) throws IllegalArgumentException{
		return 0;
	}
	
	private void populateAround(Coordinates start) {
		Coordinates next = start.northOf();
		if(!map.containsKey(next)) {
			map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y));
		}
		
		next = start.eastOf();
		if(!map.containsKey(next)) {
			map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y));
		}
		
		next = start.southOf();
		if(!map.containsKey(next)) {
			map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y));
		}
		
		next = start.westOf();
		if(!map.containsKey(next)) {
			map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y));
		}
	}
}










