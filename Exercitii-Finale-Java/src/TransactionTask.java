import java.util.concurrent.Callable;

public class TransactionTask implements Callable <Boolean>{
    private Transaction transaction;
    private Statistics statistics;
    public TransactionTask(Transaction transaction, Statistics statistics) {
        this.transaction = transaction;
        this.statistics = statistics;
    }


    @Override
    public Boolean call() throws Exception {

        Thread.sleep(150);

        boolean result = false;

        switch (transaction.getType()) {

            case DEPOSIT ->
            {
                transaction.getTo().deposit(transaction.getAmount());
                result = true;
            }

            case WITHDRAW ->
                    result = transaction.getFrom()
                            .withdraw(transaction.getAmount());

            case TRANSFER ->
                    result = transaction.getFrom()
                            .transfer(
                                    transaction.getTo(),
                                    transaction.getAmount()
                            );
        }

        statistics.incrementProcessed();

        return result;
    }
}