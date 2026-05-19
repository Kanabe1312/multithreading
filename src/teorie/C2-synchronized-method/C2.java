// Exemplul C.2 — synchronized method = lock implicit pe `this`.
// Doar un thread la un moment dat poate executa metoda. Acum mereu 200.000.
public class C2 {
    public static void main(String[] args) throws InterruptedException {
        Contor c = new Contor();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                c.incrementeaza();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                c.incrementeaza();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Asteptat: 200000");
        System.out.println("Real:     " + c.getCount() + "   (mereu 200000)");
    }

    static class Contor {
        private int count = 0;

        public synchronized void incrementeaza() {
            count++;
        }

        // getter-ul si el synchronized => memory visibility
        public synchronized int getCount() {
            return count;
        }
    }
}
