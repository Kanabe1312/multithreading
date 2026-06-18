// =============================================================
// EXERCITIUL 02 - Volatile flag (repara bug-ul)
// =============================================================
// Acelasi scenariu ca la 01, dar de data asta `opreste` e `volatile`.
// Threadul worker trebuie sa se opreasca aproape imediat dupa ce main
// seteaza flag-ul.
//
// Asteptat: worker se opreste; main printeaza "[main] gata".
// =============================================================
public class Ex202 {
    // PAS 1: declara `static volatile boolean opreste = false;`
    // TODO
    static volatile boolean opreste = false;

    public static void main(String[] args) throws InterruptedException {
        // PAS 2: Thread worker care face while(!opreste) iteratii++;
        // si printeaza nr de iteratii la final.
        // TODO
            Thread worker = new Thread(() -> {
                long iteratii = 0;
                while (!opreste) {
                    iteratii++;
                }
                System.out.println("[worker]oprit dupa: " + iteratii+" iteratii");
            });

        // PAS 3: worker.start(); Thread.sleep(500); opreste = true; worker.join();
        // TODO
        worker.start();

        Thread.sleep(500);
        opreste = true;
        worker.join();

        // PAS 4: System.out.println("[main] gata");
        // TODO
        System.out.println("[main] gata");
    }
}
