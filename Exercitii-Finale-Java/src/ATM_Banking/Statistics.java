package ATM_Banking;

public class Statistics {
    private int processed;

    public synchronized void incrementProcessed() {
        processed++;
    }

    public synchronized int getProcessed() {
        return processed;
    }
}
