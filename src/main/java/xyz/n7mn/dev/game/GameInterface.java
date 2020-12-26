package xyz.n7mn.dev.game;

import net.dv8tion.jda.api.entities.Message;

public abstract class GameInterface implements Game {

    private final MoneySystem moneySystem;
    private final Message message;

    public GameInterface(Message message, MoneySystem moneySystem){

        this.moneySystem = moneySystem;
        this.message = message;

    }

    public abstract void run();

    public MoneySystem getMoneySystem() {
        return moneySystem;
    }

    public Message getMessage() {
        return message;
    }
}
