// =============================================================
// EXERCITIUL 03 - Daemon thread
// =============================================================
// Creeaza un thread care printeaza "tick" la fiecare 100ms intr-o
// bucla infinita. Marcheaza-l ca daemon cu setDaemon(true) INAINTE
// de start(). Apoi main doarme 500ms si termina.
//
// Intrebare: ce s-ar intampla daca nu ar fi daemon?
// =============================================================
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // PAS 1: thread cu while(true) { print "[tick]"; sleep(100); }
        //         (prinde InterruptedException si return)
        // TODO

        // PAS 2: setDaemon(true) INAINTE de start()
        // TODO

        // PAS 3: main doarme 500ms
        // TODO
    }
}
