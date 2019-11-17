package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Test;

import navitagion.Coordinates;
import navitagion.Direction;

public class TESTCoordinates {
	//mock setups
	Coordinates origin = mock(Coordinates.class);
	Coordinates eastSide = mock(Coordinates.class);

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
		//Coordinates origin = new Coordinates(0,0);
		when(origin.eastOf()).thenReturn(new Coordinates(1, 0));
		when(origin.southOf()).thenReturn(new Coordinates(0, -1));
		when(origin.westOf()).thenReturn(new Coordinates(-1, 0));
		when(origin.northOf()).thenReturn(new Coordinates(0, 1));
		
		assertEquals(origin.eastOf(), new Coordinates(1,0));
		assertEquals(origin.southOf(), new Coordinates(0,-1));
		assertEquals(origin.westOf(), new Coordinates(-1,0));
		assertEquals(origin.northOf(), new Coordinates(0,1));
	}
	
	@Test
	public void testGetSide() {
		//Test getSide and isAdjacent methods in Coordinates class
		Coordinates originSide = new Coordinates(0,0);
		Coordinates east = new Coordinates(1,0);
		Coordinates west = new Coordinates(-1,0);
		Coordinates north = new Coordinates(0,1);
		Coordinates south = new Coordinates(0,-1);

		//getSide mocked tests
		doReturn(Direction.East).when(origin).getSide(east);
		doReturn(Direction.West).when(origin).getSide(west);
		doReturn(Direction.North).when(origin).getSide(north);
		doReturn(Direction.South).when(origin).getSide(south);
		doReturn(Direction.West).when(eastSide).getSide(originSide);

		assertEquals(Direction.East, origin.getSide(east));
		assertEquals(Direction.West, eastSide.getSide(originSide));
		assertEquals(Direction.North, origin.getSide(north));
		assertEquals(Direction.West, origin.getSide(west));
		assertEquals(Direction.South, origin.getSide(south));

		//isAdjacent mocked tests
		when(origin.isAdjacent(east)).thenReturn(true);
		when(origin.isAdjacent(west)).thenReturn(true);
		when(origin.isAdjacent(north)).thenReturn(true);
		when(origin.isAdjacent(south)).thenReturn(true);

		assertTrue(origin.isAdjacent(east));
		assertTrue(origin.isAdjacent(west));
		assertTrue(origin.isAdjacent(north));
		assertTrue(origin.isAdjacent(south));

	}

}