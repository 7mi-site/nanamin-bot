package xyz.n7mn.dev.Command.money;

public class Money {
    private final String UserID;
    private final long Money;

    public Money(String userID, long money){
        this.UserID = userID;
        this.Money = money;
    }

    public String getUserID() {
        return UserID;
    }

    public long getMoney() {
        return Money;
    }
}
