package xyz.n7mn.dev.game;

public abstract class GameInterface implements Game {

    private final MoneySystem moneySystem;
    private final Money money;

    public GameInterface(MoneySystem moneySystem, Money money){

        this.moneySystem = moneySystem;
        this.money = money;

    }

    public abstract String run();

    public MoneySystem getMoneySystem() {
        return moneySystem;
    }

    public Money getMoney() {
        return money;
    }
}
