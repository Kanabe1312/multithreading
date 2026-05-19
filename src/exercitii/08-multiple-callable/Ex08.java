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

public class Ex08 {
    public static void main(String[] args) throws Exception {
        // PAS 1: 3 Callable<Integer> care fac sume pe intervale diferite
        //        (extrage logica intr-o metoda helper: suma(int de_la, int pana_la))
        // TODO

        // PAS 2: ExecutorService cu newFixedThreadPool(3)
        // TODO

        // PAS 3: submit pentru fiecare task, primesti 3 Future
        // TODO

        // PAS 4: aduni f1.get() + f2.get() + f3.get() si printezi
        // TODO
    }

    // private static int suma(int de_la, int pana_la) { ... }
    // TODO
}
