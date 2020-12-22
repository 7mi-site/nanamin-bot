package xyz.n7mn.dev.game;

import java.security.SecureRandom;

public class Fx {

    public String run(MoneySystem moneySystem, Money money, int useMoney){

        if (money.getMoney() <= useMoney){
            return "所持金が足りないですっ！";
        }

        if (useMoney <= 0){
            return "それはおかしいですよ...";
        }

        int nowMoney = money.getMoney();

        StringBuffer sb = new StringBuffer();
        int b = 1;
        if (useMoney < 100){
            sb.append("元値 2倍もーど！\n");
            b = 2;
        }

        if (useMoney >= 100 && useMoney < 1000){
            sb.append("元値 5倍もーど！\n");
            b = 5;
        }

        if (useMoney >= 1000){
            sb.append("元値 10倍もーど！\n");
            b = 10;
        }

        int mo = useMoney * b;
        int mo2 = useMoney * b;
        sb.append(mo);
        sb.append(" ");
        sb.append(moneySystem.getCurrency());
        sb.append("を元値にFX開始っ！！\n");


        for (int i = 0; i < 5; i++){

            sb.append((i + 1));
            sb.append("回目：");

            int rr = mo2 * 2;
            int n = new SecureRandom().nextInt(rr);
            if (new SecureRandom().nextBoolean()){
                mo = mo + n;
                sb.append(n);
                sb.append(" ");
                sb.append(moneySystem.getCurrency());
                sb.append(" あがったよ！\n");

            } else {
                mo = mo - n;
                sb.append(n);
                sb.append(" ");
                sb.append(moneySystem.getCurrency());
                sb.append(" さがった...\n");
            }
        }

        sb.append("最終的に");
        sb.append(mo);
        sb.append(" ");
        sb.append(moneySystem.getCurrency());
        sb.append("になったよ！\n\n");

        mo = mo - mo2;
        sb.append("(");
        sb.append(mo);
        sb.append(moneySystem.getCurrency());
        sb.append("獲得しました！)");

        nowMoney = nowMoney + mo;
        moneySystem.setMoney(money.getDiscordUserID(), nowMoney);

        return sb.toString();

    }

}
