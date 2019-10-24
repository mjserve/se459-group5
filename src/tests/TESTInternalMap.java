package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import navitagion.Coordinates;
import robotics.Mocks.MockSensors;
import robotics.map.InternalMap;
import sensorSimulator.OutOfFloorMapBoundsException;
import sensors.ISensorPackage;

public class TESTInternalMap {

	@Test
	void TESTMapConstruction(){

		Coordinates target = new Coordinates(0,0);
		ISensorPackage sensors = new MockSensors();
		try {
			InternalMap map = new InternalMap(target, sensors);
			
			assertTrue(map.tileExists(target));
			assertTrue(map.tileExists(target.northOf()));
			assertTrue(map.tileExists(target.eastOf()));
			assertTrue(map.tileExists(target.southOf()));
			assertTrue(map.tileExists(target.westOf()));
			
		} catch (OutOfFloorMapBoundsException e) {
			fail("Exception on creation using Coordinates (0,0)");
		}
		
	}
}
