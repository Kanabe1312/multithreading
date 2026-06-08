import java.util.List;

public class MonitorTask implements Runnable {
    private List<Account> accounts;
    private Statistics statistics;

    public MonitorTask(List<Account> accounts,Statistics statistics) {

        this.accounts = accounts;
        this.statistics = statistics;
    }

    @Override
    public void run() {

        while (true) {

            System.out.println();
            System.out.println("monitor");

            System.out.println("Processed: " + statistics.getProcessed());

            for (Account account : accounts) {

                System.out.println(account.getId() + ": " + account.getBalance());
            }

            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                return;
            }
        }
    }
}
