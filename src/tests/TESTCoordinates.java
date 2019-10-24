package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import navitagion.Coordinates;

public class TESTCoordinates {

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals() {
		Coordinates coord1 = new Coordinates(5,6);
		Coordinates coord2 = new Coordinates(6,5);
		Coordinates coord3 = new Coordinates(5,6);
		
		Coordinates coord4 = coord1;
		Coordinates coord5 = new Coordinates(1,6);
		//Testing failures
		//assertThat(coord1,  is(not(coord2)));
		assertNotSame(coord1, coord2);
		
		//Testing Equality
		assertEquals("Values should  match", coord1, coord3);
		
		assertFalse(coord1.equals(null));
		assertTrue(coord1.equals(coord1));
		assertTrue(coord1.equals(coord4));
		assertFalse(coord1.equals(5));
		
		assertFalse(coord1.equals(coord5));
		assertTrue(coord1.equals(coord3));
		
		assertTrue(coord1.equals(coord1.clone()));
		
		assertEquals(coord1.toString(), "(5, 6)");
	}
	
	@Test
	public void testTranslation() {
		Coordinates origin = new Coordinates(0,0);
		
		assertEquals(origin.eastOf(), new Coordinates(1,0));
		assertEquals(origin.southOf(), new Coordinates(0,-1));
		assertEquals(origin.westOf(), new Coordinates(-1,0));
		assertEquals(origin.northOf(), new Coordinates(0,1));
	}

}