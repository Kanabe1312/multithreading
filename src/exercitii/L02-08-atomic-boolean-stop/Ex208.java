// =============================================================
// EXERCITIUL 08 - AtomicBoolean ca buton de stop
// =============================================================
// Pornesti 3 threaduri "ceasornic" care printeaza "[ceas-N] tick"
// la fiecare 100 ms, atat timp cat un AtomicBoolean `ruleaza` e true.
//
// Main asteapta 500 ms, apoi face ruleaza.set(false). Toate cele 3
// threaduri trebuie sa se opreasca curand dupa.
//
// Folosesti `ruleaza.get()` in conditia de while.
// Folosesti `try/catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }`
// in jurul lui Thread.sleep(100).
// =============================================================

// PAS 1: import java.util.concurrent.atomic.AtomicBoolean;
// TODO

public class Ex208 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: AtomicBoolean ruleaza = new AtomicBoolean(true);
        // TODO

        // PAS 3: porneste 3 threaduri intr-un Thread[] ceasuri = new Thread[3];
        // Fiecare thread are un index `i` capturat (foloseste un "final int idx = i;")
        // si in run() face: while (ruleaza.get()) { print "[ceas-idx] tick"; Thread.sleep(100); }
        // TODO

        // PAS 4: dupa start-uri, Thread.sleep(500)
        // TODO

        // PAS 5: ruleaza.set(false); print "[main] am cerut stop"
        // TODO

        // PAS 6: join() pe toate ceasurile; print "[main] toate ceasurile s-au oprit"
        // TODO
    }
}
