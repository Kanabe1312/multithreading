import java.util.concurrent.locks.ReentrantLock;

// Exemplul A.1 — ReentrantLock: alternativa EXPLICITA la synchronized.
//
// `synchronized` ia si elibereaza lock-ul automat (la intrarea/iesirea din
// bloc). `ReentrantLock` il iei manual cu lock() si TREBUIE sa-l eliberezi
// tu cu unlock() — mereu in `finally`, altfel daca apare o exceptie lock-ul
// ramane blocat pentru totdeauna.
//
// De ce sa-l folosesti in loc de synchronized? Pentru ca ofera in plus:
//   - tryLock()            -> incerci sa iei lock-ul fara sa astepti la nesfarsit
//   - tryLock(timp, unit)  -> astepti cel mult X
//   - lockInterruptibly()  -> astept, dar pot fi intrerupt
//   - fairness             -> new ReentrantLock(true) = ordine FIFO
//   - Condition            -> await()/signal() (vezi A.3)
//
// "Reentrant" = acelasi thread poate lua lock-ul de mai multe ori (fiecare
// lock() trebuie sa aiba un unlock() pereche).
public class L3A1 {
    static class Contor {
        private final ReentrantLock lock = new ReentrantLock();
        private int count = 0;

        void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();   // MEREU in finally
            }
        }

        int get() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Contor c = new Contor();
        Runnable job = () -> {
            for (int i = 0; i < 100_000; i++) c.increment();
        };

        Thread t1 = new Thread(job);
        Thread t2 = new Thread(job);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Asteptat: 200000, Real: " + c.get());
    }
}
