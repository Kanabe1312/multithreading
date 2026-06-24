import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

// Exemplul D.3 — FIX alternativ prin tryLock(timeout) + RETRY (plasa de siguranta).
//
// Cand NU poti impune o ordine globala (D.2), te aperi altfel: incerci sa iei
// ambele lock-uri cu tryLock(timeout). Daca nu reusesti sa-l iei pe al doilea,
// ELIBEREZI ce ai luat deja si REINCERCI mai tarziu. Asa nu ramai blocat la
// nesfarsit, chiar daca threadurile cer lock-urile in ordini diferite.
//
// Atentie: fara un mic "backoff" (pauza inainte de retry), doua threaduri pot
// intra in LIVELOCK — se tot ratam reciproc fara sa avanseze. De aceea punem
// o pauza scurta intre incercari.
public class L3D3 {
    static final ReentrantLock lockA = new ReentrantLock();
    static final ReentrantLock lockB = new ReentrantLock();
    static final AtomicInteger reincercari = new AtomicInteger(0);

    // incearca sa ia AMBELE lock-uri; daca nu reuseste, nu tine niciunul si return false
    static boolean lucreaza(ReentrantLock primul, ReentrantLock aldoilea) throws InterruptedException {
        if (primul.tryLock(50, TimeUnit.MILLISECONDS)) {
            try {
                if (aldoilea.tryLock(50, TimeUnit.MILLISECONDS)) {
                    try {
                        // sectiune critica cu ambele lock-uri
                        return true;
                    } finally {
                        aldoilea.unlock();
                    }
                }
            } finally {
                primul.unlock();
            }
        }
        return false;   // n-am prins ambele -> se reincearca afara
    }

    static void ruleaza(ReentrantLock primul, ReentrantLock aldoilea) {
        try {
            for (int i = 0; i < 100_000; i++) {
                while (!lucreaza(primul, aldoilea)) {
                    reincercari.incrementAndGet();
                    Thread.sleep(1);     // backoff anti-livelock
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> ruleaza(lockA, lockB), "T1");
        Thread t2 = new Thread(() -> ruleaza(lockB, lockA), "T2");   // ordine inversa

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Gata, fara deadlock (tryLock + retry). Reincercari totale: "
                + reincercari.get());
    }
}
