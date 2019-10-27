package robotics.map;

import java.util.Stack;

import navitagion.Coordinates;
import navitagion.Direction;

public class InternalPath {
	private Stack<Coordinates> path;
	private double totalCost;
	public InternalPath() {
		this.path = new Stack<Coordinates>();
	}
	
	public void push(Coordinates k){
		this.path.push(k);
	}
	
	public Coordinates pop(Coordinates k) {
		return this.path.pop();
	}
	
	public void setTotalCost(double cost) {
		this.totalCost = cost;
	}
	
	/**
	 * Returns to total energy cost to traverse from point a to point b
	 * <b> WITHOUT calculating vacuum costs </b>
	 * <b> *NOTE* </b> Will NOT calculate new costs if more coordinates are added
	 * @return Total cost to traverse from point A to point B.
	 */
	public double getTotalCost() {
		return this.totalCost;
	}
	
	
	/**
	 * Returns an ordered list of coordinates in the way that they were traversed.
	 * @return Returns a stack of coordinates
	 */
	public Stack<Coordinates> getPath(){
		Stack<Coordinates> revPath = new Stack<Coordinates>();
		path.forEach(
				(coordinate) -> {
					revPath.push(coordinate);
					}
				);
		return revPath;
	}
	/**
	 * Returns an ordered list of cardinal directions 
	 * (North, East, South, West) in the order they were traversed.
	 * @return A Stack of cardinal directions
	 */
	public Stack<Direction> dirHistory() {
		Stack<Direction> rev = new Stack<Direction>();
		Stack<Coordinates> pathCpy = (Stack<Coordinates>) path.clone();
		while(!pathCpy.empty()) {
			Coordinates current = pathCpy.pop();
			if(!pathCpy.isEmpty()) {
				Coordinates prev = pathCpy.peek();
				Direction dir = null;
				//Assuming no diagonals
				//North or south
				if(current.x == prev.x) {
					dir = current.y < prev.y ? Direction.North:Direction.South;
				} 
				//Easth or west
				else if (current.y == prev.y){
					dir = current.x < prev.x ? Direction.East: Direction.West;
				}
				rev.add(dir);
			}
		}
		return rev;
		
	}
}