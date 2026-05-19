// Exemplul A.1 — Cum creezi si pornesti primul thread.
// Rulezi cu: javac A1.java && java A1
public class A1 {
    public static void main(String[] args) throws InterruptedException {
        // Creezi un Thread cu un Runnable (lambda care implementeaza run()).
        Thread t = new Thread(() -> System.out.println("Salut din alt thread!"));

        // start() => JVM creeaza un thread OS nou si apeleaza run() pe el.
        t.start();

        // Asteptam terminarea ca rularea sa fie deterministica.
        t.join();
    }
}
