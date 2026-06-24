import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// Exemplul A.3 — Condition: await()/signal() pentru a ASTEPTA o conditie.
//
// Cu un ReentrantLock poti crea "conditii" (lock.newCondition()). Un thread
// care detine lock-ul poate:
//   - cond.await()  -> elibereaza lock-ul si adoarme pana il trezeste cineva
//   - cond.signal() -> trezeste un thread care asteapta pe acea conditie
//
// E echivalentul lui wait()/notify() de pe synchronized, dar poti avea MAI
// MULTE conditii pe acelasi lock (aici: "nu e plina" si "nu e goala").
//
// Demonstram un buffer marginit (bounded buffer): producatorul se opreste
// cand coada e plina, consumatorul cand e goala.
//
// IMPORTANT: await() se cheama MEREU intr-un `while` (nu `if`) — la trezire
// reverifici conditia (spurious wakeups + alti consumatori).
public class L3A3 {
    static final int CAPACITATE = 5;
    static final Queue<Integer> coada = new LinkedList<>();
    static final ReentrantLock lock = new ReentrantLock();
    static final Condition nuEPlina = lock.newCondition();
    static final Condition nuEGoala = lock.newCondition();

    static void pune(int x) throws InterruptedException {
        lock.lock();
        try {
            while (coada.size() == CAPACITATE) {
                nuEPlina.await();           // astept sa se elibereze un loc
            }
            coada.add(x);
            nuEGoala.signal();              // anunt consumatorul ca are ce lua
        } finally {
            lock.unlock();
        }
    }

    static int ia() throws InterruptedException {
        lock.lock();
        try {
            while (coada.isEmpty()) {
                nuEGoala.await();           // astept sa apara ceva
            }
            int x = coada.poll();
            nuEPlina.signal();              // anunt producatorul ca s-a eliberat loc
            return x;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread producator = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    pune(i);
                    System.out.println("[P] pus " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumator = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    int x = ia();
                    System.out.println("        [C] luat " + x);
                    Thread.sleep(50); // consum mai lent ca sa se umple coada
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producator.start();
        consumator.start();
        producator.join();
        consumator.join();
        System.out.println("Gata. Ramase in coada: " + coada.size());
    }
}
