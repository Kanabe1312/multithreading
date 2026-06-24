// =============================================================
// EXERCITIUL 03 - Bounded buffer cu Condition
// =============================================================
// Implementezi o coada marginita (capacitate 3) cu un ReentrantLock si
// DOUA Condition: `nuEPlina` si `nuEGoala`.
//
//   pune(x): cat timp coada e plina -> nuEPlina.await();
//            adaugi x; nuEGoala.signal();
//   ia():    cat timp coada e goala -> nuEGoala.await();
//            scoti x; nuEPlina.signal(); return x;
//
// IMPORTANT: await() MEREU in `while`, nu `if`. Tot codul intre lock()
// si unlock(), cu unlock in finally.
//
// Scenariu: 1 producator pune 1..10, 1 consumator ia 10 valori.
// Asteptat: consumatorul primeste 1..10 in ordine; coada nu depaseste 3.
// =============================================================

// PAS 1: importuri
//   import java.util.LinkedList; import java.util.Queue;
//   import java.util.concurrent.locks.Condition;
//   import java.util.concurrent.locks.ReentrantLock;
// TODO

public class Ex303 {
    // PAS 2: campuri statice:
    //   static final int CAP = 3;
    //   static final Queue<Integer> coada = new LinkedList<>();
    //   static final ReentrantLock lock = new ReentrantLock();
    //   static final Condition nuEPlina = lock.newCondition();
    //   static final Condition nuEGoala = lock.newCondition();
    // TODO

    public static void main(String[] args) throws InterruptedException {
        // PAS 5: thread producator (for 1..10 -> pune(i), print) +
        //        thread consumator (for 1..10 -> ia(), print)
        // PAS 6: start + join pe ambele
        // TODO
    }

    // PAS 3: static void pune(int x) throws InterruptedException { ... }
    // PAS 4: static int ia() throws InterruptedException { ... }
    // TODO
}
