package xyz.n7mn.dev.game.old;

import xyz.n7mn.dev.game.Money;
import xyz.n7mn.dev.game.MoneySystem;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
@Deprecated
public class OmikujiGame {

    @Deprecated
    public String run(MoneySystem moneySystem, Money money){

        List<Omikuji> omikuji = new ArrayList<>();

        omikuji.add(new Omikuji("変態","変態は死刑ですっ！",-5));
        omikuji.add(new Omikuji("凶","ざんねーん！",0));
        omikuji.add(new Omikuji("吉","すごい微妙？",5));
        omikuji.add(new Omikuji("小吉","ちょっと微妙？",10));
        omikuji.add(new Omikuji("中吉","ふつうだね！",15));
        omikuji.add(new Omikuji("大吉","やったね！",20));
        omikuji.add(new Omikuji("遅刻","遅刻はダメですよ...",40));

        return run(moneySystem, omikuji, money);
    }


    public String run(MoneySystem moneySystem, List<Omikuji> omikujiDataList, Money money){
        SecureRandom secureRandom = new SecureRandom();
        int i = secureRandom.nextInt(omikujiDataList.size() - 1);

        moneySystem.setMoney(money.getDiscordUserID(), (money.getMoney() + omikujiDataList.get(i).getCoins()));

        return omikujiDataList.get(i).getResultComment() + "\nあなたの運勢は "+omikujiDataList.get(i).getResultText()+" でした！\n(獲得コインは"+omikujiDataList.get(i).getCoins()+"です。)";
    }
}
