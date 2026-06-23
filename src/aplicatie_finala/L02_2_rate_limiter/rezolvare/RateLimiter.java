package aplicatie_finala.L02_2_rate_limiter.rezolvare;

import com.sun.net.httpserver.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {

    private int limit;
    private AtomicInteger acceptedCount = new AtomicInteger(0);

    private AtomicInteger nextResponseId = new AtomicInteger(0);


    private List<Response> acceptedResponses = Collections.synchronizedList(new ArrayList<>());


    public RateLimiter(int limit) {
        this.limit = limit;
    }


    public Response processRequest(String clientName) {
        if(acceptedCount.get() >= limit) {
            return null;
        }
        while(true) {
            int current = acceptedCount.get();

            if(current >= limit) {
                return null;
            }
            if(acceptedCount.compareAndSet(current, current+1)) {
                int responseId = nextResponseId.getAndIncrement();

                Response response = new Response(responseId, clientName);

                acceptedResponses.add(response);

                return response;
            }

        }
    }
}
