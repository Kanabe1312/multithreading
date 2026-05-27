// =============================================================
// EXERCITIUL 06 - Callable cu rezultat
// =============================================================
// Creezi un Callable<Integer> care calculeaza suma 1+2+...+100.
// Submiti-l intr-un ExecutorService cu 1 thread.
// Printezi rezultatul cu future.get().
// =============================================================

// import java.util.concurrent.Callable;
// import java.util.concurrent.ExecutionException;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.Future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Ex06 {
    public static void main(String[] args) throws Exception {

        Callable<Integer> task = () -> {//---->Task care returneaza un integer
            int suma = 0;
            for(int i = 1;i <= 100;i++){
                suma += i;
            }
            return suma;
        };


        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Integer> future = executor.submit(task);


        Integer rezultat = future.get();


        System.out.println(rezultat);

        executor.shutdown();

    }
}
