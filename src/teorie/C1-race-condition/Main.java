// Exemplul C.1 — Race condition: count++ NU e atomic.
// Asteptat: 200.000. Real: un numar mai mic, nedeterminist.
public class Main {
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
        System.out.println("Real:     " + c.getCount() + "   (< 200000 aproape mereu)");
    }

    static class Contor {
        private int count = 0;

        public void incrementeaza() {
            count++; // race! 3 pasi: citeste, +1, scrie
        }

        public int getCount() {
            return count;
        }
    }
}
