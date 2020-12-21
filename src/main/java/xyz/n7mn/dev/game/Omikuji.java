package xyz.n7mn.dev.game;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Omikuji {

    public String run(MoneyList moneyList, Money money){

        List<Integer> a = new ArrayList<>();
        a.add(0);
        a.add(5);
        a.add(10);
        a.add(15);
        a.add(18);
        a.add(20);
        a.add(25);
        a.add(30);
        a.add(35);
        a.add(-10);
        a.add(50);

        Collections.shuffle(a);

        SecureRandom secureRandom = new SecureRandom();
        int slot1 = secureRandom.nextInt(a.size() - 1);


        moneyList.setMoney(money.getDiscordUserID(), (money.getMoney() + a.get(slot1)));

        if (a.get(slot1).equals(0)){
            return "ざんねーん！\nあなたの運勢は 凶 でした！\n(獲得コインは0です。)";
        }
        if (a.get(slot1).equals(5)){
            return "すごい微妙？\nあなたの運勢は 吉 でした！\n(獲得コインは5です。)";
        }
        if (a.get(slot1).equals(10)){
            return "ちょっと微妙？\nあなたの運勢は 小吉 でした！\n(獲得コインは10です。)";
        }
        if (a.get(slot1).equals(15)){
            return "ふつうだね！\nあなたの運勢は 中吉 でした！\n(獲得コインは15です。)";
        }
        if (a.get(slot1).equals(18)){
            return "朝食にパンを食べる人は1000年後に死ぬ確率が100%らしい。\nあなたの運勢は ぱんますたー でした！\n(獲得コインは18です。)";
        }
        if (a.get(slot1).equals(20)){
            return "やったね！\nあなたの運勢は 大吉 でした！\n(獲得コインは20です。)";
        }
        if (a.get(slot1).equals(25)){
            return "？？？\nあなたの運勢は 砂 でした！\n(獲得コインは25です。)";
        }
        if (a.get(slot1).equals(30)){
            return "はてな\nあなたの運勢は 金装備 でした！\n(獲得コインは30です。)";
        }
        if (a.get(slot1).equals(35)){
            return "はてな？\nあなたの運勢は 虫特攻 でした！\n(獲得コインは35です。)";
        }
        if (a.get(slot1).equals(-10)){
            return "変態は死刑ですっ\nあなたの運勢は 変態 でした！\n(獲得コインは-10です。)";
        }

        return "遅刻はダメですよ...\nあなたの運勢は 遅刻 でした！\n(獲得コインは50です。)";
    }


}
