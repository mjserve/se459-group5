package navitagion;

public class Coordinates {

	public int x;
	public int y;

	public Coordinates(int xPos, int yPos)
	{
		this.x = xPos;
		this.y = yPos;
	}
	
	@Override
	public boolean equals(Object input) {
		if(input == null) {
			return false;
		}
		if (input == this) {
            return true;
        };
        if (input.getClass() != this.getClass()) {
            return false;
        };
        
        Coordinates obj = (Coordinates) input;
        return this.x == obj.x &&
        		this.y == obj.y;
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
