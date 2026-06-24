// =============================================================
// EXERCITIUL 02 - tryLock(): sari peste daca e ocupat
// =============================================================
// Ai o resursa protejata de un ReentrantLock. Pornesti 5 threaduri care
// vor s-o foloseasca, dar fiecare task NU vrea sa astepte: daca lock-ul
// e ocupat, printeaza "[task-N] ocupat, sar peste" si se termina.
//
// Folosesti `lock.tryLock()` (varianta fara timeout):
//   if (lock.tryLock()) {
//       try { /* lucrez ~50ms cu resursa */ }
//       finally { lock.unlock(); }
//   } else {
//       // ocupat -> skip
//   }
//
// Asteptat: unul-doua threaduri intra, restul sar peste (nedeterminist).
// =============================================================

// PAS 1: import java.util.concurrent.locks.ReentrantLock;
// TODO

public class Ex302 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: ReentrantLock lock = new ReentrantLock();
        // PAS 3: Thread[] t = new Thread[5]; captezi indexul cu `final int idx = i;`
        //        in fiecare thread apelezi foloseste(lock, idx);
        // PAS 4: start + join pe toate
        // TODO
    }

    // PAS 5: static void foloseste(ReentrantLock lock, int idx)
    //   - tryLock(): daca reusesti -> print "[task-idx] LUCREZ", Thread.sleep(50),
    //     in finally unlock + print "[task-idx] gata"
    //   - altfel -> print "[task-idx] ocupat, sar peste"
    //   (prinde InterruptedException in jurul lui sleep)
    // TODO
}
