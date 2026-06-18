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
import java.util.concurrent.atomic.AtomicBoolean;
// TODO

public class Ex208 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: AtomicBoolean ruleaza = new AtomicBoolean(true);
        // TODO
        AtomicBoolean ruleaza = new AtomicBoolean(true);//pleaca cu true

        // PAS 3: porneste 3 threaduri intr-un Thread[] ceasuri = new Thread[3];
        // Fiecare thread are un index `i` capturat (foloseste un "final int idx = i;")
        // si in run() face: while (ruleaza.get()) { print "[ceas-idx] tick"; Thread.sleep(100); }
        // TODO
        Thread[] ceasuri = new Thread[3];//3 ceasuri---cat timp avem true
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            ceasuri[i] = new Thread(() -> {
               while (ruleaza.get()) {//creste valoarea
                   System.out.println("[ceas-"+idx+"]tick");
                   try {
                       Thread.sleep(100);
                   } catch (InterruptedException e) {
                       Thread.currentThread().interrupt();
                       return;
                   }
               }

            });
        }
        // PAS 4: dupa start-uri, Thread.sleep(500)
        // TODO
        for(Thread t : ceasuri) {
            t.start();
        }
        Thread.sleep(500);//----main seteaza false si threaduriile ies
        // PAS 5: ruleaza.set(false); print "[main] am cerut stop"
        // TODO
        ruleaza.set(false);//pune false
        System.out.println("[main] am cerut stop!");

        // PAS 6: join() pe toate ceasurile; print "[main] toate ceasurile s-au oprit"
        // TODO
        for(Thread t : ceasuri) {
            t.join();
        }
        System.out.println("[main] toate ceasurile s-au oprit");

    }
}
