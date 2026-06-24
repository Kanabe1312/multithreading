import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// Exemplul B.1 — ReadWriteLock: MULTI cititori in paralel, UN scriitor exclusiv.
//
// Un ReentrantLock obisnuit lasa un singur thread inauntru, chiar daca toti
// vor doar sa CITEASCA. Dar cititorii nu se incurca intre ei — doar
// scrierile au nevoie de exclusivitate.
//
// ReentrantReadWriteLock are doua lock-uri:
//   - readLock()  -> il pot tine MAI MULTE threaduri simultan (citire paralela)
//   - writeLock() -> exclusiv: cat timp scrie cineva, nimeni nu citeste/scrie
//
// Castig real cand citesti des si scrii rar (cache, config, tabele de routing).
public class L3B1 {
    static class Cache {
        private final Map<String, String> map = new HashMap<>();
        private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

        String get(String cheie) {
            rw.readLock().lock();
            try {
                return map.get(cheie);
            } finally {
                rw.readLock().unlock();
            }
        }

        void put(String cheie, String valoare) {
            rw.writeLock().lock();
            try {
                map.put(cheie, valoare);
            } finally {
                rw.writeLock().unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Cache cache = new Cache();
        cache.put("user:1", "Ana");

        Runnable cititor = () -> {
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName()
                        + " citeste user:1 = " + cache.get("user:1"));
            }
        };

        Thread r1 = new Thread(cititor, "R1");
        Thread r2 = new Thread(cititor, "R2");
        Thread w = new Thread(() -> cache.put("user:1", "Bogdan"), "W");

        r1.start();
        r2.start();
        w.start();
        r1.join();
        r2.join();
        w.join();

        System.out.println("Final: user:1 = " + cache.get("user:1"));
    }
}
