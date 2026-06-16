import java.util.concurrent.atomic.AtomicInteger;

// Exemplul B.2 — Diferenta intre getAndIncrement() si incrementAndGet().
//
//   getAndIncrement() => returneaza valoarea VECHE, apoi adauga 1
//   incrementAndGet() => adauga 1, apoi returneaza valoarea NOUA
//
// Analog cu i++ vs ++i (post-increment vs pre-increment).
public class L2B2 {
    public static void main(String[] args) {
        AtomicInteger a = new AtomicInteger(10);

        int x = a.getAndIncrement();
        System.out.println("getAndIncrement: returnat=" + x + ", acum=" + a.get());

        AtomicInteger b = new AtomicInteger(10);

        int y = b.incrementAndGet();
        System.out.println("incrementAndGet: returnat=" + y + ", acum=" + b.get());

        // Aplicatie tipica: getAndIncrement pentru ID-uri unice.
        AtomicInteger idGen = new AtomicInteger(0);
        System.out.println("id #" + idGen.getAndIncrement()); // 0
        System.out.println("id #" + idGen.getAndIncrement()); // 1
        System.out.println("id #" + idGen.getAndIncrement()); // 2
    }
}
