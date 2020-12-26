package xyz.n7mn.dev.game;

import net.dv8tion.jda.api.entities.Message;

import java.security.SecureRandom;

public class KyusaiRoulette extends GameInterface {

    public KyusaiRoulette(Message message, MoneySystem moneySystem) {
        super(message, moneySystem);
    }

    @Override
    public void run() {

        Money money = getMoneySystem().getMoney(getMessage().getMember().getId());

        if (money.getMoney() >= -1000){
            getMessage().reply("がんばれっ！がんばれっ！").queue();
            return;
        }


        StringBuffer sb = new StringBuffer();
        int nowMoney = money.getMoney();

        sb.append("借金地獄のあなたへ贈り物だよっ！\n");
        int mo = 10000;
        int i = new SecureRandom().nextInt(4) + 1;

        nowMoney = nowMoney + (mo * i);
        sb.append("(");
        sb.append(mo * i);
        sb.append(getMoneySystem().getCurrency());
        sb.append("獲得しました。");

        getMoneySystem().setMoney(money.getDiscordUserID(), nowMoney);

        getMessage().reply(sb.toString()).queue();

    }
}
