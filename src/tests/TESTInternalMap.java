package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import navitagion.Coordinates;
import robotics.Mocks.MockSensors;
import robotics.map.InternalMap;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensorSimulator.TileType;
import sensors.ISensorPackage;

public class TESTInternalMap {

	@Test
	void MapConstruction (){

		Coordinates target = new Coordinates(0,0);
		ISensorPackage sensors = new MockSensors();
		try {
			InternalMap map = new InternalMap(target, sensors);
			
			assertTrue(map.tileExists(target));
			assertTrue(map.tileExists(target.northOf()));
			assertTrue(map.tileExists(target.eastOf()));
			assertTrue(map.tileExists(target.southOf()));
			assertTrue(map.tileExists(target.westOf()));
			
		} catch (Exception e) {
			fail("Exception on creation using Coordinates (0,0)");
		}
		
	}
	
	@Test
	void PopulateTile () {
		Coordinates target = new Coordinates(0,0);
		ISensorPackage sensors = new MockSensors();
		try {
			InternalMap map = new InternalMap(target, sensors);
			
			//Isolated Tile
			target = new Coordinates(0,4);
			map.addTile(target, sensors);
			
			assertTrue(map.tileExists(target));
			assertEquals(map.getTile(target).getTypeTile(),TileType.HIGH);
			assertTrue(map.tileExists(target.northOf()));
			assertTrue(map.tileExists(target.eastOf()));
			assertTrue(map.tileExists(target.southOf()));
			assertTrue(map.tileExists(target.westOf()));
			
			//Adjacent Tile
			target = new Coordinates(1,4);
			map.addTile(target, sensors);
			
			assertTrue(map.tileExists(target));
			assertEquals(map.getTile(target).getTypeTile(),TileType.HIGH);
			assertTrue(map.tileExists(target.northOf()));
			assertTrue(map.tileExists(target.eastOf()));
			assertTrue(map.tileExists(target.southOf()));
			assertTrue(map.tileExists(target.westOf()));
			assertEquals(map.getTile(target.westOf()).getTypeTile(),TileType.HIGH);
			
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
}
