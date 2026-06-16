// =============================================================
// EXERCITIUL 06 - Compare-And-Set spin loop
// =============================================================
// Implementezi manual "inmulteste cu 2" pe AtomicInteger folosind
// compareAndSet, ca sa intelegi cum se construiesc operatiile atomice.
//
// Algoritm:
//   while (true) {
//       int curent = v.get();
//       int dublu  = curent * 2;
//       if (v.compareAndSet(curent, dublu)) break;
//       // altfel: altcineva l-a schimbat intre timp, reincerc
//   }
//
// Pornesti 5 threaduri, fiecare face dubleaza() o data, pe un
// AtomicInteger pornit de la 1.
// Asteptat: 1 -> 2 -> 4 -> 8 -> 16 -> 32 (mereu 32, in orice ordine).
// =============================================================

// PAS 1: import java.util.concurrent.atomic.AtomicInteger;
// TODO

public class Ex206 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: AtomicInteger v = new AtomicInteger(1);
        // TODO

        // PAS 3: pornesti 5 threaduri, fiecare apeleaza dubleaza(v);
        // pune-le intr-un array Thread[] t = new Thread[5];
        // TODO

        // PAS 4: start + join pe toate
        // TODO

        // PAS 5: print "Asteptat: 32" si "Real: " + v.get()
        // TODO
    }

    // PAS 6: metoda statica `static void dubleaza(AtomicInteger v)` cu spin loop
    // TODO
}
