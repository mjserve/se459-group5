package activityLog;

class LogHelper {


  public String movmentUpdate(String movmentLog, String movment) {
    movmentLog = movmentLog + " " + movment;
    return movmentLog;
  }

  public String sensorUpdate(String sensorLog, String sensor) {
    sensorLog = sensorLog + " " + sensor;
    return sensorLog;
  }

  public String cleaningUpdate(String cleaningLog, String cleaning) {
    cleaningLog = cleaningLog + " " + cleaning;
    return cleaningLog;
  }

  public String batteryUpdate(String batteryLog, String battery) {
    batteryLog = batteryLog + " " + battery;
    return batteryLog;
  }

  public String reChargeUpdate(String reChargeLog, String reCharge) {
    reChargeLog = reChargeLog + " " + reCharge;
    return reChargeLog;
  }

  public String groupedUpdate(String getMovment, String getSensor, String getCleaning, String getPowerRemain, String getReCharge) {
    String[] movement = getMovment.split(" ");
    String[] sensor = getSensor.split(" ");
    String[] cleaning = getCleaning.split(" ");
    String[] powerRemain = getPowerRemain.split(" ");
    String[] reCharge = getReCharge.split(" ");
    String grouped = "";
    for (int i = 1; i < movement.length; i++) {
      grouped = grouped + "Cleaning status: " + cleaning[i] + "| Sensor : " + sensor[i];
      if (!movement[i - 1].equals(movement[i])) {
        grouped = grouped + "  Moved to " + movement[i] + "| Power Remaining: " + powerRemain[i] + "| currently " + reCharge[i] + "\n";
      } else {
        grouped = grouped + " Stayed at " + movement[i] + "| Power Remaining: " + powerRemain[i] + "| currently " + reCharge[i] + "\n";
      }
    }
    return grouped;
  }


}
