import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// Exemplul A.2 — tryLock(): incerci sa iei lock-ul FARA sa blochezi la
// nesfarsit. Daca nu reusesti, faci altceva (skip, retry, mesaj).
//
// `lock.lock()`             -> astept ORICAT pana iau lock-ul (poate bloca).
// `lock.tryLock()`          -> iau lock-ul daca e liber ACUM; altfel return false imediat.
// `lock.tryLock(2, SECONDS) -> astept cel mult 2 secunde; altfel return false.
//
// Util cand nu vrei sa "atarni": optional work, evitarea deadlock-ului,
// task-uri care pot fi sarite daca resursa e ocupata.
public class L3A2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        // Un thread care tine lock-ul ~1 secunda.
        Thread holder = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("[holder] am luat lock-ul, il tin 1s");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                System.out.println("[holder] am eliberat lock-ul");
            }
        });
        holder.start();
        Thread.sleep(100); // ne asiguram ca holder a apucat lock-ul

        // 1) tryLock() fara asteptare — lock-ul e ocupat acum
        if (lock.tryLock()) {
            try {
                System.out.println("[main] am luat lock-ul (nu ar trebui sa se intample)");
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("[main] lock ocupat -> NU astept, fac altceva");
        }

        // 2) tryLock(timeout) — astept pana se elibereaza (max 2s)
        if (lock.tryLock(2, TimeUnit.SECONDS)) {
            try {
                System.out.println("[main] am luat lock-ul dupa ce holder l-a eliberat");
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("[main] nici dupa 2s n-am reusit");
        }

        holder.join();
    }
}
