package robotics.Mocks;

import logging.IActivityLog;

public class MockActivityLog implements IActivityLog{

	public MockActivityLog(){}
	
	@Override
	public void update(String message) {
		return;
	}

}
