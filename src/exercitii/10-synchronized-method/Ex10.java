// =============================================================
// EXERCITIUL 10 - synchronized method
// =============================================================
// Aceeasi structura ca exercitiul 09, dar acum incrementeaza() si
// getCount() sunt declarate synchronized. Acum rezultatul e mereu 200.000.
//
// Intrebare: de ce trebuie si getCount() sa fie synchronized?
// (Raspuns: memory visibility.)
// =============================================================
public class Ex10 {
    public static void main(String[] args) throws InterruptedException {
        Contor contor = new Contor();

        Thread t1 = new Thread(()-> {
            for (int i = 0;i<100_000;i++){
                contor.incrementeaza();
            }
        });

        Thread t2 = new Thread(()-> {
            for(int i = 0; i <100_000;i++){
                contor.incrementeaza();
            }
        });

        t1.start();
        t2.start();

        t1.join();//asteapta
        t2.join();

        System.out.println("Asteptat: 200k");
        System.out.println("\nReal: "+ contor.getCount());

    }
    static class Contor{
        private int count = 0;
        public synchronized void incrementeaza(){
            count++;
        }
        public synchronized int getCount()
        {
            return count;
        }

    }
}
