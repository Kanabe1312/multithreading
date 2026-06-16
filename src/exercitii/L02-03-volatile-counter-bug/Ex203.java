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

    public static void main(String[] args) throws InterruptedException {
        // PAS 2: t1 si t2 — fiecare cu un for de 100_000 iteratii care face count++
        // TODO

        // PAS 3: start + join pe ambele
        // TODO

        // PAS 4: print "Asteptat: 200000" si "Real: " + count
        // TODO
    }
}
