// =============================================================
// EXERCITIUL 03 - Daemon thread
// =============================================================
// Creeaza un thread care printeaza "tick" la fiecare 100ms intr-o
// bucla infinita. Marcheaza-l ca daemon cu setDaemon(true) INAINTE
// de start(). Apoi main doarme 500ms si termina.
//
// Intrebare: ce s-ar intampla daca nu ar fi daemon?
// =============================================================
public class Ex03 {
    public static void main(String[] args) throws InterruptedException {

        Thread tick = new Thread(()-> {
            while(true){
                System.out.println("[tick]");
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });

        tick.setDaemon(true);
        tick.start();

        Thread.sleep(500);
        System.out.println("Main finished!");

    }
}
