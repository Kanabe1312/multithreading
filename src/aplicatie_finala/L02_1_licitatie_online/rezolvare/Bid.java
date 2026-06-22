package aplicatie_finala.L02_1_licitatie_online.rezolvare;

public class Bid {
    private int id;
    private String bidderName;
    private int amount;

    public Bid(int id, String bidderName, int amount) {
        this.id = id;
        this.bidderName = bidderName;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }
    public String getBidderName() {
        return bidderName;
    }
    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Bid #" + id + ": " + bidderName + "|: " + amount+" lei";
    }


}
