package aplicatie_finala.L02_3_telemetrie_iot.rezolvare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TelemetryManagement {

    private AtomicInteger nextReadingId = new AtomicInteger(1);

    private List<TelemetryReading> readings = Collections.synchronizedList(new ArrayList<>());

    private Statistics statistics = new Statistics();
    public TelemetryManagement(Statistics statistics) {
        this.statistics = statistics;
    }


    public void registerReading(String sensorName, double temperature) {
        int id  = nextReadingId.getAndIncrement();


        TelemetryReading reading = new TelemetryReading(id, sensorName, temperature);
        readings.add(reading);

        statistics.incrementReadings();
        statistics.addTemperature(temperature);

        while(true){
            double currentMax = statistics.getMaxTemperature();
            if(temperature <= currentMax){
                break;
            }

            if(statistics.getMaxRe)
        }

    }

}
