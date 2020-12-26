package xyz.n7mn.dev.game;


import net.dv8tion.jda.api.entities.Message;

import java.security.SecureRandom;

public class YosoGame extends GameInterface {

    public YosoGame(Message message, MoneySystem moneySystem) {
        super(message, moneySystem);
    }

    @Override
    public void run() {
        String text = getMessage().getContentRaw();
        String[] textSplit = text.split(" ", -1);

        if (textSplit.length == 1 && !text.equals("n.yosogame")){
            return;
        }

        if (textSplit.length == 3){

            Money money = getMoneySystem().getMoney(getMessage().getMember().getId());

            try {
                if (money.getMoney() <= Integer.parseInt(textSplit[1])){
                    getMessage().reply("えらーですっ\n所持金が足りませんっ！！").queue();
                    return;
                }

                if (Integer.parseInt(textSplit[1]) <= 0){
                    getMessage().reply("えらーですっ\n掛け金は1以上にしてくださいっ！！").queue();
                    return;
                }

                if (money.getMoney() <= 0){
                    getMessage().reply("えらーですっ\n所持金が足りませんっ！！").queue();
                    return;
                }

            } catch (Exception e){
                getMessage().reply("えらーですっ\n`n.yosogame <掛け金> <予想数字>`でお願いしますっ！").queue();
                return;
            }

            int nowMoney = money.getMoney();

            int i = new SecureRandom().nextInt(9);
            try {
                int targetInt = Integer.parseInt(textSplit[2]);

                if (targetInt >= 10){
                    getMessage().reply("えらーですっ\n予想数字は0から9の間でお願いしますっ！").queue();
                    return;
                }

                if (i == targetInt){
                    long tempInt = nowMoney + (Integer.parseInt(textSplit[1]) * 10L);

                    if (tempInt > Integer.MAX_VALUE){
                        getMoneySystem().setMoney(money.getDiscordUserID(), Integer.MAX_VALUE);
                    } else {
                        getMoneySystem().setMoney(money.getDiscordUserID(), (int) tempInt);
                    }

                    getMessage().reply("あたりっ！！\n予想数字：" + textSplit[2]+"\n"+"結果：" + i + "\n("+(Integer.parseInt(textSplit[1]) * 10)+getMoneySystem().getCurrency()+"獲得しました。)").queue();
                    return;
                } else {
                    getMoneySystem().setMoney(money.getDiscordUserID(), nowMoney - Integer.parseInt(textSplit[1]));
                    getMessage().reply("はずれです...\n予想数字：" + textSplit[2]+"\n"+"結果：" + i + "").queue();
                    return;
                }

            } catch (Exception e) {
                // e.printStackTrace();
                getMessage().reply("えらーですっ\n`n.yosogame <掛け金> <予想数字>`でお願いしますっ！").queue();
                return;
            }
        }

        getMessage().reply("えらーですっ\n`n.yosogame <掛け金> <予想数字>`でお願いしますっ！").queue();
    }

}
