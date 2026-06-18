// =============================================================
// EXERCITIUL 04 - AtomicInteger pentru contor
// =============================================================
// Inlocuiesti `int` cu `AtomicInteger` si vezi ca acum mereu obtii 200.000.
// Pornesti 2 threaduri, fiecare face count.incrementAndGet() de 100.000 ori.
// La final printezi count.get().
//
// Asteptat: 200000 (mereu).
// =============================================================

import java.util.concurrent.atomic.AtomicInteger;
// TODO

public class Ex204 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: AtomicInteger count = new AtomicInteger(0);
        // TODO
        AtomicInteger count = new AtomicInteger(0);

        // PAS 3: t1 si t2 — fiecare cu un for de 100_000 iteratii care face count.incrementAndGet()
        // TODO
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

        // PAS 4: start + join pe ambele
        // TODO
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // PAS 5: print "Asteptat: 200000" si "Real: " + count.get()
        // TODO
        System.out.println("Asteptat : 200000");
        System.out.println("Real: "+count.get());
    }
}
