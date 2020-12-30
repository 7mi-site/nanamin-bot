package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Akeome extends Chat {

    public Akeome(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {

        Matcher matcher = Pattern.compile("(.*)あけおめ(.*)").matcher(getMessageText());
        Matcher matcher2 = Pattern.compile("(.*)誕生日(.*)").matcher(getMessageText());

        if (matcher.find()){
            getMessage().addReaction("\uD83C\uDF8D").queue();
        }

        if (matcher2.find()){
            getMessage().addReaction("\uD83E\uDE85").queue();
            getMessage().addReaction("\uD83C\uDF70").queue();
        }

    }
}
