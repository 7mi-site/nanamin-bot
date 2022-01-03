package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.money.Money;
import xyz.n7mn.dev.Command.money.MoneySystem;
import xyz.n7mn.dev.i.Game;

import java.security.SecureRandom;

public class GameNomoney2 extends Game {
    public GameNomoney2(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {
        String text = getMessage().getContentRaw();
        String[] textSplit = text.split(" ", -1);

        if (textSplit.length == 1 && !text.equalsIgnoreCase("n.nomoney2")){
            return;
        }


        Money data = MoneySystem.getData(getMember().getId());
        if (data == null){
            getMessage().reply("がんばれっ！がんばれっ！！").queue();
            return;
        }

        if (data.getMoney() >= -10000){
            getMessage().reply("がんばれっ！がんばれっ！").queue();
            return;
        }

        double d = new SecureRandom().nextInt(100) * 0.01;
        long i = (long) (Math.abs(data.getMoney()) * d);


        long m = data.getMoney() + i;


        MoneySystem.updateData(new Money(data.getUserID(), m));
        getMessage().reply("ふーん...\n(所持金が"+m+" "+MoneySystem.getCurrency()+"になりました。)").queue();

    }
}
