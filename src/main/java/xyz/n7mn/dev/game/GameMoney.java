package xyz.n7mn.dev.game;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class GameMoney extends GameInterface {
    public GameMoney(Message message, MoneySystem moneySystem) {
        super(message, moneySystem);
    }

    @Override
    public void run() {
        String t = getMessage().getContentRaw().replaceAll("　"," ");
        String[] split = t.split(" ", -1);

        if (split.length == 1){
            Money money = getMoneySystem().getMoney(getMessage().getMember().getId());
            getMessage().reply("あなたが今持っている所持金は " + money.getMoney() + " " + getMoneySystem().getCurrency() + "ですっ！\n他の人に渡したいときは`n.money pay <相手の名前 or 相手のID> <金額>`でできますっ！").queue();
        } else if (split.length == 4){
            if (split[1].equals("pay")){
                String memberStr = split[2];
                Member targetMember = null;
                try {
                    targetMember = getMessage().getGuild().getMemberById(memberStr);
                } catch (Exception e){
                    List<Member> members = getMessage().getGuild().getMembers();

                    for (Member member1 : members){
                        if (member1.getNickname() != null && member1.getNickname().startsWith(memberStr)){
                            targetMember = member1;
                            break;
                        }
                        if (member1.getUser().getName().startsWith(memberStr)){
                            targetMember = member1;
                            break;
                        }
                    }
                }

                if (getMoneySystem().getMoney(getMessage().getMember().getId()).getMoney() - Integer.parseInt(split[3]) <= 0){
                    getMessage().reply("所持金が足りませんよ！！").queue();
                    return;
                }

                if (Integer.parseInt(split[3]) <= 0){
                    getMessage().reply("よくない。").queue();
                    return;
                }

                if (targetMember != null){
                    Money fromMoney = getMoneySystem().getMoney(getMessage().getMember().getId());
                    Money targetMoney = getMoneySystem().getMoney(targetMember.getId());

                    getMoneySystem().setMoney(fromMoney.getDiscordUserID(), fromMoney.getMoney() - Integer.parseInt(split[3]));
                    getMoneySystem().setMoney(targetMoney.getDiscordUserID(), targetMoney.getMoney() + Integer.parseInt(split[3]));

                    if (targetMember.getNickname() != null){
                        getMessage().reply(targetMember.getNickname() + "さんに "+split[3] + " "+ getMoneySystem().getCurrency()+"を送金しましたっ").queue();
                    } else {
                        getMessage().reply(targetMember.getUser().getName() + "さんに "+split[3] + " "+ getMoneySystem().getCurrency()+"を送金しましたっ").queue();
                    }
                } else {
                    getMessage().reply("その人 実は存在しないらしい。").queue();
                }

            }

        }
    }
}
