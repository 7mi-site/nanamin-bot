package xyz.n7mn.dev.Command;

import com.mysql.cj.log.Log;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.money.Money;
import xyz.n7mn.dev.Command.money.MoneySystem;
import xyz.n7mn.dev.i.Game;

import java.security.SecureRandom;

public class GameFx extends Game {
    public GameFx(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {
        String text = getMessage().getContentRaw();
        String[] textSplit = text.split(" ", -1);

        Money money = MoneySystem.getData(getMessage().getMember().getId());

        if (money == null){
            money = MoneySystem.getDefaultData(getMember().getId());
            MoneySystem.createData(money.getUserID());
        }

        if (!text.equals("n.fx") && textSplit.length == 1){
            return;
        }

        if (textSplit.length != 2){
            getMessage().reply("えらーですっ！\n`n.fx <掛け金>`で実行してください！").queue();
            return;
        }

        long useMoney = -1;
        try {
            useMoney = Long.parseLong(textSplit[1]);
        } catch (Exception e){
            // e.printStackTrace();
        }

        if (money.getMoney() <= useMoney){
            getMessage().reply("所持金が足りないですっ！").queue();
            return;
        }

        if (useMoney <= 0){
            getMessage().reply("それはおかしいですよ...").queue();
            return;
        }

        long nowMoney = money.getMoney();

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

        if (useMoney >= 1000 && useMoney < 10000){
            sb.append("元値 10倍もーど！\n");
            b = 10;
        }

        if (useMoney >= 10000 && useMoney < 100000){
            sb.append("元値 20倍もーど！\n");
            b = 20;
        }

        if (useMoney >= 100000 && useMoney < 1000000){
            sb.append("元値 40倍もーど！\n");
            b = 40;
        }

        if (useMoney >= 1000000 && useMoney < 10000000){
            sb.append("元値 80倍もーど！\n");
            b = 80;
        }

        if (useMoney >= 10000000 && useMoney < 100000000){
            sb.append("元値 160倍もーど！\n");
            b = 160;
        }

        if (useMoney >= 100000000){
            sb.append("元値 320倍もーど！\n");
            b = 320;
        }
        long mo = useMoney * b;
        long mo2 = useMoney * b;
        sb.append(mo);
        sb.append(" ");
        sb.append(MoneySystem.getCurrency());
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
                sb.append(MoneySystem.getCurrency());
                sb.append(" あがったよ！\n");
            } else {
                mo = mo - n;
                sb.append(n);
                sb.append(" ");
                sb.append(MoneySystem.getCurrency());
                sb.append(" さがった...\n");
            }
        }

        sb.append("最終的に");
        sb.append(mo);
        sb.append(" ");
        sb.append(MoneySystem.getCurrency());
        sb.append("になったよ！\n\n");

        mo = mo - mo2;
        sb.append("(");
        sb.append(mo);
        sb.append(MoneySystem.getCurrency());
        sb.append("獲得しました！)");

        if (nowMoney == Long.MAX_VALUE){
            sb.append("\n(所持金が保有上限を超えたため、一部は他の方への借金地獄対策のための資金とさせていただきましたっ)");
        }

        MoneySystem.updateData(new Money(money.getUserID(), nowMoney));
        getMessage().reply(sb.toString()).queue();
    }
}
