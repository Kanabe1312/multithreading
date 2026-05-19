// Exemplul A.3 — join() blocheaza threadul curent pana threadul tinta termina.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("Lucrez...");
        });

        t.start();
        t.join();                       // main asteapta aici
        System.out.println("Gata!");    // se printeaza DUPA "Lucrez..."
    }
}
