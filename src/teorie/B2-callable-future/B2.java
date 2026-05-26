import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Exemplul B.2 — Callable returneaza valoare, Future iti da rezultatul.
public class B2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Integer> task = () -> {
            int s = 0;
            for (int i = 1; i <= 10; i++) {
                s += i;
            }
            return s;
        };

        Future<Integer> future = executor.submit(task);//---submit trimite tasku de aici la executor

        // .get() blocheaza pana task-ul termina si returneaza rezultatul.//---ASTA NU INTELEG
        System.out.println("Suma 1..10 = " + future.get());//---nici conceptu asta

        executor.shutdown();//---executorul tine programele(threads) in viata ,deaia tr oprit
    }
}
