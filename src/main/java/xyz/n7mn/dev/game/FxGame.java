package xyz.n7mn.dev.game;


import net.dv8tion.jda.api.entities.Message;

import java.security.SecureRandom;


public class FxGame extends GameInterface {

    public FxGame(Message message, MoneySystem moneySystem) {
        super(message, moneySystem);
    }

    @Override
    public void run() {

        String text = getMessage().getContentRaw();
        String[] textSplit = text.split(" ", -1);

        Money money = getMoneySystem().getMoney(getMessage().getMember().getId());

        if (!text.equals("n.fx") && textSplit.length == 1){
            return;
        }

        if (textSplit.length != 2){
            getMessage().reply("えらーですっ！\n`n.fx <掛け金>`で実行してください！").queue();
            return;
        }

        int useMoney = Integer.parseInt(textSplit[1]);

        if (money.getMoney() <= useMoney){
            getMessage().reply("所持金が足りないですっ！").queue();
            return;
        }

        if (useMoney <= 0){
            getMessage().reply("それはおかしいですよ...").queue();
            return;
        }

        int nowMoney = money.getMoney();

        StringBuffer sb = new StringBuffer();
        int b = 1;
        if (useMoney < 100){
            sb.append("元値 2倍もーど！\n");
            b = 2;
        }

        if (useMoney >= 100 && useMoney < 1000){
            sb.append("元値 5倍もーど！\n");
            b = 5;
        }

        if (useMoney >= 1000){
            sb.append("元値 10倍もーど！\n");
            b = 10;
        }

        long mo = ((long) useMoney) * b;
        long mo2 = ((long) useMoney) * b;
        sb.append(mo);
        sb.append(" ");
        sb.append(getMoneySystem().getCurrency());
        sb.append("を元値にFX開始っ！！\n");


        for (int i = 0; i < 5; i++){

            sb.append((i + 1));
            sb.append("回目：");

            long rr = mo2 * 2;
            long n = Math.abs(new SecureRandom().nextLong() % rr);
            if (new SecureRandom().nextBoolean()){
                mo = mo + n;
                sb.append(n);
                sb.append(" ");
                sb.append(getMoneySystem().getCurrency());
                sb.append(" あがったよ！\n");
            } else {
                mo = mo - n;
                sb.append(n);
                sb.append(" ");
                sb.append(getMoneySystem().getCurrency());
                sb.append(" さがった...\n");
            }
        }

        sb.append("最終的に");
        sb.append(mo);
        sb.append(" ");
        sb.append(getMoneySystem().getCurrency());
        sb.append("になったよ！\n\n");

        mo = mo - mo2;
        sb.append("(");
        sb.append(mo);
        sb.append(getMoneySystem().getCurrency());
        sb.append("獲得しました！)");

        boolean f = false;
        if (((long) nowMoney) + mo >= Integer.MAX_VALUE){
            sb.append("\n(獲得金額が保有上限を超えたため、一部は他の方への借金地獄対策のための資金とさせていただきましたっ)");
            f = true;
        }

        if (f){
            nowMoney = Integer.MAX_VALUE;
        } else {
            if ((long) nowMoney + mo <= Integer.MIN_VALUE){
                nowMoney = Integer.MIN_VALUE;
            } else {
                nowMoney = nowMoney + ((int) mo);
            }
        }

        getMoneySystem().setMoney(money.getDiscordUserID(), nowMoney);
        getMessage().reply(sb.toString()).queue();
    }

}
