// =============================================================
// EXERCITIUL 07 - Lista de listeneri cu CopyOnWriteArrayList
// =============================================================
// Simulezi un sistem de "abonati" (listeneri). Un thread itereaza lista
// si "notifica" fiecare abonat, in timp ce alt thread adauga abonati noi.
//
// Cu un ArrayList obisnuit -> ConcurrentModificationException.
// Cu CopyOnWriteArrayList -> iterarea merge pe un snapshot, fara exceptie.
//
// Scenariu:
//   - pornesti cu 2 abonati ("Ana", "Bogdan")
//   - thread CITITOR: itereaza lista si printeaza "notific X" (cu un mic sleep)
//   - thread SCRIITOR: adauga "Cristi", "Dan", "Elena"
//
// Asteptat: niciun ConcurrentModificationException; la final size() == 5.
// =============================================================

// PAS 1: importuri
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
// TODO

public class Ex307 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: List<String> abonati = new CopyOnWriteArrayList<>();
        //        abonati.add("Ana"); abonati.add("Bogdan");
        // PAS 3: thread cititor: for (String a : abonati) { print "notific "+a; sleep(20); }
        //        (prinde InterruptedException)
        // PAS 4: thread scriitor: adauga "Cristi", "Dan", "Elena"
        // PAS 5: start + join pe ambele
        // PAS 6: print "Total abonati: " + abonati.size()
        // TODO

        List<String> abonati = new CopyOnWriteArrayList<>();
        abonati.add("Ana");
        abonati.add("Bogdan");

        Thread cititor = new Thread(()->{
            try {
                for(String a : abonati){
                    System.out.println("Notific " + a);
                    Thread.sleep(20);
                }
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        });
        Thread scriitor = new Thread(()->{
           abonati.add("Cristi");
           abonati.add("Dan");
           abonati.add("Elena");
        });
        cititor.start();
        scriitor.start();
        cititor.join();
        scriitor.join();

        System.out.println("Total abonati : " + abonati.size());
    }
}
