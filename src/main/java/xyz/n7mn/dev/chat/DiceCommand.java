package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.security.SecureRandom;

public class DiceCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public DiceCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
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

            int i = (secureRandom.nextInt(Integer.MAX_VALUE) % Integer.parseInt(split[1])) + 1;
            getTextChannel().sendMessage("さいころの結果は" + i + "です。").queue();

        }

    }
}
