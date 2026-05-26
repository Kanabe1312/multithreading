// Exemplul A.5 — Daemon thread. JVM nu il asteapta la shutdown.
// In acest demo il intrerupem manual ca sa nu blocheze rularea.
public class A5 {
    public static void main(String[] args) throws InterruptedException {
        Thread tick = new Thread(() -> {
            while (true) {
                System.out.println("[tick]");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });

        // IMPORTANT: setDaemon() trebuie apelat INAINTE de start().
        tick.setDaemon(true);
        tick.start();

        Thread.sleep(300);   // vezi ~3 tick-uri
        tick.interrupt();
        tick.join();
    }
}
