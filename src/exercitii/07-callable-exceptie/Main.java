// =============================================================
// EXERCITIUL 07 - Callable cu exceptie
// =============================================================
// Creezi un Callable<String> care arunca IOException("File not found").
// Submiti-l, incerci future.get() si prinzi ExecutionException.
// Printezi e.getCause().getMessage().
//
// Intrebare: de ce nu primesti direct IOException, ci e wrapped?
// =============================================================

// import java.io.IOException;
// import java.util.concurrent.Callable;
// import java.util.concurrent.ExecutionException;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // PAS 1: Callable<String> care arunca IOException
        // TODO

        // PAS 2: submit + try { future.get(); } catch (ExecutionException e) { ... }
        // TODO

        // PAS 3: print e.getCause().getMessage()
        // TODO
    }
}
