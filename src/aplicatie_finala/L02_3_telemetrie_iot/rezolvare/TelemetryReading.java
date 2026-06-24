package aplicatie_finala.L02_3_telemetrie_iot.rezolvare;

public class TelemetryReading {


    private int id;
    private String senzorName;
    private double temperature;

    public TelemetryReading(int id, String senzorName, double temperature) {
        this.id = id;
        this.senzorName = senzorName;
        this.temperature = temperature;
    }

    public int getId() {
        return id;
    }
    public String getSenzorName() {
        return senzorName;
    }
    public double getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "Reading#"+ id + " | " + senzorName + " | " + temperature+ "Celsius";
    }
}
