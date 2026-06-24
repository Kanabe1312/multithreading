// =============================================================
// EXERCITIUL 08 - Deadlock si cum il eviti prin lock ordering
// =============================================================
// Ai doua conturi, fiecare cu propriul ReentrantLock. Faci transferuri
// in ambele sensuri din doua threaduri.
//
// PARTEA A — provoaca deadlock-ul (ca sa-l intelegi):
//   transfer(A -> B): lock A, apoi lock B
//   transfer(B -> A): lock B, apoi lock A
//   Daca threadul 1 ia A si threadul 2 ia B in acelasi timp, fiecare
//   asteapta lock-ul celuilalt -> programul ATARNA (deadlock).
//   (Daca nu se blocheaza din prima, ruleaza de cateva ori / mareste bucla.)
//
// PARTEA B — fixezi prin LOCK ORDERING:
//   Iei MEREU lock-urile in aceeasi ordine globala (ex: dupa id-ul contului,
//   intai cel cu id mai mic). Asa nu se mai poate forma ciclul de asteptare.
//   Foloseste tot try/finally pentru fiecare unlock.
//
// Asteptat (partea B): programul se termina; suma totala din conturi e
// constanta inainte si dupa transferuri (banii nu se pierd / nu se dubleaza).
// =============================================================

// PAS 1: import java.util.concurrent.locks.ReentrantLock;
// TODO

public class Ex308 {
    // PAS 2: clasa Cont cu:
    //   - final int id;
    //   - int sold;
    //   - final ReentrantLock lock = new ReentrantLock();
    //   - constructor (id, sold)
    // TODO

    public static void main(String[] args) throws InterruptedException {
        // PAS 4: Cont a = new Cont(1, 1000); Cont b = new Cont(2, 1000);
        // PAS 5: thread1 face de multe ori transfer(a, b, 1);
        //        thread2 face de multe ori transfer(b, a, 1);
        // PAS 6: start + join; print soldurile + suma totala (trebuie 2000)
        // TODO
    }

    // PAS 3: static void transfer(Cont din, Cont in, int suma)
    //   Varianta SIGURA (lock ordering):
    //     Cont primul  = (din.id < in.id) ? din : in;
    //     Cont aldoilea= (din.id < in.id) ? in : din;
    //     primul.lock.lock();
    //     try {
    //         aldoilea.lock.lock();
    //         try { din.sold -= suma; in.sold += suma; }
    //         finally { aldoilea.lock.unlock(); }
    //     } finally { primul.lock.unlock(); }
    // TODO
}
