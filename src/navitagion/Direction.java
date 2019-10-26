package navitagion;

public enum Direction {
	North ("North"),
	East ("East"),
	South ("South"),
	West ("West");
	
	private String name;
	private Direction(String n) {
		this.name = n;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
