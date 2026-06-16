// Exemplul A.3 — `volatile` NU rezolva count++ (operatie compusa).
//
// `volatile` da visibility (alte threaduri vad scrierile), dar count++
// e format din 3 pasi: citeste, +1, scrie. Doua threaduri pot citi aceeasi
// valoare in acelasi timp si pierd o incrementare.
//
// Concluzie: volatile e bun doar pentru flag-uri / referinte unde scrierea
// e o singura operatie. Pentru contoare folosim AtomicInteger.
public class L2A3 {
    static volatile int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                count++; // 3 pasi, nu e atomic chiar daca e volatile
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                count++;
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Asteptat: 200000");
        System.out.println("Real:     " + count + "  <-- aproape mereu < 200000");
    }
}
