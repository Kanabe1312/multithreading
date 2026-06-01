import java.util.List;

public class MonitorTask implements Runnable {
    private List<Account> accounts;

    public MonitorTask(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void run() {

    }
}
