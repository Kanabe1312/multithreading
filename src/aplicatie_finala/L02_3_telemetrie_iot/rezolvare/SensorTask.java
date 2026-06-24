package aplicatie_finala.L02_3_telemetrie_iot.rezolvare;

import java.util.Random;

public class SensorTask implements Runnable{

    private String sensorName;
    private TelemetryManagement telemetryManagement;
    private double temperature;

    public SensorTask(String sensorName, double temperature, TelemetryManagement telemetryManagement) {
        this.sensorName = sensorName;
        this.temperature = temperature;
        this.telemetryManagement = telemetryManagement;
    }

    @Override
    public void run() {
        try{
            Thread.sleep(new Random().nextInt(11)+5);

        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            return;
        }
        telemetryManagement.registerReading(sensorName, temperature);
    }
}
