package aplicatie_finala.L02_3_telemetrie_iot.rezolvare;

public class DashboardDaemon implements Runnable {

    private Statistics statistics;

    public DashboardDaemon(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("[dashboard] total=" + statistics.getTotalReadings() + " min=" + statistics.getMinTemperature() + " max=" + statistics.getMaxTemperature()
                            + " avg=" + statistics.getAverageTemperature());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}