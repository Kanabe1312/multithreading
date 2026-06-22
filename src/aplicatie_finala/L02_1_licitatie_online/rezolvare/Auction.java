package aplicatie_finala.L02_1_licitatie_online.rezolvare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Auction {
    private AtomicInteger nextBidId = new AtomicInteger(1);

    private AtomicReference<Bid> currentWinner;

    private List<Bid> acceptedBids = Collections.synchronizedList(new ArrayList<>());

    public Auction() {
        Bid startBid = new Bid(0,"START",100);
        currentWinner = new AtomicReference<>(startBid);
    }

    public boolean submitBid(String bidderName, int amount) {
        int id = nextBidId.getAndIncrement();
        Bid newBid = new Bid(id,bidderName,amount);

        while(true){
            Bid current = currentWinner.get();
            if(amount <= current.getAmount()){
                return false;
            }
            if(currentWinner.compareAndSet(current,newBid)){
                acceptedBids.add(newBid);
                return true;
            }
        }
    }

    public Bid getWinner() {
        return currentWinner.get();
    }

    public List<Bid> getAcceptedBids() {
        return acceptedBids;
    }

}
