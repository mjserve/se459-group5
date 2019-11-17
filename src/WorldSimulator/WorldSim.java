package WorldSimulator;

import java.io.FileReader;
import java.util.ArrayList;

import navitagion.Coordinates;
import navitagion.Direction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class WorldSim {

    private static WorldSim instance;
    private Tile[][] loadedMap;

    private WorldSim(){

    }

    public static WorldSim getInstance(){
        if (instance == null){
            instance = new WorldSim();
        }
        return instance;
    }


    //incorporate json file here
    public void loadAltFloorPlan()
    {
        //NOTE: CHANGED ROW AND COLUMN
        int row = 6;
        int column = 2;

        //Get all tiles of map in ArrayList
        //Refactor for json input files later on
        ArrayList<Tile> listOfTiles = new ArrayList<Tile>();
        populateAltArray(listOfTiles);

        loadedMap = new Tile[row][column];

        int i = 0;
        while (i < listOfTiles.size())
        {
            loadedMap[listOfTiles.get(i).getXCoordinate()][listOfTiles.get(i).getYCoordinate()] = listOfTiles.get(i);
            i++;
        }

    }
    

    public void loadJSONFloorPlan(String fileName)
    {
        ArrayList<Tile> tileList11 = new ArrayList<Tile>();
        JSONParser parser = new JSONParser();

        try
        {
            Object obj = parser.parse(new FileReader(fileName));
            JSONArray jArr = (JSONArray) obj;

            int max_x = 0;
            int max_y = 0;
            int dirt_val;
            TileType tType;
            int xPos;
            int yPos;
            TileSide northSide;
            TileSide southSide;
            TileSide eastSide;
            TileSide westSide;

            //Create arguments to create each Tile object
            for (int i=0; i < jArr.size(); i++)
            {
                JSONObject tmpObj = (JSONObject) jArr.get(i);
                String dirt = (String) tmpObj.get("dirtVal");
                String typeOfTile = (String) tmpObj.get("TileType");
                String xPosition = (String) tmpObj.get("xPos");
                String yPosition = (String) tmpObj.get("yPos");
                String nSide = (String) tmpObj.get("northSide");
                String sSide = (String) tmpObj.get("southSide");
                String eSide = (String) tmpObj.get("eastSide");
                String wSide = (String) tmpObj.get("westSide");

                dirt_val = Integer.parseInt(dirt);
                tType = resolveTileType(typeOfTile);
                xPos = Integer.parseInt(xPosition);
                yPos = Integer.parseInt(yPosition);
                northSide = resolveTileSide(nSide);
                southSide = resolveTileSide(sSide);
                eastSide = resolveTileSide(eSide);
                westSide = resolveTileSide(wSide);

                Tile tmp = new Tile(dirt_val, tType, xPos, yPos, northSide, southSide, eastSide, westSide);
                tileList11.add(tmp);

                //Get max x and y positions to initialize Tile[][]
                if (xPos > max_x)
                {
                    max_x = xPos;
                }
                if (yPos > max_y)
                {
                    max_y = yPos;
                }

            }

            loadedMap = new Tile[max_x + 1][max_y + 1];

            //Populate the loadedMap
            for (int j=0; j < tileList11.size(); j++)
            {
                loadedMap[tileList11.get(j).getXCoordinate()][tileList11.get(j).getYCoordinate()] = tileList11.get(j);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Old floor plan loading method -> delete?

    public void loadFloorPlan()
    {
        int row = 10;
        int column = 10;

        //Get all tiles of map in ArrayList
        //Refactor for json input files later on
        ArrayList<Tile> listOfTiles = new ArrayList<Tile>();
        populateArray(listOfTiles);

        loadedMap = new Tile[row][column];

        int i = 0;
        while (i < listOfTiles.size())
        {
            loadedMap[listOfTiles.get(i).getXCoordinate()][listOfTiles.get(i).getYCoordinate()] = listOfTiles.get(i);
            i++;
        }
    }

    //Used to find if Tile at coords has any dirt present
    public boolean isTileDirty(Coordinates coords)
    {
        if (loadedMap[coords.x][coords.y].getDirtVal() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //Returns the dirt value from Tile at coords decremented by 1
    public int removeDirt(Coordinates coords)
    {
        return loadedMap[coords.x][coords.y].removeDirt();
    }


    public boolean checkForBarrier(Coordinates coords, Direction d)
    {
        return loadedMap[coords.x][coords.y].hasObstacle(d);
    }

    //Retrieves the TileType of a Tile object at the specified coordinates
    //Throws exception if coordinates are out of bounds in any way
    public TileType retrieveTileType(Coordinates c) throws OutOfFloorMapBoundsException
    {
        int x = c.x;
        int y = c.y;

        //Tests to see if x or y is out of bounds of the indices of the created map
        if ((x > loadedMap.length-1) || (x < 0))
        {
            throw new OutOfFloorMapBoundsException("Coordinates must be within the map's boundaries.");
        }
        else if ((y > loadedMap[x].length-1) || (y < 0))
        {
            throw new OutOfFloorMapBoundsException("Coordinates must be within the map's boundaries.");
        }
        //Tests if coordinates are within created map's indices BUT no Tile is present at this location
        else if ((loadedMap[x][y] == null))
        {
            throw new OutOfFloorMapBoundsException("Coordinates must be within the map's boundaries.");
        }
        else{
            return loadedMap[x][y].getTypeTile();

        }
    }

    private TileType resolveTileType(String type)
    {
        if (type.equals("OBSTACLE"))
        {
            return TileType.OBSTACLE;
        }
        if (type.equals("HIGH"))
        {
            return TileType.HIGH;
        }
        if (type.equals("LOW"))
        {
            return TileType.LOW;
        }
        if (type.equals("BARE"))
        {
            return TileType.BARE;
        }

        return null;
    }

    private TileSide resolveTileSide(String side)
    {
        if (side.equals("PASSABLE"))
        {
            return TileSide.PASSABLE;
        }
        if (side.equals("WALL"))
        {
            return TileSide.WALL;
        }
        if (side.equals("DOOR_OPEN"))
        {
            return TileSide.DOOR_OPEN;
        }
        if (side.equals("DOOR_CLOSED"))
        {
            return TileSide.DOOR_CLOSED;
        }

        return null;
    }

    //Creates a 10x10 default map
    //All borders are of TileType OBSTACLE --> i'll post a picture of the default map on Slack
    //Used for default floor plan loading -> delete?
    private void populateArray(ArrayList<Tile> allTiles)
    {
        //Row 1
        Tile t1 = new Tile(1, TileType.BARE, 0, 0, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE, TileSide.WALL);
        allTiles.add(t1);
        Tile t2 = new Tile(0, TileType.BARE, 1, 0, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t2);
        Tile t3 = new Tile(0, TileType.BARE, 2, 0, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t3);
        Tile t4 = new Tile(0, TileType.BARE, 3, 0, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t4);
        Tile t5 = new Tile(0, TileType.BARE, 4, 0, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t5);
        Tile t6 = new Tile(0, TileType.BARE, 5, 0, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t6);
        Tile t7 = new Tile(0, TileType.BARE, 6, 0, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t7);
        Tile t8 = new Tile(0, TileType.BARE, 7, 0, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t8);
        Tile t9 = new Tile(0, TileType.BARE, 8, 0, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t9);
        Tile t10 = new Tile(0, TileType.BARE, 9, 0, TileSide.PASSABLE, TileSide.WALL, TileSide.WALL, TileSide.PASSABLE);
        allTiles.add(t10);

        //Row 2
        Tile t11 = new Tile(3, TileType.BARE, 0, 1, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL);
        allTiles.add(t11);
        Tile t12 = new Tile(0, TileType.BARE, 1, 1, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t12);
        Tile t13 = new Tile(4, TileType.BARE, 2, 1, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t13);
        Tile t14 = new Tile(0, TileType.BARE, 3, 1, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t14);
        Tile t15 = new Tile(0, TileType.BARE, 4, 1, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t15);
        Tile t16 = new Tile(0, TileType.BARE, 5, 1, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t16);
        Tile t17 = new Tile(0, TileType.BARE, 6, 1, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t17);
        Tile t18 = new Tile(5, TileType.BARE, 7, 1, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t18);
        Tile t19 = new Tile(0, TileType.BARE, 8, 1, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t19);
        Tile t20 = new Tile(0, TileType.BARE, 9, 1, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE);
        allTiles.add(t20);

        //Row 3
        Tile t21 = new Tile(1, TileType.BARE, 0, 2, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL);
        allTiles.add(t21);
        Tile t22 = new Tile(0, TileType.BARE, 1, 2, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t22);
        Tile t23 = new Tile(0, TileType.BARE, 2, 2, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t23);
        Tile t24 = new Tile(0, TileType.BARE, 3, 2, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t24);
        Tile t25 = new Tile(0, TileType.BARE, 4, 2, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t25);
        Tile t26 = new Tile(0, TileType.BARE, 5, 2, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t26);
        Tile t27 = new Tile(0, TileType.BARE, 6, 2, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t27);
        Tile t28 = new Tile(0, TileType.BARE, 7, 2, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t28);
        Tile t29 = new Tile(0, TileType.BARE, 8, 2, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t29);
        Tile t30 = new Tile(0, TileType.BARE, 9, 2, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE);
        allTiles.add(t30);

        //Row 4
        Tile t31 = new Tile(0, TileType.BARE, 0, 3, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL);
        allTiles.add(t31);
        Tile t32 = new Tile(0, TileType.BARE, 1, 3, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t32);
        Tile t33 = new Tile(0, TileType.BARE, 2, 3, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t33);
        Tile t34 = new Tile(0, TileType.BARE, 3, 3, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t34);
        Tile t35 = new Tile(0, TileType.BARE, 4, 3, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t35);
        Tile t36 = new Tile(0, TileType.BARE, 5, 3, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t36);
        Tile t37 = new Tile(0, TileType.BARE, 6, 3, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t37);
        Tile t38 = new Tile(0, TileType.BARE, 7, 3, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t38);
        Tile t39 = new Tile(0, TileType.BARE, 8, 3, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t39);
        Tile t40 = new Tile(0, TileType.BARE, 9, 3, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE);
        allTiles.add(t40);

        //Row 5
        Tile t41 = new Tile(1, TileType.BARE, 0, 4, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL);
        allTiles.add(t41);
        Tile t42 = new Tile(0, TileType.BARE, 1, 4, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t42);
        Tile t43 = new Tile(0, TileType.BARE, 2, 4, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t43);
        Tile t44 = new Tile(0, TileType.BARE, 3, 4, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t44);
        Tile t45 = new Tile(0, TileType.BARE, 4, 4, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t45);
        Tile t46 = new Tile(0, TileType.BARE, 5, 4, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t46);
        Tile t47 = new Tile(0, TileType.BARE, 6, 4, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t47);
        Tile t48 = new Tile(0, TileType.BARE, 7, 4, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t48);
        Tile t49 = new Tile(0, TileType.BARE, 8, 4, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t49);
        Tile t50 = new Tile(0, TileType.BARE, 9, 4, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE);
        allTiles.add(t50);

        //Row 6
        Tile t51 = new Tile(0, TileType.LOW, 0, 5, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL);
        allTiles.add(t51);
        Tile t52 = new Tile(0, TileType.LOW, 1, 5, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t52);
        Tile t53 = new Tile(0, TileType.LOW, 2, 5, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t53);
        Tile t54 = new Tile(0, TileType.LOW, 3, 5, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t54);
        Tile t55 = new Tile(0, TileType.LOW, 4, 5, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t55);
        Tile t56 = new Tile(0, TileType.HIGH, 5, 5, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t56);
        Tile t57 = new Tile(0, TileType.HIGH, 6, 5, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t57);
        Tile t58 = new Tile(0, TileType.HIGH, 7, 5, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t58);
        Tile t59 = new Tile(0, TileType.HIGH, 8, 5, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t59);
        Tile t60 = new Tile(0, TileType.HIGH, 9, 5, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE);
        allTiles.add(t60);

        //Row 7
        Tile t61 = new Tile(5, TileType.LOW, 0, 6, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL);
        allTiles.add(t61);
        Tile t62 = new Tile(0, TileType.LOW, 1, 6, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t62);
        Tile t63 = new Tile(0, TileType.LOW, 2, 6, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t63);
        Tile t64 = new Tile(0, TileType.LOW, 3, 6, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t64);
        Tile t65 = new Tile(0, TileType.LOW, 4, 6, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t65);
        Tile t66 = new Tile(0, TileType.HIGH, 5, 6, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t66);
        Tile t67 = new Tile(0, TileType.HIGH, 6, 6, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t67);
        Tile t68 = new Tile(0, TileType.HIGH, 7, 6, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t68);
        Tile t69 = new Tile(0, TileType.HIGH, 8, 6, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t69);
        Tile t70 = new Tile(0, TileType.HIGH, 9, 6, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE);
        allTiles.add(t70);

        //Row 8
        Tile t71 = new Tile(2, TileType.LOW, 0, 7, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL);
        allTiles.add(t71);
        Tile t72 = new Tile(0, TileType.LOW, 1, 7, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t72);
        Tile t73 = new Tile(0, TileType.LOW, 2, 7, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t73);
        Tile t74 = new Tile(0, TileType.LOW, 3, 7, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t74);
        Tile t75 = new Tile(0, TileType.LOW, 4, 7, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t75);
        Tile t76 = new Tile(0, TileType.HIGH, 5, 7, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t76);
        Tile t77 = new Tile(0, TileType.HIGH, 6, 7, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t77);
        Tile t78 = new Tile(0, TileType.HIGH, 7, 7, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t78);
        Tile t79 = new Tile(0, TileType.HIGH, 8, 7, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t79);
        Tile t80 = new Tile(0, TileType.HIGH, 9, 7, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE);
        allTiles.add(t80);

        //Row 9
        Tile t81 = new Tile(4, TileType.LOW, 0, 8, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL);
        allTiles.add(t81);
        Tile t82 = new Tile(0, TileType.LOW, 1, 8, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t82);
        Tile t83 = new Tile(0, TileType.LOW, 2, 8, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t83);
        Tile t84 = new Tile(0, TileType.LOW, 3, 8, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t84);
        Tile t85 = new Tile(0, TileType.LOW, 4, 8, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t85);
        Tile t86 = new Tile(0, TileType.HIGH, 5, 8, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t86);
        Tile t87 = new Tile(0, TileType.HIGH, 6, 8, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t87);
        Tile t88 = new Tile(0, TileType.HIGH, 7, 8, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t88);
        Tile t89 = new Tile(0, TileType.HIGH, 8, 8, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t89);
        Tile t90 = new Tile(0, TileType.HIGH, 9, 8, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE);
        allTiles.add(t90);

        //Row 10
        Tile t91 = new Tile(4, TileType.LOW, 0, 9, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.WALL);
        allTiles.add(t91);
        Tile t92 = new Tile(0, TileType.LOW, 1, 9, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t92);
        Tile t93 = new Tile(0, TileType.LOW, 2, 9, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t93);
        Tile t94 = new Tile(0, TileType.LOW, 3, 9, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t94);
        Tile t95 = new Tile(0, TileType.LOW, 4, 9, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t95);
        Tile t96 = new Tile(0, TileType.HIGH, 5, 9, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t96);
        Tile t97 = new Tile(0, TileType.HIGH, 6, 9, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t97);
        Tile t98 = new Tile(0, TileType.HIGH, 7, 9, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t98);
        Tile t99 = new Tile(0, TileType.HIGH, 8, 9, TileSide.WALL, TileSide.PASSABLE, TileSide.PASSABLE, TileSide.PASSABLE);
        allTiles.add(t99);
        Tile t100 = new Tile(0, TileType.HIGH, 9, 9, TileSide.WALL, TileSide.PASSABLE, TileSide.WALL, TileSide.PASSABLE);
        allTiles.add(t100);



    }

	public void populateAltArray(ArrayList<Tile> allTiles) {
		Tile a1 = new Tile(0, TileType.HIGH, 0,1, "NW");
		allTiles.add(a1);
		Tile a2 = new Tile(0, TileType.HIGH, 1, 1, "N");
		allTiles.add(a2);
		Tile a3 = new Tile(0, TileType.HIGH, 2, 1, "NE");
		allTiles.add(a3);
		Tile a4 = new Tile(0, TileType.HIGH, 0, 0, "SW");
		allTiles.add(a4);
		Tile a5 = new Tile(0, TileType.HIGH, 1, 0, "S");
		allTiles.add(a5);
		Tile a6 = new Tile(0, TileType.HIGH, 2, 0, "S");
		allTiles.add(a6);
		
		Tile b1 = new Tile(0, TileType.LOW, 3, 0, "S");
		allTiles.add(b1);
		Tile b2 = new Tile(0, TileType.LOW, 4, 0, "S");
		allTiles.add(b2);
		Tile b3 = new Tile(0, TileType.LOW, 5, 0, "SE");
		allTiles.add(b3);
		Tile b4 = new Tile(0, TileType.LOW, 3, 1, "NW");
		allTiles.add(b4);
		Tile b5 = new Tile(0, TileType.LOW, 4, 1, "N");
		allTiles.add(b5);
		Tile b6 = new Tile(0, TileType.LOW, 5, 1, "NE");
		allTiles.add(b6);
	}
}