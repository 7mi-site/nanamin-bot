package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.money.Money;
import xyz.n7mn.dev.Command.money.MoneySystem;

import java.security.SecureRandom;

public class GameNoMoney extends xyz.n7mn.dev.i.Game {

    public GameNoMoney(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {
        Money money = MoneySystem.getData(getMessage().getMember().getId());

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
        sb.append(MoneySystem.getCurrency());
        sb.append("獲得しました。");

        MoneySystem.updateData(new Money(money.getUserID(), nowMoney));
        getMessage().reply(sb.toString()).queue();

    }
}
