package robotics.map;
import java.util.HashMap;
import java.util.Map;
import navitagion.Coordinates;
import navitagion.Direction;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensorSimulator.Tile;
import sensorSimulator.TileSide;
import sensorSimulator.TileType;
import sensors.ISensorPackage;

public class InternalMap {
	
	private Map<Coordinates, Tile> map;
	
	public InternalMap(Coordinates start, ISensorPackage query) throws OutOfFloorMapBoundsException{
		map = new HashMap <Coordinates, Tile>();
		Tile tile;
		
		//start tile
		if (query.dirtDetector(start)) {
			tile = new Tile(1,query.terrainType(start), start.x, start.y, 
					TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
		}
		else {
			tile = new Tile(0,query.terrainType(start), start.x, start.y, 
					TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
		}
		
		map.put(start, tile);
		
		//Surrounding tiles
		Coordinates next = start.northOf();
		map.put(next, new Tile(1, TileType.UNKNOWN, next.x, next.y, 
				TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE));
		
		next = start.eastOf();
		map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y, 
				TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE));
		
		next = start.southOf();
		map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y, 
				TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE));
		
		next = start.westOf();
		map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y, 
				TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE));

	}
	
	public void addTile(Coordinates target, ISensorPackage query) throws IllegalArgumentException, OutOfFloorMapBoundsException{
		if (map.containsKey(target) && map.get(target).getTypeTile() != TileType.UNKNOWN) 
			throw new IllegalArgumentException("Tile already in map");
		
		Tile tile;
		
		//start tile
		if (query.dirtDetector(target))
			tile = new Tile(1,query.terrainType(target), target.x, target.y);
		else 
			tile = new Tile(0,query.terrainType(target), target.x, target.y);
		
		map.put(target, tile);
		
		populateAround(target);
	}
	
	public void updateCollision(Direction dir, Coordinates loc) throws OutOfFloorMapBoundsException{
		
		if(!map.containsKey(loc))
			throw new OutOfFloorMapBoundsException("Tile not contained in existing map");
		
		switch(dir) {
		case North:
			map.get(loc).setNorthSide(TileSide.WALL);
			break;
		case East:
			map.get(loc).setEastSide(TileSide.WALL);
			break;
		case South:
			map.get(loc).setSouthSide(TileSide.WALL);
			break;
		case West:
			map.get(loc).setWestSide(TileSide.WALL);
			break;
		}
	}
	
	public void updateTerrain (Coordinates loc, TileType terrain) throws OutOfFloorMapBoundsException{
		
		if(!map.containsKey(loc))
			throw new OutOfFloorMapBoundsException("Tile not contained in existing map");
		
		map.get(loc).setType(terrain);;
	}
	
	public Tile getTile(Coordinates loc) throws OutOfFloorMapBoundsException{
		
		if(!map.containsKey(loc)) throw new OutOfFloorMapBoundsException("Coordinates not included in map");
		
		return map.get(loc);
		
	}
	
	public double moveCost(Coordinates start, Coordinates end) throws IllegalArgumentException{
		//Sanitizing
		if (start.equals(end)) 
			throw new IllegalArgumentException("Cannot move between the same start and end tile");
		if(Math.abs(start.x - end.x) != 1 || Math.abs(start.y - end.y) != 1) 
			throw new IllegalArgumentException("Tiles are too far apart");
		return 0;
	}
	
	public boolean tileExists(Coordinates target) {
		return map.containsKey(target);
	}
	
	private void populateAround(Coordinates start) {
		Coordinates next = start.northOf();
		if(!map.containsKey(next)) {
			map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y
					, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE));
		}
		
		next = start.eastOf();
		if(!map.containsKey(next)) {
			map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y
					, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE));
		}
		
		next = start.southOf();
		if(!map.containsKey(next)) {
			map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y
					, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE));
		}
		
		next = start.westOf();
		if(!map.containsKey(next)) {
			map.put(next,  new Tile(1, TileType.UNKNOWN, next.x, next.y
					, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE));
		}
	}
}










