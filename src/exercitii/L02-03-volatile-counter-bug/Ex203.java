// =============================================================
// EXERCITIUL 03 - Volatile NU rezolva count++
// =============================================================
// Ai `static volatile int count = 0;`. Pornesti 2 threaduri, fiecare
// face count++ de 100.000 ori. Astepti cu join() si printezi count.
//
// Asteptat: 200.000. Vei vedea < 200.000. De ce?
// `volatile` da visibility, dar count++ NU e atomic
// (citeste -> +1 -> scrie). Cele 2 threaduri se calca.
// =============================================================
public class Ex203 {
    // PAS 1: declara `static volatile int count = 0;`
    // TODO
    static volatile int count = 0;

    public static void main(String[] args) throws InterruptedException {
        // PAS 2: t1 si t2 — fiecare cu un for de 100_000 iteratii care face count++
        // TODO

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                count++;
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                count++;
            }
        });
        // PAS 3: start + join pe ambele
        // TODO
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // PAS 4: print "Asteptat: 200000" si "Real: " + count
        // TODO
        System.out.println("Asteptat : 200000");
        System.out.println("Real: "+count);
    }
}
