// =============================================================
// EXERCITIUL 05 - Runnable cu parametri
// =============================================================
// Creezi clasa SalutatorCuNume care primeste un String in constructor
// si printeaza "Salut, <nume>!".
// Pornesti 3 threaduri cu nume diferite: "Ana", "Bogdan", "Cristi".
// =============================================================
public class Ex05 {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new SalutatorCuNume("Ana"));
        Thread t2 = new Thread(new SalutatorCuNume("Bogdan"));
        Thread t3 = new Thread(new SalutatorCuNume("Cristi"));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

    }

    static class SalutatorCuNume implements Runnable{

        private  final String nume;

        public SalutatorCuNume(String nume){

            this.nume = nume;
        }
        @Override
        public void run(){
            System.out.println("Salut," + nume + "!");
        }

    }

}
