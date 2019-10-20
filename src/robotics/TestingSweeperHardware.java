package robotics;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

class TESTSweeperHardware {

	@Test
	void testIllegalHardwareValues() {
		try{
			new SweeperHardware(-5, 5);
			fail("Exception was expected for negative dust cap");
		} catch(IllegalArgumentException e) {}
		try{
			new SweeperHardware(5, -5);
			fail("Exception was expected for negative battery cap");
		} catch(IllegalArgumentException e) {}
		try{
			new SweeperHardware(-5, -5);
			fail("Exception was expected for negative values");
		} catch(IllegalArgumentException e) {}
	}
	
	@Test
	void testIncrementation() {
		SweeperHardware mockSweeper = new SweeperHardware(250, 150);
		
		assertThat(mockSweeper.getBattery(), is(150));
		mockSweeper.setBattery(100);
		assertThat(mockSweeper.getBattery(), is(100));
		//Testing if bin is full
		assertThat(mockSweeper.incrimentDust(255), is(true));
		
		assertThat(mockSweeper.incrementBattery(40), is(140));
		
		assertThat(mockSweeper.batteryCritical(141), is(true));
		assertThat(mockSweeper.batteryCritical(1), is(false));
		mockSweeper.setDust(100);
		assertThat(mockSweeper.dustBinFull(), is(false));
		mockSweeper.setDust(251);
		assertThat(mockSweeper.dustBinFull(), is(true));
	}

}
