// =============================================================
// EXERCITIUL 04 - Runnable cu clasa proprie
// =============================================================
// Creezi o clasa Salutator care implementeaza Runnable.
// In run() printezi "Salut din Salutator!".
// Apoi din main: new Thread(new Salutator()).start();
// =============================================================
public class Ex04 {
    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread(new Salutator());



        t.start();

        t.join();
    }
    static class Salutator implements Runnable {

        @Override
        public void run() {

            System.out.println("Salut din Salutator!");

        }

    }
}
