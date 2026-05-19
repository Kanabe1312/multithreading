// Exemplul A.1 — Cum creezi si pornesti primul thread.
// Rulezi cu: javac Main.java && java Main
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Creezi un Thread cu un Runnable (lambda care implementeaza run()).
        Thread t = new Thread(() -> System.out.println("Salut din alt thread!"));

        // start() => JVM creeaza un thread OS nou si apeleaza run() pe el.
        t.start();

        // Asteptam terminarea ca rularea sa fie deterministica.
        t.join();
    }
}
