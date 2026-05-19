// =============================================================
// EXERCITIUL 10 - synchronized method
// =============================================================
// Aceeasi structura ca exercitiul 09, dar acum incrementeaza() si
// getCount() sunt declarate synchronized. Acum rezultatul e mereu 200.000.
//
// Intrebare: de ce trebuie si getCount() sa fie synchronized?
// (Raspuns: memory visibility.)
// =============================================================
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // PAS 1: copiezi structura din exercitiul 09
        // TODO

        // PAS 2: la Contor, pui synchronized pe incrementeaza() SI pe getCount()
        // TODO
    }

    // static class Contor {
    //     private int count = 0;
    //     public synchronized void incrementeaza() { count++; }
    //     public synchronized int getCount() { return count; }
    // }
    // TODO
}
