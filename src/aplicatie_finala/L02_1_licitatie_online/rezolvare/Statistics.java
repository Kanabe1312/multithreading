package aplicatie_finala.L02_1_licitatie_online.rezolvare;

import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private AtomicInteger accepted = new AtomicInteger(0);
    private AtomicInteger rejected = new AtomicInteger(0);
    private AtomicInteger processed = new AtomicInteger(0);



    public void incrementAccepted(){
        accepted.incrementAndGet();
    };
    public void incrementRejected(){
        rejected.incrementAndGet();
    };
    public void incrementProcessed(){
        processed.incrementAndGet();
    };

    public int getAccepted() {
        return accepted.get();
    }
    public int getRejected() {
        return rejected.get();
    }
    public int getProcessed() {
        return processed.get();
    }
}
