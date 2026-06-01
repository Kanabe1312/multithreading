import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Exemplul B.3 — Exceptiile dintr-un Callable sunt ambalate in ExecutionException.
// Exceptia originala e accesibila la e.getCause().
public class B3 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> task = () -> {
            throw new IOException("File not found");
        };

        Future<String> future = executor.submit(task);

        try {
            future.get();
        } catch (ExecutionException e) {//wrapper-ul pentru erorile async
            // e.getCause() == IOException-ul original
            System.out.println("originala: " + e.getCause().getMessage());
        }

        executor.shutdown();
    }
}
