package visualMovement;

import activityLog.ActivityLog;
import java.util.Arrays;

public class VisualMove {

  String floor[][] = new String[10][10];



  public static void  main(String[] args) throws InterruptedException {
    VisualMove v = new VisualMove();
    ActivityLog a = new ActivityLog();
   a.updateMovment("5,5");
   a.updateSensor("4,5uufloor-6,5uufloor-5,4uufloor-5,6uufloor");

    a.updateMovment("5,6");
    a.updateSensor("4,6uufloor-6,6uufloor-5,5uufloor-5,7uufloor");

    a.updateMovment("5,7");
    a.updateSensor("4,7uufloor-6,7uufloor-5,6uufloor-5,8uuwall");

    a.updateMovment("6,7");
    a.updateSensor("5,7uufloor-7,7uufloor-6,6uufloor-6,8uuwall");

    a.updateMovment("7,7");
    a.updateSensor("6,7uufloor-8,7uufloor-7,6uufloor-7,8uuwall");

    a.updateMovment("8,7");
    a.updateSensor("7,7uufloor-9,7uuwall-8,6uuwall-8,8uufloor");

    a.updateMovment("8,8");
    a.updateSensor("7,8uuwall-9,8uuwall-8,7uufloor-8,9uufloor");


    v.visual(a);

  }

  // Is sloppy, should improve. PoC for visualization
  public void visual(ActivityLog a) throws InterruptedException {
    String copy[][] = floor;
    for (int i = 0; i < copy.length; i++){
      for (int x = 0; x < copy[i].length; x++){
        copy[i][x] = "X";
      }
    }
    String[] move = a.getMovment().split(" ");
    String[] sensor = a.getSensor().split(" ");
    String[] move2;
    String[] sensor2;
    String[] tempSensor;
    String[] wallorTile;
    String[] checkTileType;

    System.out.println(sensor[0]);
    System.out.println(sensor[1]);
    System.out.println(Arrays.toString(sensor));

    for (int i = 1; i < move.length; i++){
      sensor2 = sensor[i].split("-");
      move2 = move[i].split(",");

        copy[Integer.parseInt(move2[0])][Integer.parseInt(move2[1])] = "A";


      for (int q = 0; q < sensor2.length; q++){
        tempSensor = sensor2[q].split(",");
        wallorTile = tempSensor[1].split("uu");

        System.out.println(wallorTile[0]);
        System.out.println(wallorTile[1]);
        System.out.println(Arrays.toString(wallorTile));

        if (wallorTile[1].equals("floor")){
          copy[Integer.parseInt(tempSensor[0])][Integer.parseInt(wallorTile[0])] = "O";
        }
        else{
          copy[Integer.parseInt(tempSensor[0])][Integer.parseInt(wallorTile[0])] = "-";
        }
      }

      printFloor(copy);
      Thread.sleep(1500);

      //7,8.wall
    }
  }

  public void printFloor(String[][] copy){
    clearScreen();
    for (int i = 0; i < copy.length; i++){
      for (int x = 0; x < copy[i].length; x++){
        System.out.print(copy[i][x] + "   ");
      }
      System.out.println("\n");
    }
  }

  public static void clearScreen() {
    try
    {
      final String os = System.getProperty("os.name");

      if (os.contains("Windows"))
      {
        Runtime.getRuntime().exec("cls");
      }
      else
      {
        Runtime.getRuntime().exec("clear");
      }
    }
    catch (final Exception e)
    {
      //  Handle any exceptions.
    }

    for(int i = 0; i < 30; i++) {
      System.out.println();
    }

    }
}
