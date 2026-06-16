// =============================================================
// EXERCITIUL 07 - AtomicReference pentru "maximum vazut"
// =============================================================
// Ai un `AtomicReference<Integer>` care tine maximul vazut din mai
// multe threaduri. Fiecare thread incearca sa actualizeze maximul
// cu o valoare proprie folosind un spin CAS:
//
//   while (true) {
//       Integer curent = max.get();
//       if (val <= curent) break;                       // nu e mai mare
//       if (max.compareAndSet(curent, val)) break;      // am reusit
//       // altfel altcineva a schimbat max, reincerc
//   }
//
// Pornesti 5 threaduri cu valorile {7, 3, 12, 9, 5}.
// Asteptat: max = 12.
// =============================================================

// PAS 1: import java.util.concurrent.atomic.AtomicReference;
// TODO

public class Ex207 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: AtomicReference<Integer> max = new AtomicReference<>(Integer.MIN_VALUE);
        // TODO

        // PAS 3: int[] valori = {7, 3, 12, 9, 5}; un Thread[] t = new Thread[5];
        // Fiecare thread apeleaza incearcaMaxim(max, valori[i]);
        // TODO

        // PAS 4: start + join pe toate
        // TODO

        // PAS 5: print "Asteptat: 12" si "Real: " + max.get()
        // TODO
    }

    // PAS 6: metoda statica `static void incearcaMaxim(AtomicReference<Integer> max, int val)`
    // cu spin CAS pana setezi corect maximul (sau val nu e mai mare).
    // TODO
}
