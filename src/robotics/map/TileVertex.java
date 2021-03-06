package robotics.map;

import java.util.ArrayList;
import java.util.List;

import navitagion.Coordinates;
import navitagion.Direction;
import WorldSimulator.TileType;

public class TileVertex {
	final double DEFAULTCOST = 3;
	public int x;
	public int y;
	boolean explored;
	Coordinates position;
	TileEdge N, E, S, W;
	TileType floor;
	
	public TileVertex(Coordinates coord) {
		this.position = coord;
		this.x = coord.x;
		this.y = coord.y;
		N = null;
		E = null;
		S = null;
		W = null;
		floor = TileType.UNKNOWN;
		explored = false;
	}
	
	public void setTileType(TileType type) {
		floor = type;
		explored = true;
	}
	
	public TileType getTileType() {
		return floor;
	}
	
	public Coordinates getCoordinates() {
		return this.position;
	}
	
	//Create a specific edge
	public void newTileEdge(Direction cardinal){
		switch(cardinal) {
		case North: N = new TileEdge(DEFAULTCOST);
		case East:  E = new TileEdge(DEFAULTCOST);
		case South: S = new TileEdge(DEFAULTCOST);
		case West:  W = new TileEdge(DEFAULTCOST);
		}
	}
	
	//Return a specific edge
	public TileEdge getTileEdge(Direction cardinal){
		switch(cardinal) {
		case North: 	return N;
		case East: 		return E;
		case South: 	return S;
		case West: 		return W;
		}
		return null;
	}
	//Set a specific edge
	public void setTileEdge(Direction cardinal, TileEdge newEdge){
		switch(cardinal) {
		case North: 	N = newEdge; break;
		case East: 		E = newEdge; break;
		case South: 	S= newEdge; break;
		case West: 		W = newEdge; break;
		}
	}
	
	//Return a specific neighbor following an edge
	public TileVertex getCardinalNeighbor(Direction cardinal) {
		switch(cardinal) {
		case North: return 	N!=null? N.getNextVertex():null;
		case East: return 	E!=null? E.getNextVertex():null;
		case South: return 	S!=null? S.getNextVertex():null;
		case West: return 	W!=null? W.getNextVertex():null;
		}
		return null;
		
	}
	

	//Return a list of neighbors, following the edge
	public List<Pair<Direction, TileVertex>> getAllNeighbors() {
		List<Pair<Direction, TileVertex>> neighbors = new ArrayList<Pair<Direction, TileVertex>>();
		if(N != null && N.getNextVertex() !=null) 
			neighbors.add(new Pair<Direction, TileVertex>(Direction.North, N.getNextVertex()));
		if(E != null && E.getNextVertex() !=null) 
			//neighbors.add(E.getNextVertex());
			neighbors.add(new Pair<Direction, TileVertex>(Direction.East, E.getNextVertex()));
		if(S != null && S.getNextVertex() !=null) 
			//neighbors.add(S.getNextVertex());
			neighbors.add(new Pair<Direction, TileVertex>(Direction.South, S.getNextVertex()));
		if(W != null && W.getNextVertex() !=null) 
			//neighbors.add(W.getNextVertex());
			neighbors.add(new Pair<Direction, TileVertex>(Direction.West, W.getNextVertex()));
		return neighbors;		
	}
	
	public List<TileVertex> getAllNeighborsVert() {
		List<TileVertex> neighbors = new ArrayList<TileVertex>();
		if(N != null && N.getNextVertex() !=null) 
			neighbors.add(N.getNextVertex());
		if(E != null && E.getNextVertex() !=null) 
			neighbors.add(E.getNextVertex());
		if(S != null && S.getNextVertex() !=null) 
			neighbors.add(S.getNextVertex());
		if(W != null && W.getNextVertex() !=null) 
			neighbors.add(W.getNextVertex());
			
		return neighbors;		
	}
	
	@Override
	public String toString() {
		return this.explored? "(" + x +"," + y + ")": "?" + x +"," + y + "?";
		
	}
	
}
