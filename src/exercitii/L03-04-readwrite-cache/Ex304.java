// =============================================================
// EXERCITIUL 04 - Cache cu ReadWriteLock
// =============================================================
// Construiesti un mic cache (Map<String,Integer>) protejat cu un
// ReentrantReadWriteLock:
//   - get(cheie)         -> sub readLock()  (multi cititori in paralel)
//   - put(cheie, valoare)-> sub writeLock() (exclusiv)
//
// Scenariu: pui "x" = 0. Pornesti 3 threaduri cititoare (citesc "x" de
// cateva ori) si 1 thread scriitor (face put "x" = 42).
//
// Asteptat: la final get("x") == 42. (Valorile intermediare pot varia.)
// Idee de inteles: cititorii nu se blocheaza intre ei; doar scriitorul
// e exclusiv.
// =============================================================

// PAS 1: importuri
   import java.util.HashMap; import java.util.Map;
   import java.util.concurrent.locks.ReentrantReadWriteLock;
// TODO

public class Ex304 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 3: Cache cache = new Cache(); cache.put("x", 0);
        // PAS 4: 3 threaduri cititoare (for cateva citiri din cache.get("x"))
        //        + 1 thread scriitor (cache.put("x", 42))
        // PAS 5: start + join pe toate
        // PAS 6: print "Final x = " + cache.get("x")
        // TODO
        Cache cache = new Cache();
        cache.put("x",0);
        Runnable citior = ()->{
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName()+"citeste x = "+cache.get("x"));
            }
        };
        Thread r1 = new Thread(citior,"R1");
        Thread r2 = new Thread(citior,"R2");
        Thread r3 = new Thread(citior,"R3");

        Thread w = new Thread(() -> {cache.put("x", 42);
            System.out.println("Writer a scris 42");

        }, "W");

        r1.start();
        r2.start();
        r3.start();
        w.start();

        r1.join();
        r2.join();
        r3.join();
        w.join();

    }

    // PAS 2: clasa Cache cu:
    //   - private final Map<String,Integer> map = new HashMap<>();
    //   - private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    //   - Integer get(String k) -> rw.readLock().lock(); try {...} finally {...}
    //   - void put(String k, Integer v) -> rw.writeLock().lock(); try {...} finally {...}
    // TODO
    static class Cache {
        private Map<String, Integer> map;
        private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();//protejeaza mapa
        Integer get(String k) {
            rw.readLock().lock();
            try {
                return map.get(k);
            } finally {
                rw.readLock().unlock();
            }
        }
        void put(String k, Integer v) {
            rw.writeLock().lock();
            try {
                map.put(k, v);

            }finally {
                rw.writeLock().unlock();
            }
        }
    }
}
