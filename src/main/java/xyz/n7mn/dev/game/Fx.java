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

        long mo = ((long) useMoney) * b;
        long mo2 = ((long) useMoney) * b;
        sb.append(mo);
        sb.append(" ");
        sb.append(moneySystem.getCurrency());
        sb.append("を元値にFX開始っ！！\n");


        for (int i = 0; i < 5; i++){

            sb.append((i + 1));
            sb.append("回目：");

            long rr = mo2 * 2;
            long n = Math.abs(new SecureRandom().nextLong() % rr);
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

        boolean f = false;
        if (((long) nowMoney) + mo >= Integer.MAX_VALUE){
            sb.append("\n(獲得金額が保有上限を超えたため、一部は他の方への借金地獄対策のための資金とさせていただきましたっ)");
            f = true;
        }

        if (f){
            nowMoney = Integer.MAX_VALUE;
        } else {
            if ((long) nowMoney + mo <= Integer.MIN_VALUE){
                nowMoney = Integer.MIN_VALUE;
            } else {
                nowMoney = nowMoney + ((int) mo);
            }
        }

        moneySystem.setMoney(money.getDiscordUserID(), nowMoney);

        return sb.toString();

    }

}
