// =============================================================
// EXERCITIUL 05 - getAndIncrement vs incrementAndGet
// =============================================================
// Demonstrezi diferenta:
//   - getAndIncrement => returneaza valoarea VECHE, apoi adauga 1
//   - incrementAndGet => adauga 1, apoi returneaza valoarea NOUA
//
// PARTEA A — comparatie:
//   AtomicInteger a = new AtomicInteger(10);
//   int x = a.getAndIncrement(); // x=10, a=11
//   AtomicInteger b = new AtomicInteger(10);
//   int y = b.incrementAndGet(); // y=11, b=11
//   Printezi x, a.get(), y, b.get()
//
// PARTEA B — generator de ID-uri unice:
//   Folosesti getAndIncrement() ca sa generezi ID-uri 0, 1, 2 dintr-un
//   AtomicInteger pornit de la 0. Pornesti 3 threaduri, fiecare cere
//   un ID si il printeaza. Astepti cu join().
//   Asteptat: cele 3 ID-uri 0, 1, 2 (in orice ordine, dar distincte).
// =============================================================

import java.util.concurrent.atomic.AtomicInteger;
// TODO

public class Ex205 {
    public static void main(String[] args) throws InterruptedException {
        // PARTEA A: comparatie getAndIncrement vs incrementAndGet
        // PAS 2: AtomicInteger a = new AtomicInteger(10); int x = a.getAndIncrement();
        // PAS 3: AtomicInteger b = new AtomicInteger(10); int y = b.incrementAndGet();
        // PAS 4: print rezultatele cu mesaje clare
        // TODO
        AtomicInteger a = new AtomicInteger(10);
        int x = a.getAndIncrement();
        AtomicInteger b = new AtomicInteger(10);
        int y = b.incrementAndGet();
        System.out.println("getAndIncrement: ");
        System.out.println("x = "+ x);
        System.out.println("a = "+a.get());
        System.out.println();
        System.out.println("incrementAndGet: ");
        System.out.println("y = "+ y);
        System.out.println("b = "+b.get());

        // PARTEA B: generator de ID-uri
        // PAS 5: AtomicInteger idGen = new AtomicInteger(0);
        // PAS 6: 3 threaduri, fiecare cere un ID cu idGen.getAndIncrement() si il printeaza
        // PAS 7: start + join pe toate
        // TODO
        System.out.println();
        AtomicInteger idgen = new AtomicInteger(0);
        Thread t1 = new Thread(()->{
            int id = idgen.getAndIncrement();
            System.out.println("T1 -> "+id);
        });
        Thread t2 = new Thread(()->{
            int id = idgen.getAndIncrement();
            System.out.println("T2 -> "+id);
        });
        Thread t3 = new Thread(()->{
            int id = idgen.getAndIncrement();
            System.out.println("T3 -> "+id);
        });
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();

    }
}
