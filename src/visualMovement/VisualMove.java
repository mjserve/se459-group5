package visualMovement;

import activityLog.ActivityLog;
import sensorSimulator.SensorSim;

import java.util.ArrayList;
import java.util.Arrays;

public class VisualMove {

  String floor[][] = new String[10][10];


  public static void main(String[] args) throws InterruptedException {
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
    for (int i = 0; i < copy.length; i++) {
      for (int x = 0; x < copy[i].length; x++) {
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

    for (int i = 1; i < move.length; i++) {
      sensor2 = sensor[i].split("-");
      move2 = move[i].split(",");

      copy[Integer.parseInt(move2[0])][Integer.parseInt(move2[1])] = "R";


      for (int q = 0; q < sensor2.length; q++) {
        tempSensor = sensor2[q].split(",");
        wallorTile = tempSensor[1].split("uu");

        if (wallorTile[1].equals("floor")) {
          copy[Integer.parseInt(tempSensor[0])][Integer.parseInt(wallorTile[0])] = "O";
        } else {
          if (q == 0) {
            copy[Integer.parseInt(tempSensor[0])][Integer.parseInt(wallorTile[0])] = "^";
          } else if (q == 1) {
            copy[Integer.parseInt(tempSensor[0])][Integer.parseInt(wallorTile[0])] = "v";
          } else if (q == 2) {
            copy[Integer.parseInt(tempSensor[0])][Integer.parseInt(wallorTile[0])] = "<";
          } else if (q == 3) {
            copy[Integer.parseInt(tempSensor[0])][Integer.parseInt(wallorTile[0])] = ">";
          }
        }
      }

      printFloor(copy);
      Thread.sleep(1500);

      //7,8.wall
    }
  }

  public void printFloor(String[][] copy) {
    clearScreen();
    ArrayList<Integer> locationUp = new ArrayList<>();
    ArrayList<Integer> locationDown = new ArrayList<>();
    String test = "";

    for (int i = 0; i < copy.length; i++) {

      //upper wall would be printed loop looking ahead

      for (int x = 0; x < copy[i].length; x++) { //actually tile information being printed with side walls
        if (copy[i][x].equals("X")) {
          System.out.print("   " + copy[i][x] + "   ");
        } else if (copy[i][x].equals("O")) {
          System.out.print("   " + copy[i][x] + "   ");
        } else if (copy[i][x].equals("<")) {
          System.out.print("   " + "X  <");
        } else if (copy[i][x].equals(">")) {
          System.out.print(">  X" + "   ");
        } else if (copy[i][x].equals("v")) {
          locationDown.add(i);
          System.out.print("   X   ");
        } else if (copy[i][x].equals("^")) {
          locationUp.add(i);
          System.out.print("   X   ");
        } else if (copy[i][x].equals("R")) {
          System.out.print("   " + copy[i][x] + "   ");
        }
      }
      System.out.println("\n");
      int temp;
      if (!locationDown.isEmpty()) {
        temp = locationDown.get(0);
      } else {
        temp = 0;
      }
      for (int x = 0; x < temp; x++) { //lower wall being printed, will replace temp with next lower wall in arraylist until no more remaining
        if (x + 1 == temp) {
          test = test + "   v   ";
        } else {
          test = test + "       ";
        }
      }
//      System.out.print("test");
      System.out.print(test);
      locationDown = new ArrayList<>();
    }
  }

  public static void clearScreen() {
    try {
      final String os = System.getProperty("os.name");

      if (os.contains("Windows")) {
        Runtime.getRuntime().exec("cls");
      } else {
        Runtime.getRuntime().exec("clear");
      }
    } catch (final Exception e) {
      //  Handle any exceptions.
    }

    for (int i = 0; i < 30; i++) {
      System.out.println();
    }

  }
}
