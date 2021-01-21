package banking;

public class Card {
    private int id;
    private String number;
    private String pin;
    private int balance;

    public Card() {
        this.id = 0;
        this.number = "Unknown";
        this.pin = "0000";
        this.balance = 0;
    }

    public Card(int id, String number, String pin, int balance) {
        this.id = id;
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
