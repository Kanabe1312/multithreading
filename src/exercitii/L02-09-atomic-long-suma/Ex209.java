// =============================================================
// EXERCITIUL 09 - AtomicLong pentru contoare > Integer.MAX_VALUE
// =============================================================
// Folosesti AtomicLong cand `int` nu e suficient (peste ~2.1 miliarde).
//
// Scenariu: 4 threaduri, fiecare adauga la `total` valori intre 1 si
// 1.000.000 (foloseste `total.addAndGet(i)` intr-un for cu i de la 1 la 1_000_000).
// Suma 1..1_000_000 = 500_000_500_000 (depaseste Integer.MAX_VALUE ~2.147 mld).
// Cu 4 threaduri => 4 * 500_000_500_000 = 2_000_002_000_000.
//
// Asteptat: 2000002000000.
// =============================================================

// PAS 1: import java.util.concurrent.atomic.AtomicLong;
// TODO

public class Ex209 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: AtomicLong total = new AtomicLong(0);
        // TODO

        // PAS 3: 4 threaduri, fiecare face for (int i = 1; i <= 1_000_000; i++) total.addAndGet(i);
        // pune-le intr-un Thread[] t = new Thread[4];
        // TODO

        // PAS 4: start + join pe toate
        // TODO

        // PAS 5: print "Asteptat: 2000002000000" si "Real: " + total.get()
        // TODO
    }
}
