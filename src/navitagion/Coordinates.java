package navitagion;

public class Coordinates {

	public int x;
	public int y;
	
	
	boolean equals(Coordinates input) {
		if (	input.x == this.x && 
				input.y == this.y)
			return true;
		else
			return false;
	}
	
}
