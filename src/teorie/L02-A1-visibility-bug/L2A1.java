// Exemplul A.1 — Visibility bug: threadul worker NU vede ca main a schimbat flag-ul.
//
// Cauza: JVM-ul are voie sa tina `gata` intr-un registru CPU al threadului
// worker. Modificarea facuta de main pe heap nu se propaga automat la
// cache-ul/registrul threadului worker => bucla infinita.
//
// IMPORTANT: pe unele JVM-uri / sisteme s-ar putea sa se opreasca totusi.
// Daca ti se intampla asta, foloseste -server cand rulezi:
//   javac L2A1.java
//   java -server L2A1
// Bug-ul e PERMIS de Java Memory Model, chiar daca nu apare mereu.
public class L2A1 {
    // BUG: NU e volatile, asa ca worker-ul poate citi o valoare cached la nesfarsit.
    static boolean gata = false;

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            System.out.println("[worker] pornesc");
            long iteratii = 0;
            while (!gata) {
                iteratii++;
            }
            System.out.println("[worker] m-am oprit dupa " + iteratii + " iteratii");
        });

        worker.start();
        Thread.sleep(500);

        System.out.println("[main] setez gata=true");
        gata = true;

        // Asteptam 2 secunde. Daca worker-ul nu vede flag-ul, ramane in while.
        worker.join(2000);
        if (worker.isAlive()) {
            System.out.println("[main] worker-ul INCA ruleaza => visibility bug confirmat");
            System.exit(0); // fortam iesirea
        }
    }
}
