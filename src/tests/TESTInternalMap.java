package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;
import navitagion.Coordinates;
import navitagion.Direction;
import robotics.Mocks.MockSensors;
import robotics.map.InternalMap;
import sensorSimulator.TileSide;
import sensorSimulator.TileType;
import sensors.ISensorPackage;

public class TESTInternalMap {

	/*
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
	public void PopulateTile () {
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
	
	@Test
	public void updateTerrain() {
		Coordinates target = new Coordinates(0,0);
		ISensorPackage sensors = new MockSensors();
		try {
			InternalMap map = new InternalMap(target, sensors);
			
			assertEquals(map.getTile(target).getTypeTile(), TileType.HIGH);
			
			map.updateTerrain(target, TileType.BARE);
			assertEquals(map.getTile(target).getTypeTile(), TileType.BARE);
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void updateCollision() {
		Coordinates target = new Coordinates(0,0);
		ISensorPackage sensors = new MockSensors();
		try {
			InternalMap map = new InternalMap(target, sensors);
			
			assertEquals(map.getTile(target).getEasSide(),TileSide.PASSABLE);
			
			map.updateCollision(Direction.East, target);
			assertEquals(map.getTile(target).getEasSide(), TileSide.WALL);
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void movementCost() {
		
		Coordinates target = new Coordinates(0,0);
		Coordinates adjacent = new Coordinates(0,1);
		ISensorPackage sensors = new MockSensors();
		try {
			InternalMap map = new InternalMap(target, sensors);
			map.addTile(adjacent, sensors);
			
			assertTrue(map.tileExists(target));
			assertTrue(map.tileExists(adjacent));
			
			assertEquals(map.moveCost(target, adjacent), 3);
			
			map.updateTerrain(adjacent, TileType.LOW);
			assertEquals(map.moveCost(target, adjacent), 2.5);
			
			
		} catch (Exception e) {
			fail(e.toString());
		}
		
	}
	*/
}

















