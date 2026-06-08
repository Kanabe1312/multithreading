package ATM_Banking;

public class Transaction {

    private int id;
    private TransactionType type;
    private Account from;
    private Account to;
    private int amount;

    public Transaction(int id, TransactionType type, Account from, Account to, int amount) {
        this.id = id;
        this.type = type;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }


    public Account getTo() {
        return to;
    }
    public int getId() {
        return id;
    }
    public TransactionType getType() {
        return type;
    }
    public Account getFrom() {
        return from;
    }
    public int getAmount() {
        return amount;
    }
}
