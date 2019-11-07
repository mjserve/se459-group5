package WorldSimulator;

public enum TileType {
    OBSTACLE(-1),
    HIGH(3),
    LOW(2),
    BARE(1),
    UNKNOWN(3);

    
    private int cost;
    
    private TileType(int cost) {
    	this.cost = cost;
    }
    
    public int getCost() {
    	return cost;
    }
}
