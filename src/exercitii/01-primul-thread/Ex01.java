import java.sql.SQLOutput;
import java.util.TreeMap;

// =============================================================
// EXERCITIUL 01 - Primul thread, start vs run, sleep
// =============================================================
// Vezi README.md (sectiunea "Hinturi") pentru detalii complete.
//
// Pasul a) Creezi un thread care printeaza "Salut din alt thread!" si il pornesti cu .start().
// Pasul b) Modifici threadul sa printeze si Thread.currentThread().getName().
//          Pornesti-l o data cu .start(), o data cu .run(). Observa diferenta.
// Pasul c) Creezi 3 threaduri care printeaza, dorm 500ms, apoi printeaza din nou.
//          Prinde InterruptedException.
// =============================================================
public class Ex01 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 1: thread cu lambda + .start() + .join()
        // TODO

        Thread t = new Thread(()-> System.out.println("Salut din alt thread!"+Thread.currentThread().getName()));
        t.start();//-->creaza tread nou
        t.join();//-->main asteapta
        // PAS 2: acelasi task pornit cu .run() — pe ce thread ruleaza?
        // TODO
       Thread t2 = new Thread(()-> System.out.println("Rulez pe: " + Thread.currentThread().getName()));
       t2.start();
       t2.join();
       Thread t3 = new Thread(()-> System.out.println("Rulez pe : "+ Thread.currentThread().getName()));
       t3.run();//-->metoda normala !nu creara thread!




        // PAS 3: 3 threaduri cu sleep(500), prind InterruptedException
        // TODO

        Thread a = new Thread(()-> printDupaPauza("Thread A"));
        Thread b = new Thread(()-> printDupaPauza("Thread B"));
        Thread c = new Thread(()-> printDupaPauza("Thread C"));

        a.start();
        b.start();
        c.start();

        a.join();
        b.join();
        c.join();

    }
    private static void printDupaPauza(String nume){
        System.out.println("["+nume+"] inainte de sleep");
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("[" + nume + "] dupa sleep");
    }

}
