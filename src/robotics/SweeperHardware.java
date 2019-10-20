package robotics;

public class SweeperHardware {

	public final int DUSTCAP;
	public final int BATTERYCAP;
	
	public final int BUFFER = 5;
	
	private int battery;
	private int dust;
	
	//Constructor
	SweeperHardware(int dustCap, int batteryCap){
		
		//Input Sanitizing
		if (dustCap <= 0) 		throw new IllegalArgumentException("Invalid dust capacity (" + dustCap + ")");
		if (batteryCap <= 0 ) 	throw new IllegalArgumentException("Invalid battery capacity (" + batteryCap + ")");
		
		this.DUSTCAP = dustCap;
		this.BATTERYCAP = batteryCap;
		
		this.battery = batteryCap;
		this.dust = 0;
	}
	
	//Getters & Setters
	public int getBattery() {
		return battery;
	}
	
	public void setBattery(int battery) {
		this.battery = battery;
	}
	
	public int getDust() {
		return dust;
	}
	
	public void setDust(int newDust) {
		this.dust = newDust;
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
	
	//Advises whether the current buffer settings will allow spending energy of the specified amount.
	public boolean batteryCritical(int cost) {
		if (cost + BUFFER > battery) return true;
		else return false;
	}
	
	public boolean dustBinFull() {
		if (dust < DUSTCAP)
			return false;
		else 
			return true;
	}
}
























