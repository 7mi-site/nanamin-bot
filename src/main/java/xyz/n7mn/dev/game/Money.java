package xyz.n7mn.dev.game;

import java.util.UUID;

public class Money {

    private UUID UserID;
    private String DiscordUserID;
    private int Money;

    public Money(UUID userID, String discordUserID, int money){
        this.UserID = userID;
        this.DiscordUserID = discordUserID;
        this.Money = money;
    }

    public Money(String discordUserID, int money){

        this.UserID = UUID.randomUUID();
        this.DiscordUserID = discordUserID;
        this.Money = money;

    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getDiscordUserID() {
        return DiscordUserID;
    }

    public void setDiscordUserID(String discordUserID) {
        DiscordUserID = discordUserID;
    }

    public int getMoney() {
        return Money;
    }

    public void setMoney(int money) {
        Money = money;
    }
}
