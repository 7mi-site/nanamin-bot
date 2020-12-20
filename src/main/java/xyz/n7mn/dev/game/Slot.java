package xyz.n7mn.dev.game;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Slot {

    int money = 100; // 1回

    public String run(MoneyList moneyList, Money money){

        if (money.getMoney() < 100){
            return "所持金が足りないですっ！出直してきてね！！";
        }

        List<Integer> a = new ArrayList<>();
        a.add(0);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        a.add(5);
        a.add(6);
        a.add(7);
        a.add(8);
        a.add(9);

        List<Integer> b = new ArrayList<>(a);
        List<Integer> c = new ArrayList<>(a);

        Collections.shuffle(a);
        Collections.shuffle(b);
        Collections.shuffle(c);

        SecureRandom secureRandom = new SecureRandom();
        int slot1 = secureRandom.nextInt(a.size() - 1);
        int slot2 = secureRandom.nextInt(b.size() - 1);
        int slot3 = secureRandom.nextInt(c.size() - 1);

        int plus = -100;
        boolean flag = false;
        if (a.get(slot1).equals(b.get(slot2)) && b.get(slot2).equals(c.get(slot3))){
            plus = this.money * 5;
            if (a.get(slot1).equals(7) && b.get(slot2).equals(7) && c.get(slot3).equals(7)){
                plus = this.money = 10;
            }
            flag = true;
        }

        if (a.get(slot1).equals(7) && b.get(slot2).equals(7) && c.get(slot3).equals(3)){
            plus = this.money * 8;
            flag = true;
        }

        moneyList.setMoney(money.getDiscordUserID(), (money.getMoney() + plus));

        if (flag){
            return "あたり！ "+ (plus / this.money) + "倍！\nスロット結果 ： `"+a.get(slot1)+" "+b.get(slot2)+" "+c.get(slot3)+"`";
        }

        return "ざんねーん！\nスロット結果 ： `"+a.get(slot1)+" "+b.get(slot2)+" "+c.get(slot3)+"`";
    }


}
