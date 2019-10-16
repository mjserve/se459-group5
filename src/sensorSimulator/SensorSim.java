package sensorSimulator;

import navitagion.Coordinates;

import java.util.ArrayList;


public class SensorSim {

    private static SensorSim instance;
    private Tile[][] loadedMap;

    private SensorSim(){

    }

    public static SensorSim getInstance(){
        if (instance == null){
            instance = new SensorSim();
        }
        return instance;
    }

    //incorporate json file here
    public void loadFloorPlan()
    {
        //default row size
        //default column size
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

    //Robot can ask if tile is valid at (x, y)
    //Returns true if position returned is not an obstacle or out of bounds of map
    /*
    public boolean checkIsMovable(int x, int y)
    {
        return loadedMap.validatePos(x, y);
    }
    */

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

    //Creates a 10x10 default map
    //All borders are of TileType OBSTACLE --> i'll post a picture of the default map on Slack
    private void populateArray(ArrayList<Tile> allTiles)
    {
        //Row 1
        Tile t1 = new Tile(0, TileType.OBSTACLE, 0, 0);
        allTiles.add(t1);
        Tile t2 = new Tile(0, TileType.OBSTACLE, 1, 0);
        allTiles.add(t2);
        Tile t3 = new Tile(0, TileType.OBSTACLE, 2, 0);
        allTiles.add(t3);
        Tile t4 = new Tile(0, TileType.OBSTACLE, 3, 0);
        allTiles.add(t4);
        Tile t5 = new Tile(0, TileType.OBSTACLE, 4, 0);
        allTiles.add(t5);
        Tile t6 = new Tile(0, TileType.OBSTACLE, 5, 0);
        allTiles.add(t6);
        Tile t7 = new Tile(0, TileType.OBSTACLE, 6, 0);
        allTiles.add(t7);
        Tile t8 = new Tile(0, TileType.OBSTACLE, 7, 0);
        allTiles.add(t8);
        Tile t9 = new Tile(0, TileType.OBSTACLE, 8, 0);
        allTiles.add(t9);
        Tile t10 = new Tile(0, TileType.OBSTACLE, 9, 0);
        allTiles.add(t10);

        //Row 2
        Tile t11 = new Tile(0, TileType.OBSTACLE, 0, 1);
        allTiles.add(t11);
        Tile t12 = new Tile(0, TileType.BARE, 1, 1);
        allTiles.add(t12);
        Tile t13 = new Tile(0, TileType.BARE, 2, 1);
        allTiles.add(t13);
        Tile t14 = new Tile(0, TileType.BARE, 3, 1);
        allTiles.add(t14);
        Tile t15 = new Tile(0, TileType.BARE, 4, 1);
        allTiles.add(t15);
        Tile t16 = new Tile(0, TileType.BARE, 5, 1);
        allTiles.add(t16);
        Tile t17 = new Tile(0, TileType.BARE, 6, 1);
        allTiles.add(t17);
        Tile t18 = new Tile(0, TileType.BARE, 7, 1);
        allTiles.add(t18);
        Tile t19 = new Tile(0, TileType.BARE, 8, 1);
        allTiles.add(t19);
        Tile t20 = new Tile(0, TileType.OBSTACLE, 9, 1);
        allTiles.add(t20);

        //Row 3
        Tile t21 = new Tile(0, TileType.OBSTACLE, 0, 2);
        allTiles.add(t21);
        Tile t22 = new Tile(0, TileType.BARE, 1, 2);
        allTiles.add(t22);
        Tile t23 = new Tile(0, TileType.BARE, 2, 2);
        allTiles.add(t23);
        Tile t24 = new Tile(0, TileType.BARE, 3, 2);
        allTiles.add(t24);
        Tile t25 = new Tile(0, TileType.BARE, 4, 2);
        allTiles.add(t25);
        Tile t26 = new Tile(0, TileType.BARE, 5, 2);
        allTiles.add(t26);
        Tile t27 = new Tile(0, TileType.BARE, 6, 2);
        allTiles.add(t27);
        Tile t28 = new Tile(0, TileType.BARE, 7, 2);
        allTiles.add(t28);
        Tile t29 = new Tile(0, TileType.BARE, 8, 2);
        allTiles.add(t29);
        Tile t30 = new Tile(0, TileType.OBSTACLE, 9, 2);
        allTiles.add(t30);

        //Row 4
        Tile t31 = new Tile(0, TileType.OBSTACLE, 0, 3);
        allTiles.add(t31);
        Tile t32 = new Tile(0, TileType.BARE, 1, 3);
        allTiles.add(t32);
        Tile t33 = new Tile(0, TileType.BARE, 2, 3);
        allTiles.add(t33);
        Tile t34 = new Tile(0, TileType.BARE, 3, 3);
        allTiles.add(t34);
        Tile t35 = new Tile(0, TileType.BARE, 4, 3);
        allTiles.add(t35);
        Tile t36 = new Tile(0, TileType.BARE, 5, 3);
        allTiles.add(t36);
        Tile t37 = new Tile(0, TileType.BARE, 6, 3);
        allTiles.add(t37);
        Tile t38 = new Tile(0, TileType.BARE, 7, 3);
        allTiles.add(t38);
        Tile t39 = new Tile(0, TileType.BARE, 8, 3);
        allTiles.add(t39);
        Tile t40 = new Tile(0, TileType.OBSTACLE, 9, 3);
        allTiles.add(t40);

        //Row 5
        Tile t41 = new Tile(0, TileType.OBSTACLE, 0, 4);
        allTiles.add(t41);
        Tile t42 = new Tile(0, TileType.BARE, 1, 4);
        allTiles.add(t42);
        Tile t43 = new Tile(0, TileType.BARE, 2, 4);
        allTiles.add(t43);
        Tile t44 = new Tile(0, TileType.BARE, 3, 4);
        allTiles.add(t44);
        Tile t45 = new Tile(0, TileType.BARE, 4, 4);
        allTiles.add(t45);
        Tile t46 = new Tile(0, TileType.BARE, 5, 4);
        allTiles.add(t46);
        Tile t47 = new Tile(0, TileType.BARE, 6, 4);
        allTiles.add(t47);
        Tile t48 = new Tile(0, TileType.BARE, 7, 4);
        allTiles.add(t48);
        Tile t49 = new Tile(0, TileType.BARE, 8, 4);
        allTiles.add(t49);
        Tile t50 = new Tile(0, TileType.OBSTACLE, 9, 4);
        allTiles.add(t50);

        //Row 6
        Tile t51 = new Tile(0, TileType.OBSTACLE, 0, 5);
        allTiles.add(t51);
        Tile t52 = new Tile(0, TileType.LOW, 1, 5);
        allTiles.add(t52);
        Tile t53 = new Tile(0, TileType.LOW, 2, 5);
        allTiles.add(t53);
        Tile t54 = new Tile(0, TileType.LOW, 3, 5);
        allTiles.add(t54);
        Tile t55 = new Tile(0, TileType.LOW, 4, 5);
        allTiles.add(t55);
        Tile t56 = new Tile(0, TileType.HIGH, 5, 5);
        allTiles.add(t56);
        Tile t57 = new Tile(0, TileType.HIGH, 6, 5);
        allTiles.add(t57);
        Tile t58 = new Tile(0, TileType.HIGH, 7, 5);
        allTiles.add(t58);
        Tile t59 = new Tile(0, TileType.HIGH, 8, 5);
        allTiles.add(t59);
        Tile t60 = new Tile(0, TileType.OBSTACLE, 9, 5);
        allTiles.add(t60);

        //Row 7
        Tile t61 = new Tile(0, TileType.OBSTACLE, 0, 6);
        allTiles.add(t61);
        Tile t62 = new Tile(0, TileType.LOW, 1, 6);
        allTiles.add(t62);
        Tile t63 = new Tile(0, TileType.LOW, 2, 6);
        allTiles.add(t63);
        Tile t64 = new Tile(0, TileType.LOW, 3, 6);
        allTiles.add(t64);
        Tile t65 = new Tile(0, TileType.LOW, 4, 6);
        allTiles.add(t65);
        Tile t66 = new Tile(0, TileType.HIGH, 5, 6);
        allTiles.add(t66);
        Tile t67 = new Tile(0, TileType.HIGH, 6, 6);
        allTiles.add(t67);
        Tile t68 = new Tile(0, TileType.HIGH, 7, 6);
        allTiles.add(t68);
        Tile t69 = new Tile(0, TileType.HIGH, 8, 6);
        allTiles.add(t69);
        Tile t70 = new Tile(0, TileType.OBSTACLE, 9, 6);
        allTiles.add(t70);

        //Row 8
        Tile t71 = new Tile(0, TileType.OBSTACLE, 0, 7);
        allTiles.add(t71);
        Tile t72 = new Tile(0, TileType.LOW, 1, 7);
        allTiles.add(t72);
        Tile t73 = new Tile(0, TileType.LOW, 2, 7);
        allTiles.add(t73);
        Tile t74 = new Tile(0, TileType.LOW, 3, 7);
        allTiles.add(t74);
        Tile t75 = new Tile(0, TileType.LOW, 4, 7);
        allTiles.add(t75);
        Tile t76 = new Tile(0, TileType.HIGH, 5, 7);
        allTiles.add(t76);
        Tile t77 = new Tile(0, TileType.HIGH, 6, 7);
        allTiles.add(t77);
        Tile t78 = new Tile(0, TileType.HIGH, 7, 7);
        allTiles.add(t78);
        Tile t79 = new Tile(0, TileType.HIGH, 8, 7);
        allTiles.add(t79);
        Tile t80 = new Tile(0, TileType.OBSTACLE, 9, 7);
        allTiles.add(t80);

        //Row 9
        Tile t81 = new Tile(0, TileType.OBSTACLE, 0, 8);
        allTiles.add(t81);
        Tile t82 = new Tile(0, TileType.LOW, 1, 8);
        allTiles.add(t82);
        Tile t83 = new Tile(0, TileType.LOW, 2, 8);
        allTiles.add(t83);
        Tile t84 = new Tile(0, TileType.LOW, 3, 8);
        allTiles.add(t84);
        Tile t85 = new Tile(0, TileType.LOW, 4, 8);
        allTiles.add(t85);
        Tile t86 = new Tile(0, TileType.HIGH, 5, 8);
        allTiles.add(t86);
        Tile t87 = new Tile(0, TileType.HIGH, 6, 8);
        allTiles.add(t87);
        Tile t88 = new Tile(0, TileType.HIGH, 7, 8);
        allTiles.add(t88);
        Tile t89 = new Tile(0, TileType.HIGH, 8, 8);
        allTiles.add(t89);
        Tile t90 = new Tile(0, TileType.OBSTACLE, 9, 8);
        allTiles.add(t90);

        //Row 10
        Tile t91 = new Tile(0, TileType.OBSTACLE, 0, 9);
        allTiles.add(t91);
        Tile t92 = new Tile(0, TileType.OBSTACLE, 1, 9);
        allTiles.add(t92);
        Tile t93 = new Tile(0, TileType.OBSTACLE, 2, 9);
        allTiles.add(t93);
        Tile t94 = new Tile(0, TileType.OBSTACLE, 3, 9);
        allTiles.add(t94);
        Tile t95 = new Tile(0, TileType.OBSTACLE, 4, 9);
        allTiles.add(t95);
        Tile t96 = new Tile(0, TileType.OBSTACLE, 5, 9);
        allTiles.add(t96);
        Tile t97 = new Tile(0, TileType.OBSTACLE, 6, 9);
        allTiles.add(t97);
        Tile t98 = new Tile(0, TileType.OBSTACLE, 7, 9);
        allTiles.add(t98);
        Tile t99 = new Tile(0, TileType.OBSTACLE, 8, 9);
        allTiles.add(t99);
        Tile t100 = new Tile(0, TileType.OBSTACLE, 9, 9);
        allTiles.add(t100);



    }
}