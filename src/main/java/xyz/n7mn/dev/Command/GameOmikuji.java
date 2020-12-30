package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.game.Omikuji;
import xyz.n7mn.dev.Command.game.OmikujiSystem;
import xyz.n7mn.dev.Command.money.Money;
import xyz.n7mn.dev.Command.money.MoneySystem;

import java.security.SecureRandom;
import java.util.List;

public class GameOmikuji extends xyz.n7mn.dev.i.Game {

    public GameOmikuji(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        List<Omikuji> omikujiData = OmikujiSystem.getOmikujiData(getGuild());
        Money money = MoneySystem.getData(getMember().getId());
        if (money == null){
            money = MoneySystem.getDefaultData(getMember().getId());
        }

        SecureRandom secureRandom = new SecureRandom();
        int i = secureRandom.nextInt(omikujiData.size() - 1);

        long tempInt = (long) money.getMoney() + (long) omikujiData.get(i).getCoins();
        if (tempInt > Integer.MAX_VALUE){
            tempInt = Integer.MAX_VALUE;
        }
        MoneySystem.updateData(new Money(money.getUserID(), (int) tempInt));
        getMessage().reply(omikujiData.get(i).getResultComment() + "\nあなたの運勢は "+omikujiData.get(i).getResultText()+" でした！\n(獲得コインは"+omikujiData.get(i).getCoins()+"です。)").queue();
    }
}
