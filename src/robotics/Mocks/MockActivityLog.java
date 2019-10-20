package robotics.Mocks;

import logging.IActivityLog;

public class MockActivityLog implements IActivityLog{

	@Override
	public void update(String message) {
		System.out.println(message);
		return;
	}

}
