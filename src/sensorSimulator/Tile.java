package sensorSimulator;
import navitagion.Coordinates;
import navitagion.Direction;

public class Tile{

    private TileType type;
    private int dirtVal;
    private Coordinates coords;
    private TileSide northSide;
    private TileSide southSide;
    private TileSide eastSide;
    private TileSide westSide;

    //Constructor
    //Could change to take in Coordinates instead of position in int
    public Tile(int dirt, TileType tType, int xPos, int yPos)
    {
        this.type = tType;
        this.dirtVal = dirt;

        this.coords = new Coordinates(xPos, yPos);

        northSide = null;
        southSide = null;
        eastSide = null;
        westSide = null;
    }

    public Tile(int dirt, TileType tType, int xPos, int yPos, TileSide nS, TileSide sS, TileSide eS, TileSide wS)
    {
        this.type = tType;
        this.dirtVal = dirt;

        this.coords = new Coordinates(xPos, yPos);

        northSide = nS;
        southSide = sS;
        eastSide = eS;
        westSide = wS;
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

    public TileSide getNorthSide()
    {
        return northSide;
    }

    public TileSide getSouthSide()
    {
        return southSide;
    }

    public TileSide getEasSide()
    {
        return eastSide;
    }

    public TileSide getWestSide()
    {
        return westSide;
    }

    public boolean hasObstacle(Direction d)
    {
        if (d == Direction.North)
        {
            if (northSide == TileSide.WALL || northSide == TileSide.DOOR_CLOSED)
            {
                return true;
            }
        }
        if (d == Direction.South)
        {
            if (southSide == TileSide.WALL || southSide == TileSide.DOOR_CLOSED)
            {
                return true;
            }
        }
        if (d == Direction.East)
        {
            if (eastSide == TileSide.WALL || eastSide == TileSide.DOOR_CLOSED)
            {
                return true;
            }
        }
        if (d == Direction.West)
        {
            if (westSide == TileSide.WALL || westSide == TileSide.DOOR_CLOSED)
            {
                return true;
            }
        }

        return false;
    }
}