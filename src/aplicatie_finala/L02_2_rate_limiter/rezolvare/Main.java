package aplicatie_finala.L02_2_rate_limiter.rezolvare;

import com.sun.net.httpserver.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws Exception{

        long startTime = System.currentTimeMillis();
        RateLimiter rateLimiter = new RateLimiter(100);
        Statistics statistics = new Statistics();


        ExecutorService executorService = Executors.newFixedThreadPool(20);


        List<Future<Response>> futures = new ArrayList<>();

        for(int i = 1 ;i <= 500;i++){
            String clientName = String.format("Client-%03d", i);

            Future<Response> future = executorService.submit(new RequestTask(clientName, rateLimiter, statistics));
            futures.add(future);

        }
        for(Future<Response> future : futures){
            future.get();
        }
        executorService.shutdown();
        long duration = System.currentTimeMillis() - startTime;


        System.out.println("\n===Sumar===");
        System.out.println("Accepted: "+statistics.getAccepted());
        System.out.println("Rejected: "+statistics.getRejected());


        List<Response> accepted = rateLimiter.getAcceptedResponses();
        System.out.println("\nFirst 5 Accepted: ");
        for( int i = 0; i< Math.min(5,accepted.size());i++){
            System.out.println(accepted.get(i));
        }
        System.out.println("\nLast 5 Accepted: ");
        int start = Math.max(0,accepted.size()-5);

        for(int i = start;i<accepted.size();i++){
            System.out.println(accepted.get(i));
        }

        System.out.println("\nDuration: "+ duration +" ms");



    }
}
