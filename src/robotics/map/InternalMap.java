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
		if (query.dirtDetector(start)) {
			tile = new Tile(1,query.terrainType(start), start.x, start.y);
		}
		else {
			tile = new Tile(0,query.terrainType(start), start.x, start.y);
		}
		
		map.put(start, tile);
	}
	
	public void updateCollision(Direction direc, Coordinates loc) throws IllegalArgumentException{
		
	}
	
	public void updateTerrain (Coordinates loc, TileType terrain) throws IllegalArgumentException{
		
	}
	
	public double moveCost(Coordinates start, Coordinates end) throws IllegalArgumentException{
		return 0;
	}
}
