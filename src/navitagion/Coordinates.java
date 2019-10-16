package navitagion;

public class Coordinates {

	public int x;
	public int y;

	public Coordinates(int xPos, int yPos)
	{
		this.x = xPos;
		this.y = yPos;
	}
	
	boolean equals(Coordinates input) {
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
	
	public Coordinates clone() {
		return new Coordinates(this.x, this.y);
	}
	
	@Override
	public String toString() {
		return new StringBuilder("(").append(this.x).append(", ").append(this.y).append(")").toString();		
	}
	
}
