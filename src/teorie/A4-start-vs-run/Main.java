// Exemplul A.4 — Diferenta intre start() si run().
// start() => thread nou. run() => apel normal de metoda, ruleaza pe threadul curent.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Runnable task = () ->
                System.out.println("Rulez pe: " + Thread.currentThread().getName());

        Thread t1 = new Thread(task);
        t1.start();    // alt thread (ex: "Thread-0")
        t1.join();

        Thread t2 = new Thread(task);
        t2.run();      // apel normal de metoda => ruleaza pe "main"
    }
}
