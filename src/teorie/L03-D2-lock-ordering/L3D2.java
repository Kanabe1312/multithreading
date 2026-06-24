import java.util.concurrent.locks.ReentrantLock;

// Exemplul D.2 — FIX prin LOCK ORDERING (acelasi scenariu ca D.1, dar reparat).
//
// Cauza deadlock-ului din D.1 era ca cele doua threaduri luau lock-urile in
// ordini DIFERITE. Solutia principala: stabileste o ordine GLOBALA (aici
// dupa `id`) si ia MEREU lock-urile in acea ordine, indiferent din ce sens
// vine treaba. Atunci ciclul de asteptare nu se mai poate forma.
//
// T1 cere (a, b), T2 cere (b, a) — dar `lucreaza()` le reordoneaza dupa id,
// deci ambele iau intai resursa cu id mai mic. Programul se TERMINA.
public class L3D2 {
    static class Resursa {
        final int id;
        final ReentrantLock lock = new ReentrantLock();
        Resursa(int id) { this.id = id; }
    }

    static void lucreaza(Resursa x, Resursa y) {
        Resursa primul   = (x.id < y.id) ? x : y;     // ordine globala dupa id
        Resursa aldoilea = (x.id < y.id) ? y : x;
        primul.lock.lock();
        try {
            aldoilea.lock.lock();
            try {
                // sectiune critica cu ambele resurse
            } finally {
                aldoilea.lock.unlock();
            }
        } finally {
            primul.lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Resursa a = new Resursa(1);
        Resursa b = new Resursa(2);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) lucreaza(a, b);
        }, "T1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) lucreaza(b, a);   // ordine logica inversa
        }, "T2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Gata dupa 2.000.000 de operatii — fara deadlock (lock ordering).");
    }
}
