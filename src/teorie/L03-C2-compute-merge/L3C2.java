import java.util.concurrent.ConcurrentHashMap;

// Exemplul C.2 — merge() / compute(): update COMPUS atomic pe ConcurrentHashMap.
//
// "Citeste-modifica-scrie" intr-un singur pas atomic, fara lock manual.
//
// Bug clasic (check-then-act):
//     Integer v = map.get(k);
//     map.put(k, (v == null ? 0 : v) + 1);
//   -> doua threaduri citesc acelasi v, ambele scriu v+1 => o incrementare PIERDUTA.
//
// Solutia: merge(k, 1, Integer::sum)
//   - daca k lipseste -> pune 1
//   - daca k exista   -> aplica functia (vechi, 1) -> vechi + 1
//   - totul ATOMIC pentru cheia k.
public class L3C2 {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Integer> frecventa = new ConcurrentHashMap<>();
        String[] cuvinte = {"a", "b", "a", "c", "b", "a"};

        Runnable job = () -> {
            for (String c : cuvinte) {
                frecventa.merge(c, 1, Integer::sum);   // atomic
            }
        };

        // 2 threaduri parcurg fiecare acelasi sir => fiecare cuvant numarat de 2x
        Thread t1 = new Thread(job);
        Thread t2 = new Thread(job);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Asteptat: a=6, b=4, c=2 (3+3, 2+2, 1+1). Mereu, fara incrementari pierdute.
        System.out.println(frecventa);

        // Varianta cu compute() — primesti cheia si valoarea curenta (poate fi null):
        frecventa.compute("a", (k, v) -> (v == null ? 0 : v) + 100);
        System.out.println("dupa compute a += 100: " + frecventa.get("a")); // 106
    }
}
