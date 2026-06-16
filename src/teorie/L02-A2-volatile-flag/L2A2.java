// Exemplul A.2 — `volatile` rezolva visibility-ul.
//
// `volatile` spune JVM-ului:
//   - citirile/scrierile pe `gata` se duc DIRECT in main memory (heap)
//   - nu se cache-uiesc in registre / cache CPU per-thread
//
// Important: `volatile` NU face operatiile compuse atomice. Aici merge
// pentru ca facem doar citire / scriere simpla, nu count++ sau ceva similar.
public class L2A2 {
    static volatile boolean gata = false;

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

        worker.join();
        System.out.println("[main] gata");
    }
}
