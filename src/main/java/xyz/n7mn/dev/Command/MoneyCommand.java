package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.money.Money;
import xyz.n7mn.dev.Command.money.MoneySystem;
import xyz.n7mn.dev.Command.money.MoneyUtil;
import xyz.n7mn.dev.i.Game;

import java.util.List;

public class MoneyCommand extends Game {

    public MoneyCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {
        String t = getMessage().getContentRaw().replaceAll("　"," ");
        String[] split = t.split(" ", -1);

        if (split.length == 1){

            Money money = MoneySystem.getData(getMember().getId());
            if (money == null){
                money = MoneySystem.getDefaultData(getMember().getId());
                MoneySystem.createData(getMember().getId());
            }
            getMessage().reply("" +
                    "あなたが今持っている所持金は " + money.getMoney() + " " + MoneySystem.getCurrency() + "ですっ！\n" +
                    "他の人に渡したいときは`n.money pay <相手の名前 or 相手のID> <金額>`でできますっ！"
            ).queue();

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

                Money fromMoney = MoneySystem.getData(getMember().getId());
                if (fromMoney == null){
                    fromMoney = MoneySystem.getDefaultData(getMember().getId());
                    MoneySystem.createData(getMember().getId());
                }
                if (fromMoney.getMoney() - Long.parseLong(split[3]) <= 0){
                    getMessage().reply("所持金が足りませんよ！！").queue();
                    return;
                }

                if (Long.parseLong(split[3]) <= 0){
                    getMessage().reply("よくない。").queue();
                    return;
                }

                if (targetMember != null){
                    Money targetMoney = MoneySystem.getData(targetMember.getId());
                    if (targetMoney == null){
                        targetMoney = MoneySystem.getDefaultData(targetMember.getId());
                        MoneySystem.createData(targetMember.getId());
                    }

                    long temp = MoneyUtil.add(targetMoney.getMoney(), Long.parseLong(split[3]), true);
                    if (temp == Long.MAX_VALUE){
                        getMessage().reply("相手は上限に達していますっ！！").queue();
                        return;
                    }

                    Money money1 = new Money(fromMoney.getUserID(), fromMoney.getMoney() - Long.parseLong(split[3]));
                    Money money2 = new Money(targetMoney.getUserID(), targetMoney.getMoney() + Long.parseLong(split[3]));
                    MoneySystem.updateData(money1);
                    MoneySystem.updateData(money2);

                    if (targetMember.getNickname() != null){
                        getMessage().reply(targetMember.getNickname() + "さんに "+split[3] + " "+ MoneySystem.getCurrency()+"を送金しましたっ").queue();
                    } else {
                        getMessage().reply(targetMember.getUser().getName() + "さんに "+split[3] + " "+ MoneySystem.getCurrency()+"を送金しましたっ").queue();
                    }
                } else {
                    getMessage().reply("その人 実は存在しないらしい。").queue();
                }

            }

        }
    }
}
