package sensorSimulator;
import navitagion.Coordinates;

public class Tile{

    private TileType type;
    private int dirtVal;
    private Coordinates coords;

    //Constructor
    //Could change to take in Coordinates instead of position in int
    public Tile(int dirt, TileType tType, int xPos, int yPos)
    {
        this.type = tType;
        this.dirtVal = dirt;

        this.coords = new Coordinates(xPos, yPos);
    }

    public int getXCoordinate()
    {
        return coords.x;
    }

    public int getYCoordinate()
    {
        return coords.y;
    }

    public TileType getTypeTile()
    {
        return type;
    }

    public int getDirtVal()
    {
        return dirtVal;
    }
}