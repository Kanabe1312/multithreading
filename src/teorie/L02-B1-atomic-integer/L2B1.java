import java.util.concurrent.atomic.AtomicInteger;

// Exemplul B.1 — `AtomicInteger` rezolva count++ fara `synchronized`.
//
// `incrementAndGet()` face citeste-aduna-scrie ca o singura operatie atomica
// (folosind instructiuni CPU speciale, ex. CAS). Rezultatul e mereu corect.
public class L2B1 {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                count.incrementAndGet();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                count.incrementAndGet();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Asteptat: 200000");
        System.out.println("Real:     " + count.get() + "   (mereu 200000)");
    }
}
