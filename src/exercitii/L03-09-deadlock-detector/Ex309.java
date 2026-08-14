// =============================================================
// EXERCITIUL 09 - Detecteaza deadlock-ul cu ThreadMXBean
// =============================================================
// La Ex08 partea A ai vazut ca un deadlock ATARNA programul. Acum provoci
// acelasi deadlock, dar adaugi un "detector" care il DIAGNOSTICHEAZA si
// inchide programul controlat, in loc sa-l lasi blocat la nesfarsit.
//
// Idee: JVM-ul stie cine pe cine asteapta. Prin
//   ThreadMXBean bean = ManagementFactory.getThreadMXBean();
//   long[] blocate = bean.findDeadlockedThreads();   // null daca nu e deadlock
// poti afla daca s-a format un ciclu de asteptare si pe ce threaduri.
//
// Scenariu:
//   - 2 lock-uri (lockA, lockB)
//   - T1: ia A, doarme putin, apoi vrea B
//   - T2: ia B, doarme putin, apoi vrea A   (ordine INVERSA -> deadlock)
//   - threadurile sunt `daemon` (ca sa nu tina JVM-ul viu dupa System.exit)
//   - in main: dupa ~1s, findDeadlockedThreads(); daca != null, afiseaza
//     fiecare thread blocat + cine ii tine lock-ul (getLockOwnerName()),
//     apoi System.exit(0)
//
// Asteptat: "DEADLOCK detectat pe 2 threaduri" + cele doua linii T1/T2,
// iar programul se termina (nu atarna).
// =============================================================

// PAS 1: importuri
   import java.lang.management.ManagementFactory;
   import java.lang.management.ThreadInfo;
   import java.lang.management.ThreadMXBean;
   import java.util.concurrent.locks.ReentrantLock;
// TODO

public class Ex309 {
     static final ReentrantLock lockA = new ReentrantLock();
     static final ReentrantLock lockB = new ReentrantLock();
    // TODO

    public static void main(String[] args) throws InterruptedException {
        // PAS 3: thread T1 -> lockA.lock(); try { sleep(200); lockB.lock(); try{...}finally{unlock} } finally { unlock }
        // PAS 4: thread T2 -> lockB.lock(); try { sleep(200); lockA.lock(); ... }  (ordine inversa)
        //        ambele: setDaemon(true), apoi start()
        // PAS 5: Thread.sleep(1000);
        //        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        //        long[] blocate = bean.findDeadlockedThreads();
        //        daca blocate == null -> print "nu s-a format deadlock, mai ruleaza" + return
        //        altfel -> print "DEADLOCK detectat pe N threaduri" si pentru fiecare
        //        ThreadInfo din bean.getThreadInfo(blocate) afiseaza
        //        info.getThreadName() + " asteapta lock-ul detinut de " + info.getLockOwnerName();
        //        apoi System.exit(0);
        // TODO

        Thread t1 = new Thread(()->{
            lockA.lock();
            try{
             dormi(200);
             lockB.lock();
                      try {

                      }finally {
                          lockB.unlock();
                      }
            }finally {
                lockA.unlock();
            }
        },"T1");
        Thread t2 = new Thread(()->{
            lockB.lock();
            try{
             dormi(200);
             lockA.lock();
             try {

             }finally {
                 lockA.unlock();
             }
            }finally {
                lockB.unlock();
            }
        },"T2");

        t1.start();
        t2.start();
        Thread.sleep(1000);
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        long[]blocate = mxBean.findDeadlockedThreads();

        if(blocate == null){
            System.out.println("Nu s-a format deadlock.");
            return;
        }

        System.out.println("Deadlock detectat pe "+blocate.length+" threaduri");
        for (long id : blocate) {
            ThreadInfo threadInfo = mxBean.getThreadInfo(id);

            System.out.println(threadInfo.getThreadName() + "asteapta lock-ul detinut de "+threadInfo.getLockOwnerName());
        }
        System.exit(0);












    }

    // (optional) PAS 6: static void dormi(long ms) care prinde InterruptedException,
    //   ca sa nu repeti try/catch in fiecare thread.
    // TODO
    static void dormi(long ms){
        try {
            Thread.sleep(ms);
        }catch(InterruptedException e){}
        Thread.currentThread().interrupt();
    }
}
