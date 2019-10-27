package robotics.map;
import navitagion.Direction;

public class TileEdge {
	//Cost of traversing this edge
	private double cost;

	private boolean edgeVisited;
	//This the TileVertex this is connected to.
	private TileVertex nextVertex;
	
	//Initializes this TileEdge, visited is False.
	public TileEdge(double weight) {
		this.cost = weight;
		edgeVisited = false;
		nextVertex = null;
	}
	
	public TileEdge(double weight, TileVertex newVert) {
		this.cost = weight;
		edgeVisited = true;
		nextVertex = newVert;
	}
	
	public double getEdgeCost() {
		return cost;
	}
	
	public void setEdgecost(double newCost) {
		this.cost = newCost;
	}
	
	public void setNextVertex(TileVertex newVertex) {
		this.nextVertex =  newVertex;
	}
	
	public void visit() {
		edgeVisited=true;
	}
	
	public TileVertex getNextVertex() {
		return edgeVisited? nextVertex:null;
	}
	
	public boolean isVisited() {
		return edgeVisited;
	}
	
}
