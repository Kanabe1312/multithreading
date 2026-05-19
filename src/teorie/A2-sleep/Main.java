// Exemplul A.2 — Thread.sleep cu InterruptedException.
// sleep() arunca o checked exception => trebuie prinsa.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("Inainte de sleep");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // best practice: re-set interrupt flag si iesi
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("Dupa sleep");
        });
        t.start();
        t.join();
    }
}
