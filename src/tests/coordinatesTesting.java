package tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
		assertThat(coord1,  is(not(coord2)));
		
		//Testing Equality
		assertEquals(coord1, coord3, "Values should  match");
		
		assertThat(coord1.equals(null), is(false));
		assertThat(coord1.equals(coord1), is(true));
		assertThat(coord1.equals(coord4), is(true));
		assertThat(coord1.equals(5), is(false));
		
		assertThat(coord1.equals(coord5), is(false));
		assertThat(coord1.equals(coord6), is(false));
		
		assertThat(coord1.equals(coord1.clone()), is(true));
		
		assertThat(coord1.toString(), is("(5, 6)"));
	}

}