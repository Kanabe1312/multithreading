import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// Exemplul C.3 — CopyOnWriteArrayList: lista thread-safe optimizata pentru
// CITIRI dese si scrieri RARE.
//
// La fiecare scriere (add/remove) copiaza intreg array-ul intern. Scump la
// scriere, dar:
//   - citirile NU au nevoie de lock (foarte rapide)
//   - poti itera in timp ce alt thread modifica, FARA ConcurrentModificationException
//     (iteratorul vede un "snapshot" de la momentul cand a inceput)
//
// Tipic pentru: liste de listeneri/abonati, configurari citite des, observeri.
// NU o folosi daca scrii des (un ArrayList copiat la fiecare add e lent).
public class L3C3 {
    public static void main(String[] args) throws InterruptedException {
        List<String> abonati = new CopyOnWriteArrayList<>();
        abonati.add("Ana");
        abonati.add("Bogdan");

        // Un thread itereaza in timp ce altul adauga -> fara exceptie.
        Thread cititor = new Thread(() -> {
            for (String a : abonati) {
                System.out.println("notific " + a);
                try {
                    Thread.sleep(20); // tinem iterarea deschisa mai mult
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread scriitor = new Thread(() -> abonati.add("Cristi"));

        cititor.start();
        scriitor.start();
        cititor.join();
        scriitor.join();

        System.out.println("Total abonati la final: " + abonati.size());
    }
}
