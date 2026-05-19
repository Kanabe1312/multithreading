// =============================================================
// EXERCITIUL 01 - Primul thread, start vs run, sleep
// =============================================================
// Vezi README.md (sectiunea "Hinturi") pentru detalii complete.
//
// Pasul a) Creezi un thread care printeaza "Salut din alt thread!" si il pornesti cu .start().
// Pasul b) Modifici threadul sa printeze si Thread.currentThread().getName().
//          Pornesti-l o data cu .start(), o data cu .run(). Observa diferenta.
// Pasul c) Creezi 3 threaduri care printeaza, dorm 500ms, apoi printeaza din nou.
//          Prinde InterruptedException.
// =============================================================
public class Ex01 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 1: thread cu lambda + .start() + .join()
        // TODO

        // PAS 2: acelasi task pornit cu .run() — pe ce thread ruleaza?
        // TODO

        // PAS 3: 3 threaduri cu sleep(500), prind InterruptedException
        // TODO
    }
}
