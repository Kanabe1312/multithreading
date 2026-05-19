// =============================================================
// EXERCITIUL 09 - Race condition (fara protectie)
// =============================================================
// Creezi clasa Contor cu un int count = 0 si o metoda
// incrementeaza() care face count++.
// Pornesti 2 threaduri, fiecare face incrementeaza() de 100.000 ori.
// Astepti cu join() si printezi count.
//
// Asteptat: 200.000. Vei vedea < 200.000.
// De ce? count++ NU e atomic (citeste -> +1 -> scrie).
// =============================================================
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // PAS 1: instantiezi Contor
        // TODO

        // PAS 2: 2 threaduri, fiecare cu for (i=0; i<100_000; i++) contor.incrementeaza();
        // TODO

        // PAS 3: start + join pe ambele
        // TODO

        // PAS 4: print "Asteptat: 200000" si "Real: <count>"
        // TODO
    }

    // PAS 5: defineste Contor cu int count si metodele incrementeaza() + getCount()
    // TODO
}
