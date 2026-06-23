package aplicatie_finala.L02_2_rate_limiter.rezolvare;

import java.util.Random;
import java.util.concurrent.Callable;

public class RequestTask implements Callable<Response> {
    private String clientName;
    private RateLimiter rateLimiter;
    private Statistics statistics;


    public RequestTask(String clientName, RateLimiter rateLimiter, Statistics statistics) {
        this.clientName = clientName;
        this.rateLimiter = rateLimiter;
        this.statistics = statistics;
    }

    @Override
    public Response call() throws Exception {
        Thread.sleep(new Random().nextInt(16)+5);

        Response response = rateLimiter.processRequest(clientName);

        if(response != null) {
            statistics.incrementAccepted();
            return response;
        }else  {
            statistics.incrementRejected();
            return null;
        }

    }



}
