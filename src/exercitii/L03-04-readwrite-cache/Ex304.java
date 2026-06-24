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
//   import java.util.HashMap; import java.util.Map;
//   import java.util.concurrent.locks.ReentrantReadWriteLock;
// TODO

public class Ex304 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 3: Cache cache = new Cache(); cache.put("x", 0);
        // PAS 4: 3 threaduri cititoare (for cateva citiri din cache.get("x"))
        //        + 1 thread scriitor (cache.put("x", 42))
        // PAS 5: start + join pe toate
        // PAS 6: print "Final x = " + cache.get("x")
        // TODO
    }

    // PAS 2: clasa Cache cu:
    //   - private final Map<String,Integer> map = new HashMap<>();
    //   - private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    //   - Integer get(String k) -> rw.readLock().lock(); try {...} finally {...}
    //   - void put(String k, Integer v) -> rw.writeLock().lock(); try {...} finally {...}
    // TODO
}
