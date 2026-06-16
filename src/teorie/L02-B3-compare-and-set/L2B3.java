import java.util.concurrent.atomic.AtomicInteger;

// Exemplul B.3 — Compare-And-Set (CAS).
//
// `compareAndSet(expected, newValue)`:
//   - daca valoarea curenta == expected  => o schimba in newValue, returneaza true
//   - altfel                              => nu schimba nimic,   returneaza false
//
// E baza tuturor operatiilor atomice. Permite "spin loop":
// incercam sa actualizam, daca altcineva a schimbat valoarea, reincercam.
public class L2B3 {
    public static void main(String[] args) {
        AtomicInteger v = new AtomicInteger(10);

        // Reusita: 10 == expected
        boolean ok1 = v.compareAndSet(10, 99);
        System.out.println("CAS(10 -> 99): success=" + ok1 + ", acum=" + v.get());

        // Esueaza: e 99, nu 10
        boolean ok2 = v.compareAndSet(10, 200);
        System.out.println("CAS(10 -> 200): success=" + ok2 + ", acum=" + v.get());

        // Exemplu: dublu pana cand reusim (spin loop).
        v.set(5);
        while (true) {
            int curent = v.get();
            int dublu = curent * 2;
            if (v.compareAndSet(curent, dublu)) {
                break; // nimeni n-a schimbat intre timp => am reusit
            }
            // altfel: altcineva a schimbat v, reincercam
        }
        System.out.println("Dupa CAS-spin (5 -> dublu): " + v.get()); // 10
    }
}
