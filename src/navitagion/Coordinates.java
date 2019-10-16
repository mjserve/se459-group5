package navitagion;

public class Coordinates {

	public int x;
	public int y;
	
	public boolean equals(Coordinates input) {
		if (	input.x == this.x && 
				input.y == this.y)
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return x * 97 + y;
	}
	
}
