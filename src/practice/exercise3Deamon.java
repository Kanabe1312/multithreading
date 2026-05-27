package practice;

public class exercise3Deamon {
    public static void main ( String[]args) throws InterruptedException{
        Thread background = new Thread(() -> {
           while(true){
               System.out.println("[BACKGROUND] tick");
               try{
                   Thread.sleep(300);
               }catch (InterruptedException e){
                   System.out.println("[BACKGRUOND] oprit");
                   return;
               }
           }
        });

        Animal t= ()->{
            System.out.println("test");
        };

        t.test();
        background.setDaemon(true);
        background.start();


        Thread principal = new Thread(()-> {
            System.out.println("[PRINCIPAL] tack");
            try{
                Thread.sleep(300);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("[PRINCIPAL] oprit");
        });

        principal.start();
        principal.join();

    }
}
