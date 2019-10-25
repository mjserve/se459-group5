package robotics.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import navitagion.Coordinates;
import navitagion.Direction;

public class InternalPath {
	private Stack<Coordinates> path;
	public InternalPath() {
		this.path = new Stack<Coordinates>();
	}
	
	public void push(Coordinates k){
		this.path.push(k);
	}
	
	public Coordinates pop(Coordinates k) {
		return this.path.pop();
	}
	
	public Coordinates peek(Coordinates k) {
		return this.path.peek();
	}
	
	public Stack<Coordinates> getPath(){
		return path;
	}
	
	public List<Direction> history() {
		ArrayList<Direction> rev = new ArrayList<Direction>();
		
		while(!path.empty()) {
			Coordinates current = path.pop();
			Coordinates prev = path.peek();
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
		return rev;
		
	}
}
