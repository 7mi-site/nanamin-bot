package xyz.n7mn.dev.Command.money;

public class Money {
    private final String UserID;
    private final int Money;

    public Money(String userID, int money){
        this.UserID = userID;
        this.Money = money;
    }

    public String getUserID() {
        return UserID;
    }

    public int getMoney() {
        return Money;
    }
}
