package practice;

public class exercises2 {
    public static void main(String[] args) throws InterruptedException {
        // --- Pasul a + b: start() vs run() ---
        // start() creeaza thread nou; run() ruleaza pe threadul curent (main).
        Thread thread = new Thread(() ->
                System.out.println("Salut din alt thread! Rulez pe: " + Thread.currentThread().getName())
        );
        thread.start();
        thread.join();

        Thread thread2 = new Thread(() ->
                System.out.println("Salut din alt thread! Rulez pe: " + Thread.currentThread().getName())
        );
        thread2.run();

        // --- Pasul c: 3 threaduri cu sleep(500) ---
        Thread t1 = new Thread(() -> printDupaPauza("Thread A"));
        Thread t2 = new Thread(() -> printDupaPauza("Thread B"));
        Thread t3 = new Thread(() -> printDupaPauza("Thread C"));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }

    private static void printDupaPauza(String nume) {
        System.out.println("[" + nume + "] inainte de sleep");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Worker interrupted while sleeping", e);
        }
        System.out.println("[" + nume + "] dupa sleep");
    }
}
