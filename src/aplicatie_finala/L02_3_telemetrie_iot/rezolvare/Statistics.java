package aplicatie_finala.L02_3_telemetrie_iot.rezolvare;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Statistics {

    private AtomicInteger totalReadings = new AtomicInteger(0);
    private AtomicLong totalTemperature = new AtomicLong(0);

    private AtomicReference<Double> minTemperature = new AtomicReference<>(Double.MAX_VALUE);
    private AtomicReference<Double> maxTemperature = new AtomicReference<>(-Double.MAX_VALUE);
   public AtomicReference<Double> getMinReference(){
       return minTemperature;
   }
   public AtomicReference<Double> getMaxReference(){
       return maxTemperature;
   }


    public void incrementReadings(){
        totalReadings.incrementAndGet();
    }

    public void addTemperature(double temperature){
        totalTemperature.addAndGet(Math.round(temperature * 10));
    }


    public int getTotalReadings(){
        return totalReadings.get();
    }
   public double getTotalTemperature(){
        return totalTemperature.get() / 10.0;
   }
    public double getMinTemperature(){
        return minTemperature.get();
    }
    public double getMaxTemperature(){
        return maxTemperature.get();
    }


    public double getAverageTemperature(){
        if(totalReadings.get() == 0){
            return 0;
        }else {
            return getTotalTemperature()/totalReadings.get();
        }
    }


    public synchronized void updateMax(double temperature) {
        if (temperature > maxTemperature.get()) {
            maxTemperature.set(temperature);
        }
    }

    public synchronized void updateMin(double temperature) {
        if (temperature < minTemperature.get()) {
            minTemperature.set(temperature);
        }
    }

}
