package robotics.Mocks.copy;

import logging.IActivityLog;

public class MockActivityLog implements IActivityLog{

	public MockActivityLog(){}
	
	@Override
	public void update(String message) {
		System.out.println(message);
		return;
	}

}
