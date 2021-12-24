package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.money.Money;
import xyz.n7mn.dev.Command.money.MoneyComparator;
import xyz.n7mn.dev.Command.money.MoneySystem;
import xyz.n7mn.dev.i.Game;

import java.awt.*;
import java.util.List;

public class GameRank extends Game {
    public GameRank(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {
        StringBuffer sb = new StringBuffer();

        List<Money> moneyList = MoneySystem.getMoneyList(getGuild());
        System.out.println(moneyList.size());
        moneyList.sort(new MoneyComparator());

        String sendUserID = getMessage().getMember().getId();

        int i = 1;
        int sendRank = 0;
        int c = 0;
        int sendC = 0;

        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(MoneySystem.getCurrency() + " 所持数ランキング");
        builder.setDescription(getGuild().getName() + "でのランキングです！");
        builder.setColor(Color.ORANGE);
        System.out.println(moneyList.size());
        for (Money money : moneyList){

            Member member = getMessage().getGuild().getMemberById(money.getUserID());
            if (member != null && !member.getUser().isBot()){
                if (i <= 10){

                    String name = "";
                    if (member.getNickname() != null){
                        name = member.getNickname();
                    } else {
                        name = member.getUser().getName();
                    }

                    if (member.getId().equals(sendUserID)){
                        builder.addField(i+"位 ("+money.getMoney()+" "+MoneySystem.getCurrency()+")", "`"+name+"` (あなたですっ！)", false);
                    } else {
                        builder.addField(i+"位 ("+money.getMoney()+" "+MoneySystem.getCurrency()+")", "`"+name+"`", false);
                    }

                }

                if (member.getId().equals(sendUserID)){
                    sendRank = i;
                    sendC = c;
                }

                i++;
            }
            c++;
        }

        builder.addField("あなたの順位",sendRank + " 位 ("+moneyList.get(sendC).getMoney()+"コイン)", false);
        getMessage().reply(builder.build()).queue();
    }
}
