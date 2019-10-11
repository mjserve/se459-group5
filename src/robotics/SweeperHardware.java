package robotics;

import navitagion.Coordinates;

public class SweeperHardware {

	public final int DUSTCAP = 50;
	public final int BATTERYCAP = 150;
	
	private int battery;
	private Coordinates coord;
	private int dust;
	
	
	//Getters & Setters
	public int getBattery() {
		return battery;
	}
	
	public void setBattery(int battery) {
		this.battery = battery;
	}

	public Coordinates getCoord() {
		return coord;
	}
	
	public int getDust() {
		return dust;
	}
	
	//Other Methods
	
	//Increment the battery level & return the new value
	public int incrementBattery(int delta) {
		return this.battery += delta;
	}
	
	//Increment the dust level and return if the dust bin is now full
	public boolean incrimentDust(int delta) {
		
		this.dust += delta;
		
		return (dust >= DUSTCAP);
		
	}
}
























