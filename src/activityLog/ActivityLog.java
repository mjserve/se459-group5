package activityLog;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
public class ActivityLog {
  /*
  A class to maintain 5 seperate logs and output one individual log to a file called Data.txt

  When using this class make sure to use each update methods the same number of times.
  They all correlate to each other when outputting the final log.
  An example file output :

   Cleaning status: Clean| Sensor : A5,A3,B4  Moved to A5| Power Remaining: 70%| currently not_recharging
   Cleaning status: Dirty| Sensor : A6,A4,B5 Stayed at A5| Power Remaining: 69%| currently not_recharging

   */
  private String movment;
  private String sensor;
  private String cleaning;
  private String powerRemain;
  private String reCharge;
  private String grouped;
  private LogHelper logHelper = new LogHelper();


  public ActivityLog() {
    movment = "";
    sensor = "";
    cleaning = "";
    powerRemain = "";
    reCharge = "";
    grouped = "";
  }

  public String getMovment() {
    return this.movment;
  }

  public String getSensor() {
    return this.sensor;
  }

  public String getCleaning() {
    return this.cleaning;
  }

  public String getPowerRemain() {
    return this.powerRemain;
  }

  public String getReCharge() {
    return this.reCharge;
  }

  public String getGrouped(){
    return this.grouped;
  }

  public void updateMovment(String newMovment) {
  this.movment = logHelper.movmentUpdate(this.getMovment(), newMovment);
  }

  public void updateSensor(String newSensor) {
    this.sensor = logHelper.sensorUpdate(this.getSensor(), newSensor);
  }

  public void updateCleaning(String newCleaning) {
    this.cleaning = logHelper.cleaningUpdate(this.getCleaning(), newCleaning);
  }

  public void updatePowerRemain(String newPowerRemain) {
    this.powerRemain = logHelper.batteryUpdate(this.getPowerRemain(), newPowerRemain);
  }

  public void updateReCharge(String newReCharge) {
    this.reCharge = logHelper.reChargeUpdate(this.getReCharge(), newReCharge);
  }

  public void updateGrouped() {
    this.grouped = logHelper.groupedUpdate(getMovment(), getSensor(), getCleaning(), getPowerRemain(), getReCharge());
  }

  public void writeFile()
    {
      try {
        updateGrouped();
        String grouped = this.getGrouped();
        BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"));
        writer.write(grouped);
        writer.close();
      }
      catch(IOException ie){
        System.out.println("IOException Error");
    }
  }


  public static void main(String[] args) {
    ActivityLog a = new ActivityLog();
    a.updateCleaning("Clean");
    a.updateMovment("A5");
    a.updatePowerRemain("70%");
    a.updateReCharge("not_recharging");
    a.updateSensor("A5,A3,B4");

    a.updateCleaning("Dirty");
    a.updateMovment("A5");
    a.updatePowerRemain("69%");
    a.updateReCharge("not_recharging");
    a.updateSensor("A6,A4,B5");
      a.writeFile();
  }
}
