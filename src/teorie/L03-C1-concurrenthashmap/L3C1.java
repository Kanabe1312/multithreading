import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Exemplul C.1 — ConcurrentHashMap: map thread-safe FARA sa pui synchronized.
//
// Un HashMap obisnuit, accesat din mai multe threaduri care scriu, se poate
// corupe (bucle infinite, date pierdute). Solutia veche era
// `Collections.synchronizedMap(...)` — dar aceea blocheaza TOT map-ul la
// fiecare operatie.
//
// ConcurrentHashMap permite acces concurent fara sa blocheze tot map-ul:
//   - operatiile individuale (put/get/remove) sunt atomice
//   - mai multe threaduri pot lucra pe chei diferite in paralel
//
// Metode utile care evita "check-then-act" (citeste apoi modifica):
//   putIfAbsent(k, v) -> pune doar daca nu exista cheia
//   getOrDefault(k, d)-> get sau o valoare default
//   compute / merge   -> update atomic (vezi C.2)
public class L3C1 {
    public static void main(String[] args) {
        Map<String, Integer> stoc = new ConcurrentHashMap<>();
        stoc.put("mere", 10);
        stoc.put("pere", 5);

        // putIfAbsent: pune DOAR daca nu exista deja cheia
        stoc.putIfAbsent("mere", 999);    // ignorat — "mere" exista deja
        stoc.putIfAbsent("banane", 7);    // adaugat — "banane" lipsea

        System.out.println("mere   = " + stoc.get("mere"));      // 10
        System.out.println("banane = " + stoc.get("banane"));    // 7
        System.out.println("kiwi   = " + stoc.getOrDefault("kiwi", 0)); // 0
        System.out.println("Tot stocul: " + stoc);

        // ATENTIE: "citeste apoi scrie" pe doi pasi NU e atomic:
        //   Integer v = stoc.get("mere"); stoc.put("mere", v + 1);
        // doua threaduri pot citi acelasi v => o incrementare pierduta.
        // Pentru asta exista merge()/compute() — vezi exemplul C.2.
    }
}
