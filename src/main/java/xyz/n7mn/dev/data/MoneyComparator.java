package xyz.n7mn.dev.data;

import xyz.n7mn.dev.game.Money;

import java.util.Comparator;

public class MoneyComparator implements Comparator<Money> {

    @Override
    public int compare(Money o1, Money o2) {
        return Integer.compare(o2.getMoney(), o1.getMoney());
    }

}
