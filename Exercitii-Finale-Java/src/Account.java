public class Account {

    private String id;
    private int balance;

    public Account(String id, int balance) {
        this.id = id;
        this.balance = balance;
    }
    public String getId() {
        return id;
    }
    public int getBalance() {
        return balance;
    }

    public synchronized void deposit(int amount) {
        this.balance += amount;
    }
    public synchronized boolean withdraw(int amount) {
        if(balance >= amount){
            this.balance -= amount;
            return true;
        }
        return false;
    }
    public boolean transfer(Account destination, int amount) {
        if(withdraw(amount)){
            destination.deposit(amount);
            return true;
        }
        return false;
    }



}
