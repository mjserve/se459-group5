package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import robotics.SweeperHardware;

public class TESTSweeperHardware {
	//mock setup class
	SweeperHardware mockSweep = mock(SweeperHardware.class);
	@Test
	public void testIllegalHardwareValues() {
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

	//uses mockito to test methods within SweeperHardware class
	@Test
	public void testIncrementation() {
		//SweeperHardware mockSweeper = new SweeperHardware(250, 150);

		//getBattery mock test
		when(mockSweep.getBattery()).thenReturn((double) 150);
		assertEquals(mockSweep.getBattery(), 150);

		when(mockSweep.getBattery()).thenReturn((double) 100);

		assertEquals(mockSweep.getBattery(), 100);
		when(mockSweep.incrimentDust(255)).thenReturn(true);
		//Testing if bin is full
		//incrimentDust mock
		assertTrue(mockSweep.incrimentDust(255));

		when(mockSweep.incrimentBattery(40)).thenReturn((double) 140);
		assertEquals(mockSweep.incrimentBattery(40), 140);

		//batteryCritical mock
		when(mockSweep.batteryCritical(141)).thenReturn(true);
		assertTrue(mockSweep.batteryCritical(141));

		when(mockSweep.batteryCritical(1)).thenReturn(false);
		assertFalse(mockSweep.batteryCritical(1));

		mockSweep.setDust(100);

		////dustBinFull mock
		when(mockSweep.dustBinFull()).thenReturn(false);
		assertFalse(mockSweep.dustBinFull());

		doAnswer((i) -> {
			assertTrue("100".equals(i.getArgument(0)));
			return null;
		}).when(mockSweep).setBattery(anyInt());

		when(mockSweep.getDust()).thenReturn(251);
		when(mockSweep.dustBinFull()).thenReturn(true);
		assertTrue(mockSweep.dustBinFull());
	}

}
