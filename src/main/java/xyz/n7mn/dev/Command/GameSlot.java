package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.money.Money;
import xyz.n7mn.dev.Command.money.MoneySystem;
import xyz.n7mn.dev.i.Game;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameSlot extends Game {
    public GameSlot(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {
        Money money = MoneySystem.getData(getMessage().getMember().getId());
        int but = 100;

        if (money.getMoney() < 100){
            getMessage().reply("所持金が足りないですっ！出直してきてね！！").queue();
            return;
        }

        List<Integer> a = new ArrayList<>();
        a.add(0);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        a.add(5);
        a.add(6);
        a.add(7);
        a.add(8);
        a.add(9);

        List<Integer> b = new ArrayList<>(a);
        List<Integer> c = new ArrayList<>(a);

        Collections.shuffle(a);
        Collections.shuffle(b);
        Collections.shuffle(c);

        SecureRandom secureRandom = new SecureRandom();
        int slot1 = secureRandom.nextInt(a.size() - 1);
        int slot2 = secureRandom.nextInt(b.size() - 1);
        int slot3 = secureRandom.nextInt(c.size() - 1);

        int plus = -100;
        boolean flag = false;
        if (a.get(slot1).equals(b.get(slot2)) && b.get(slot2).equals(c.get(slot3))){
            plus = but * 5;
            if (a.get(slot1).equals(7) && b.get(slot2).equals(7) && c.get(slot3).equals(7)){
                plus = but = 10;
            }
            flag = true;
        }

        if (a.get(slot1).equals(7) && b.get(slot2).equals(7) && c.get(slot3).equals(3)){
            plus = but * 8;
            flag = true;
        }

        long tempInt = (money.getMoney() + (long) plus);
        if (tempInt > Integer.MAX_VALUE){
            tempInt = Integer.MAX_VALUE;
        }

        MoneySystem.updateData(new Money(money.getUserID(), (int) tempInt));

        if (flag){
            getMessage().reply("あたり！ "+ (plus / but) + "倍！\nスロット結果 ： `"+a.get(slot1)+" "+b.get(slot2)+" "+c.get(slot3)+"`").queue();
            return;
        }

        getMessage().reply("ざんねーん！\nスロット結果 ： `"+a.get(slot1)+" "+b.get(slot2)+" "+c.get(slot3)+"`").queue();
    }
}
