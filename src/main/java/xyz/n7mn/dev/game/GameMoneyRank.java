package xyz.n7mn.dev.game;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class GameMoneyRank extends GameInterface {
    public GameMoneyRank(Message message, MoneySystem moneySystem) {
        super(message, moneySystem);
    }

    @Override
    public void run() {
        StringBuffer sb = new StringBuffer();

        List<Money> moneyList = getMoneySystem().getMoneyList();

        String sendUserID = getMessage().getMember().getId();

        int i = 1;
        int sendRank = 0;

        sb.append("---- ");
        sb.append(getMoneySystem().getCurrency());
        sb.append(" 所持数ランキング ----\n");

        for (Money money : moneyList){

            Member member = getMessage().getGuild().getMemberById(money.getDiscordUserID());
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
                    sb.append(getMoneySystem().getCurrency());
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
