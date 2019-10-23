package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import robotics.SweeperHardware;

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
		
		assertEquals(mockSweeper.getBattery(), 150);
		mockSweeper.setBattery(100);
		assertEquals(mockSweeper.getBattery(), 100);
		//Testing if bin is full
		assertTrue(mockSweeper.incrimentDust(255));
		
		assertEquals(mockSweeper.incrementBattery(40), 140);
		assertTrue(mockSweeper.batteryCritical(141));
		assertFalse(mockSweeper.batteryCritical(1));
		mockSweeper.setDust(100);
		assertFalse(mockSweeper.dustBinFull());
		mockSweeper.setDust(251);
		assertTrue(mockSweeper.dustBinFull());
	}

}
