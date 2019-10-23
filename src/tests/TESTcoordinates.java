package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import navitagion.Coordinates;

class TESTcoordinates {

	@Test
	void testEquals() {
		Coordinates coord1 = new Coordinates(5,6);
		Coordinates coord2 = new Coordinates(6,5);
		Coordinates coord3 = new Coordinates(5,6);
		
		Coordinates coord4 = coord1;
		Coordinates coord5 = new Coordinates(1,6);
		Coordinates coord6 = new Coordinates(5,2);

		
		//Testing failures
		//assertThat(coord1,  is(not(coord2)));
		assertNotSame(coord1, coord2);
		
		//Testing Equality
		assertEquals(  coord1, coord3, "Values should  match");
		
		assertFalse(coord1.equals(null));
		assertTrue(coord1.equals(coord1));
		assertTrue(coord1.equals(coord4));
		assertFalse(coord1.equals(5));
		
		assertTrue(coord1.equals(coord5));
		assertTrue(coord1.equals(coord6));
		
		assertTrue(coord1.equals(coord1.clone()));
		
		assertEquals(coord1.toString(), "(5, 6)");
	}

}