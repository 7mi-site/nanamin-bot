package xyz.n7mn.dev.Command.money;

import java.util.Comparator;

public class MoneyComparator implements Comparator<Money> {

    @Override
    public int compare(Money o1, Money o2) {
        return Long.compare(o2.getMoney(), o1.getMoney());
    }

}
