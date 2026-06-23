package aplicatie_finala.L02_2_rate_limiter.rezolvare;

import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {

    private AtomicInteger accepted = new AtomicInteger(0);
    private AtomicInteger rejected = new AtomicInteger(0);

    public void incrementAccepted() {
        accepted.incrementAndGet();
    }
    public void incrementRejected() {
        rejected.incrementAndGet();
    }

    public int getAccepted() {
        return accepted.get();
    }
    public int getRejected() {
        return rejected.get();
    }
}
