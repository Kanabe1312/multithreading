// =============================================================
// EXERCITIUL 06 - Grupare cu computeIfAbsent
// =============================================================
// Grupezi niste nume dupa litera initiala, intr-un
// ConcurrentHashMap<Character, List<String>>, din mai multe threaduri.
//
// `computeIfAbsent(cheie, k -> valoareNoua)`:
//   - daca cheia lipseste -> creeaza valoarea (aici: o lista) si o pune ATOMIC
//   - returneaza lista existenta sau cea nou creata
//
// ATENTIE: lista din interior trebuie sa fie thread-safe daca mai multe
// threaduri adauga la aceeasi litera. Foloseste:
//   map.computeIfAbsent(litera, k -> new CopyOnWriteArrayList<>()).add(nume);
//
// Scenariu: ai un array de nume. 3 threaduri le proceseaza (acelasi array),
// fiecare adauga numele la grupa literei initiale.
//
// Asteptat: fiecare nume apare de 3x in grupa lui (3 threaduri).
// =============================================================

// PAS 1: importuri
//   import java.util.List;
//   import java.util.concurrent.ConcurrentHashMap;
//   import java.util.concurrent.CopyOnWriteArrayList;
// TODO

public class Ex306 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: ConcurrentHashMap<Character, List<String>> grupe = new ConcurrentHashMap<>();
        // PAS 3: String[] nume = {"Ana","Andrei","Bogdan","Cristi","Alex","Bianca"};
        // PAS 4: Runnable job: pentru fiecare nume ->
        //        char litera = nume[k].charAt(0);
        //        grupe.computeIfAbsent(litera, x -> new CopyOnWriteArrayList<>()).add(nume[k]);
        // PAS 5: 3 threaduri cu acelasi job; start + join
        // PAS 6: print grupe (sau parcurge si afiseaza litera -> lista)
        // TODO
    }
}
