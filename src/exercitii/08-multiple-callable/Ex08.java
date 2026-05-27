// =============================================================
// EXERCITIUL 08 - Multiple Callable cu rezultate
// =============================================================
// Creezi 3 Callable<Integer>:
//   - suma 1..100
//   - suma 101..200
//   - suma 201..300
// Submiti-le toate intr-un ExecutorService cu 3 threaduri (fixed pool).
// Aduni rezultatele si printezi totalul.
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

public class Ex08 {
    public static void main(String[] args) throws Exception {

        Callable<Integer> c1 = () -> suma(1,100);
        Callable<Integer>c2 = ()->suma(101,200);
        Callable<Integer>c3 = ()-> suma(201,300);

        ExecutorService executor = Executors.newFixedThreadPool(3);


        Future<Integer> f1 = executor.submit(c1);
        Future<Integer> f2 = executor.submit(c2);
        Future<Integer>f3 = executor.submit(c3);


        int total = f1.get()+f2.get()+f3.get();


        System.out.println("Total = " + total);
        executor.shutdown();
    }
    private static int suma(int de_la, int pana_la){
        int s = 0;

        for(int i = de_la;i <= pana_la;i++){
            s += i;
        }
        return s;
    }


}
