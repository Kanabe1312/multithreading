// =============================================================
// EXERCITIUL 01 - ReentrantLock in loc de synchronized
// =============================================================
// Rescrii contorul de la L01 (ex. 09/10) folosind un ReentrantLock in
// loc de `synchronized`. Acelasi rezultat (mereu 200000), dar lock-ul
// e luat/eliberat MANUAL.
//
// Reguli:
//   - lock.lock() inainte de a atinge `count`
//   - unlock() MEREU in `finally` (altfel o exceptie blocheaza lock-ul pe veci)
//   - si getCount() ia lock-ul (visibility, ca la synchronized)
//
// Asteptat: 200000 (mereu).
// =============================================================

// PAS 1: import java.util.concurrent.locks.ReentrantLock;
// TODO

public class Ex301 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 3: Contor contor = new Contor();
        // PAS 4: 2 threaduri, fiecare face contor.increment() de 100_000 ori
        // PAS 5: start + join pe ambele
        // PAS 6: print "Asteptat: 200000" si "Real: " + contor.getCount()
        // TODO
    }

    // PAS 2: clasa Contor cu:
    //   - private final ReentrantLock lock = new ReentrantLock();
    //   - private int count = 0;
    //   - void increment()  -> lock.lock(); try { count++; } finally { lock.unlock(); }
    //   - int getCount()    -> la fel, dar return count;
    // TODO
}
