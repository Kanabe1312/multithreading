// =============================================================
// EXERCITIUL 05 - Frecventa cuvinte cu ConcurrentHashMap.merge()
// =============================================================
// Numeri de cate ori apare fiecare cuvant, din mai multe threaduri, fara
// sa pierzi incrementari.
//
// GRESIT (check-then-act):
//   Integer v = map.get(c); map.put(c, (v==null?0:v)+1);   // race!
// CORECT:
//   map.merge(c, 1, Integer::sum);                          // atomic
//
// Scenariu: ai un sir de cuvinte. Pornesti 4 threaduri, fiecare parcurge
// ACELASI sir si face merge pentru fiecare cuvant.
//
// Asteptat: fiecare cuvant numarat de 4x (4 threaduri). Mereu acelasi
// rezultat, indiferent de ordine.
// =============================================================

// PAS 1: import java.util.concurrent.ConcurrentHashMap;
// TODO

public class Ex305 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: ConcurrentHashMap<String,Integer> frecventa = new ConcurrentHashMap<>();
        // PAS 3: String[] cuvinte = {"mar","para","mar","kiwi","para","mar"};
        // PAS 4: Runnable job = parcurge cuvinte si face frecventa.merge(c, 1, Integer::sum);
        // PAS 5: 4 threaduri cu acelasi job; start + join pe toate
        //        (pune-le intr-un Thread[] t = new Thread[4];)
        // PAS 6: print frecventa
        //        (Asteptat: mar=12, para=8, kiwi=4)
        // TODO
    }
}
