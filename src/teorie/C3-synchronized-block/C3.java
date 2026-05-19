import java.util.ArrayList;
import java.util.List;

// Exemplul C.3 — synchronized block cu lock dedicat (alt obiect decat `this`).
// Util cand vrei sa controlezi fin ce parti se sincronizeaza.
public class C3 {
    public static void main(String[] args) throws InterruptedException {
        Cos cos = new Cos();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                cos.adauga("p" + i);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                cos.adauga("q" + i);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Total produse: " + cos.dimensiune() + " (asteptat: 200)");
    }

    static class Cos {
        private final List<String> produse = new ArrayList<>();
        private final Object lock = new Object();

        public void adauga(String p) {
            synchronized (lock) {
                produse.add(p);
            }
        }

        public int dimensiune() {
            synchronized (lock) {
                return produse.size();
            }
        }
    }
}
