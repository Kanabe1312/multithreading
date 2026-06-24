package aplicatie_finala.L02_3_telemetrie_iot.rezolvare;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args)  throws Exception{
        long startTime = System.currentTimeMillis();

        Statistics statistics = new Statistics();

        TelemetryManagement telemetryManagement = new TelemetryManagement(statistics);

        ExecutorService executorService = Executors.newFixedThreadPool(16);

        Thread dashboard = new Thread(new DashboardDaemon(statistics));

        dashboard.setDaemon(true);
        dashboard.start();


        List<Future<?>> futures = new ArrayList<>();

        Random random = new Random();

        for(int i = 1; i <= 1000; i++) {

            String sensorName = String.format("Sensor-%03d", i);

            double temperature = Math.round((-20 + random.nextDouble() * 70) * 10) / 10.0;


           //  future cu ? pt ca runnable nu returneaza nmc nu ne intereseaza ce da
            Future<?> future = executorService.submit(new SensorTask(sensorName, temperature, telemetryManagement));

            futures.add(future);

        }

        for (Future<?> future : futures) {
            future.get();
        }
        executorService.shutdown();

        long duration = System.currentTimeMillis() - startTime;

        System.out.println("===RAPORT===");

        System.out.println("Total readings: " + statistics.getTotalReadings());
        System.out.println("Average temperature: " + statistics.getAverageTemperature());
        System.out.println("Min temperature: " + statistics.getMinTemperature());
        System.out.println("Max temperature: "+ statistics.getMaxTemperature());
        System.out.println("Duration: " + duration +" ms");



    }

}
