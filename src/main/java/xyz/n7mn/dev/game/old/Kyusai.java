package xyz.n7mn.dev.game.old;

import xyz.n7mn.dev.game.Money;
import xyz.n7mn.dev.game.MoneySystem;

import java.security.SecureRandom;
@Deprecated
public class Kyusai {

    public String run(MoneySystem moneySystem, Money money){

        if (money.getMoney() >= -1000){
            return "がんばれっ！がんばれっ！";
        }


        StringBuffer sb = new StringBuffer();
        int nowMoney = money.getMoney();

        sb.append("借金地獄のあなたへ贈り物だよっ！\n");
        int mo = 10000;
        int i = new SecureRandom().nextInt(4) + 1;

        nowMoney = nowMoney + (mo * i);
        sb.append("(");
        sb.append(mo * i);
        sb.append(moneySystem.getCurrency());
        sb.append("獲得しました。");

        moneySystem.setMoney(money.getDiscordUserID(), nowMoney);

        return sb.toString();

    }

}
