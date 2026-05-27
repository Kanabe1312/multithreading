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

import java.io.IOException;
import java.util.concurrent.*;

public class Ex07 {
    public static void main(String[] args) throws InterruptedException {
        Callable<String> task = ()-> {
            throw new IOException("File not found");
        };


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(task);//-->tine eroarea

        try{
            future.get();//--->incearca sa prind rezulzatu da callable arunca exceptia
        }catch (ExecutionException e){
            System.out.println(e.getCause().getMessage());
        }



    }
}
