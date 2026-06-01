// =============================================================
// EXERCITIUL 11 - synchronized block cu lock-uri separate
// =============================================================
// Creezi clasa ContorDublu cu doua contoare independente a si b.
// Vrei ca incrementarea pe a sa NU blocheze incrementarea pe b.
// Solutia: doua obiecte de lock separate (lockA, lockB) si
// synchronized (lockX) { ... } pentru fiecare.
//
// Intrebare: de ce nu folosesti synchronized pe metoda aici?
// (Raspuns: lock pe `this` = un singur lock => A si B s-ar bloca reciproc.)
// =============================================================
public class Ex11 {
    public static void main(String[] args) throws InterruptedException {

        ContorDublu contor = new ContorDublu();

        Thread t1 = new Thread(()->{
            for (int i=0;i<100_000;i++) {
                contor.incrementeazaA();
            }
        });

        Thread t2 = new Thread(()->{
            for (int i=0;i<100_000;i++) {
                contor.incrementeazaB();
            }
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();


        System.out.println("A = "+contor.getA());
        System.out.println("B = "+contor.getB());


    }
   static class ContorDublu{
        private int a =  0;
        private int b = 0;

        private final Object lockA = new Object();
        private final Object lockB = new Object();

        public void incrementeazaA() {
            synchronized (lockA) {
                a++;
            }
        }

        public void incrementeazaB() {
            synchronized (lockB) {
                b++;
            }
        }

        public int getA() {
            synchronized (lockA) {
                return a;
            }
        }
        public int getB() {
            synchronized (lockB) {
                return b;
            }
        }





    }


}
