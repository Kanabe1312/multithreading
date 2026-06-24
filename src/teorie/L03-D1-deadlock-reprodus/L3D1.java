import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.locks.ReentrantLock;

// Exemplul D.1 — DEADLOCK reprodus deterministic (si DETECTAT, ca sa nu atarne).
//
// Doua threaduri iau aceleasi doua lock-uri, dar in ordine INVERSA:
//   T1: lock A, apoi vrea B
//   T2: lock B, apoi vrea A
// Daca fiecare apuca primul lock si abia apoi il cere pe al doilea, se
// blocheaza reciproc -> niciunul nu mai avanseaza = DEADLOCK.
//
// Sleep-ul intre cele doua lock()-uri face blocarea SIGURA (T2 apuca B cat
// timp T1 doarme dupa A). In realitate un deadlock atarna programul la
// nesfarsit; aici un thread "detector" foloseste ThreadMXBean ca sa-l
// raporteze si inchidem JVM-ul (System.exit) ca demo-ul sa se termine.
public class L3D1 {
    static final ReentrantLock lockA = new ReentrantLock();
    static final ReentrantLock lockB = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            lockA.lock();
            try {
                System.out.println("[T1] am A, vreau si B...");
                dormi(200);              // las T2 sa apuce B
                lockB.lock();
                try {
                    System.out.println("[T1] am A si B");
                } finally {
                    lockB.unlock();
                }
            } finally {
                lockA.unlock();
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            lockB.lock();
            try {
                System.out.println("[T2] am B, vreau si A...");
                dormi(200);              // las T1 sa apuce A
                lockA.lock();
                try {
                    System.out.println("[T2] am B si A");
                } finally {
                    lockA.unlock();
                }
            } finally {
                lockB.unlock();
            }
        }, "T2");

        t1.setDaemon(true);              // ca JVM-ul sa nu fie tinut viu de threadurile blocate
        t2.setDaemon(true);
        t1.start();
        t2.start();

        // Detector: dupa o secunda verificam daca s-a format un deadlock.
        Thread.sleep(1000);
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] blocate = bean.findDeadlockedThreads();

        if (blocate == null) {
            System.out.println("De data asta nu s-a format deadlock (timing). Mai ruleaza.");
            return;
        }

        System.out.println("\nDEADLOCK detectat pe " + blocate.length + " threaduri:");
        for (ThreadInfo info : bean.getThreadInfo(blocate)) {
            System.out.println("  " + info.getThreadName()
                    + " asteapta lock-ul detinut de " + info.getLockOwnerName());
        }
        System.out.println("(in realitate programul ar atarna aici la nesfarsit)");
        System.exit(0);
    }

    static void dormi(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
