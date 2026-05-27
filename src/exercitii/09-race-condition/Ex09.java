

// =============================================================
// EXERCITIUL 09 - Race condition (fara protectie)
// =============================================================
// Creezi clasa Contor cu un int count = 0 si o metoda
// incrementeaza() care face count++.
// Pornesti 2 threaduri, fiecare face incrementeaza() de 100.000 ori.
// Astepti cu join() si printezi count.
//
// Asteptat: 200.000. Vei vedea < 200.000.
// De ce? count++ NU e atomic (citeste -> +1 -> scrie).
// =============================================================
public class Ex09 {
    public static void main(String[] args) throws InterruptedException {

        Contor contor = new Contor();

        Thread t1 = new Thread(()-> {//----------Ambele threaduri modififca acelas count
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

        public void incrementeaza(){
            count++;
        }
       public int getCount(){
            return count;
       }
    }

}
