// =============================================================
// EXERCITIUL 01 - Visibility bug (FARA volatile)
// =============================================================
// Reproduci bug-ul din A1. Ai un boolean STATIC "opreste" = false.
// Pornesti un thread worker care face: while (!opreste) iteratii++;
// Main asteapta 500 ms apoi pune opreste = true.
//
// Daca threadul nu se opreste in 2 secunde => bug-ul de visibility e prezent.
// Folosesti worker.join(2000) si daca worker.isAlive() printezi
// "BUG: worker inca ruleaza" si faci System.exit(0).
//
// IMPORTANT: NU pui volatile. Vrei sa demonstrezi bug-ul.
// Daca pe JVM-ul tau se opreste totusi, ruleaza cu: java -server Ex201
// =============================================================
public class Ex201 {
    // PAS 1: declara `static boolean opreste = false;` (FARA volatile)
    // TODO

    static boolean opreste = true;

    public static void main(String[] args) throws InterruptedException {
        // PAS 2: creezi un Thread cu un Runnable care:
        //  - printeaza "[worker] pornesc"
        //  - while (!opreste) iteratii++;
        //  - printeaza "[worker] oprit dupa <iteratii> iteratii"
        // TODO

        Thread worker = new Thread(() -> {
            System.out.println("[worker] pornesc ");
            long iteratii = 0;
            while(!opreste){
                iteratii++;
            }

        });

        // PAS 3: worker.start()
        // TODO
        worker.start();

        // PAS 4: Thread.sleep(500)
        // TODO
        Thread.sleep(500);
        // PAS 5: System.out.println("[main] setez opreste=true") apoi opreste = true
        // TODO
        System.out.println("[main] setez opreste = true");
        opreste = true;
        // PAS 6: worker.join(2000); daca worker.isAlive() printezi mesaj si System.exit(0)
        // TODO
        worker.join(2000);

        if(worker.isAlive()){
            System.out.println("[main] worker-ul inca ruleaza =>bug confirmat ");
            System.exit(0);
        }
    }
}
