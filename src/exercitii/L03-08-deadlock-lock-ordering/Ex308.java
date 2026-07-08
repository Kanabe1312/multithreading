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

import java.util.concurrent.locks.ReentrantLock;
// TODO

public class Ex308 {
    // PAS 2: clasa Cont cu:
    //   - final int id;
    //   - int sold;
    //   - final ReentrantLock lock = new ReentrantLock();
    //   - constructor (id, sold)
    // TODO


    static class Cont{
        final int id ;
        int sold;
        final ReentrantLock lock = new ReentrantLock();
        Cont(int id, int sold) {
            this.id = id;
            this.sold = sold;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        // PAS 4: Cont a = new Cont(1, 1000); Cont b = new Cont(2, 1000);
        Cont a = new Cont(1,1000);
        Cont b = new Cont(2,1000);
        // PAS 5: thread1 face de multe ori transfer(a, b, 1);
        //        thread2 face de multe ori transfer(b, a, 1);
        Thread t1 = new Thread(() -> {
            for(int i = 0;i < 100000;i++){
                transfer(a,b,1);
            }
        },"T1");
        Thread t2 = new Thread(() -> {
            for(int i = 0;i < 100000;i++){
                transfer(b,a,1);
            }
        },"T");


        // PAS 6: start + join; print soldurile + suma totala (trebuie 2000)
        // TODO
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Sold A: " + a.sold);
        System.out.println("Sold B: " + b.sold);
        System.out.println("Total: "+ (a.sold+b.sold));
    }


    static void transfer(Cont din,Cont in,int suma ){
            Cont primul = (din.id < in.id) ? din : in;
            Cont aldoilea = (din.id < in.id) ? din : in;
            primul.lock.lock();
            try {
                aldoilea.lock.lock();
                try {
                    din.sold -= suma;
                    in.sold += suma;
                }finally {
                    aldoilea.lock.unlock();
                }
            }finally {
                primul.lock.unlock();
            }
       }



}
