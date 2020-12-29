package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.money.Money;
import xyz.n7mn.dev.Command.money.MoneySystem;
import xyz.n7mn.dev.i.Game;

import java.util.List;

public class GameRank extends Game {
    public GameRank(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {
        StringBuffer sb = new StringBuffer();

        List<Money> moneyList = MoneySystem.getMoneyList(getGuild());

        String sendUserID = getMessage().getMember().getId();

        int i = 1;
        int sendRank = 0;

        sb.append("---- ");
        sb.append(MoneySystem.getCurrency());
        sb.append(" 所持数ランキング ----\n");

        for (Money money : moneyList){

            Member member = getMessage().getGuild().getMemberById(money.getUserID());
            if (member != null && !member.getUser().isBot()){
                if (i <= 10){
                    sb.append(i);
                    sb.append("位 ");
                    if (member.getNickname() != null){
                        sb.append(member.getNickname());
                    } else {
                        sb.append(member.getUser().getName());
                    }
                    sb.append(" : ");
                    sb.append(money.getMoney());
                    sb.append(MoneySystem.getCurrency());
                    sb.append("\n");
                }

                if (member.getId().equals(sendUserID)){
                    sendRank = i;
                }

                i++;
            }

        }

        sb.append("(あなたの順位は");
        sb.append(sendRank);
        sb.append("位です)");

        getMessage().reply(sb.toString()).queue();
    }
}
