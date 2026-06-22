package aplicatie_finala.L02_1_licitatie_online.rezolvare;

import java.util.Random;
import java.util.concurrent.Callable;

public class BidTask implements Callable<Boolean> {
    private String bidderName;
    private int amount;
    private Auction auction;
    private Statistics statistics;

    public BidTask(String bidderName, int amount, Auction auction, Statistics statistics) {
        this.bidderName = bidderName;
        this.amount = amount;
        this.auction = auction;
        this.statistics = statistics;
    }
    @Override
    public Boolean call() throws Exception {
        Thread.sleep(new Random().nextInt(11)+5);

        boolean accepted = auction.submitBid(bidderName, amount);
        statistics.incrementProcessed();
        if(accepted){
            statistics.incrementAccepted();
        }else {
            statistics.incrementRejected();
        }
        return accepted;
    }

}
