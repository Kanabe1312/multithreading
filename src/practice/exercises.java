package practice;

public class exercises {

    public static void main(String[] args) throws InterruptedException{
        Thread a = new Thread(() -> System.out.println("Salut din tread 1!"));
        Thread b = new Thread(()-> System.out.println("Salut din thread 2!"));
        Thread c = new Thread(() -> System.out.println("Salut din thread 3!"));
        a.start();
        a.join();
        b.start();
        c.start();

        System.out.println("test");
    }
}
