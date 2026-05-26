// Exemplul B.1 — Runnable: cu lambda (concis) vs cu clasa (cu state).
public class B1 {
    public static void main(String[] args) throws InterruptedException {
        // varianta lambda
        Runnable taskLambda = () -> System.out.println("[lambda]");//---creaza reteta
        Thread t0 = new Thread(taskLambda);//----creaza bucatarul
        t0.start();//---start la gatit
        t0.join();

        // varianta cu clasa => util cand vrei state in field
        Thread t1 = new Thread(new SalutatorCuNume("Ana"));
        Thread t2 = new Thread(new SalutatorCuNume("Bogdan"));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static class SalutatorCuNume implements Runnable {
        private final String nume;

        public SalutatorCuNume(String nume) {
            this.nume = nume;
        }

        @Override
        public void run() {
            System.out.println("Salut, " + nume + "!");
        }
    }
}
