package aplicatie_finala.L02_1_licitatie_online.rezolvare;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args)  throws Exception {
        long startTime = System.currentTimeMillis();

        Auction auction = new Auction();
        Statistics statistics = new Statistics();


        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Future<Boolean>> futures = new ArrayList<>();

        Random random = new Random();

        for(int i = 1; i <= 50;i++){
            String bidder = String.format("BIDDER_%02d",i);

            for (int j = 0; j < 3;j++){
                int amount = random.nextInt(1451)+50;


                Future<Boolean> future = executorService.submit(new BidTask(bidder, amount, auction, statistics));

                futures.add(future);

            }

        }

        executorService.shutdown();

        for(Future<Boolean> future : futures){
            future.get();
        }


        long duration = System.currentTimeMillis() - startTime;

        System.out.println();
        System.out.println("=== SUMAR ===");
        System.out.println("Total bids : "+statistics.getProcessed());
        System.out.println("Accepted bids : "+statistics.getAccepted());
        System.out.println("Rejected bids : "+statistics.getRejected());
        System.out.println("Winner : "+ auction.getWinner());
        System.out.println("Duration : "+duration+"ms");


        System.out.println("\n===FIRST 5 ACCEPTED ===");

        List<Bid> acceptedBids = auction.getAcceptedBids();

        for(int i = 0; i < Math.min(5, acceptedBids.size()); i++) {

            System.out.println(acceptedBids.get(i));
        }

        System.out.println("\n=== LAST 5 ACCEPTED ===");

        int start = Math.max(0,acceptedBids.size() - 5);

        for(int i = start; i < acceptedBids.size(); i++) {

            System.out.println(acceptedBids.get(i));

        }

        System.out.println("\nCheck : "+(statistics.getAccepted()+statistics.getRejected()));







    }
}

