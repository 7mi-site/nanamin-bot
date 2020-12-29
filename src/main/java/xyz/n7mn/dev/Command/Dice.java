package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.security.SecureRandom;

public class Dice extends Chat {
    public Dice(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return new Help(getTextChannel(), getMessage()).getHelpMessage(1006);
    }

    @Override
    public void run() {
        String[] split = getMessageText().split(" ");
        SecureRandom secureRandom = new SecureRandom();

        if (split.length == 1){

            int i = (secureRandom.nextInt(Integer.MAX_VALUE) % 6) + 1;
            getTextChannel().sendMessage("さいころの結果は" + i + "です。").queue();
            return;

        }

        if (split.length == 2){

            long i = (secureRandom.nextLong() % Long.parseLong(split[1])) + 1;
            getTextChannel().sendMessage("さいころの結果は" + i + "です。").queue();

        }

    }
}
