import java.util.concurrent.atomic.AtomicBoolean;

// Exemplul C.1 — `AtomicBoolean` ca flag controlat din alt thread.
//
// Folosim AtomicBoolean cand:
//   - vrem visibility intre threaduri (ca la volatile)
//   - DAR vrem si compareAndSet (de ex. "porneste daca nu e deja pornit")
//
// Pentru un simplu on/off, `volatile boolean` ajunge. AtomicBoolean adauga
// CAS si seamana ca API cu celelalte clase atomice.
public class L2C1 {
    public static void main(String[] args) throws InterruptedException {
        AtomicBoolean ruleaza = new AtomicBoolean(true);

        Thread worker = new Thread(() -> {
            int pasi = 0;
            while (ruleaza.get()) {
                pasi++;
            }
            System.out.println("[worker] oprit dupa " + pasi + " pasi");
        });

        worker.start();
        Thread.sleep(300);

        // CAS: opreste-l doar daca inca rula
        boolean oprit = ruleaza.compareAndSet(true, false);
        System.out.println("[main] am oprit acum? " + oprit);

        worker.join();
    }
}
