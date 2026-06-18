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

import java.util.concurrent.atomic.AtomicReference;
// TODO

public class Ex207 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 2: AtomicReference<Integer> max = new AtomicReference<>(Integer.MIN_VALUE);
        // TODO
        AtomicReference<Integer> max = new AtomicReference<>(Integer.MIN_VALUE);
        // PAS 3: int[] valori = {7, 3, 12, 9, 5}; un Thread[] t = new Thread[5];
        // Fiecare thread apeleaza incearcaMaxim(max, valori[i]);
        // TODO
        int[] valori = {7,3,12,9,5};
        Thread[] t = new Thread[5];
        for (int i = 0; i < valori.length; i++) {
            int val = valori[i];
            t[i] = new Thread(() -> {incearcaMaxim(max,val);});
        }
        // PAS 4: start + join pe toate
        // TODO
        for(Thread thread : t){
            thread.start();
        }
        for(Thread thread : t){
            thread.join();
        }
        // PAS 5: print "Asteptat: 12" si "Real: " + max.get()
        // TODO
        System.out.println("Asteptat: 12 ");
        System.out.println("Real: " + max.get());
    }

    // PAS 6: metoda statica `static void incearcaMaxim(AtomicReference<Integer> max, int val)`
    // cu spin CAS pana setezi corect maximul (sau val nu e mai mare).
    // TODO

    static void incearcaMaxim(AtomicReference<Integer>max,int val){
        while(true){
            Integer current = max.get();
            if(val <= current){
                break;
            }
            if(max.compareAndSet(current,val)){
                break;
            }
        }
    }


}
