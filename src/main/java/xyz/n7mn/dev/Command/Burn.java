package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

public class Burn extends Chat {
    public Burn(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return new Help(getTextChannel(), getMessage()).getHelpMessage(1008);
    }

    @Override
    public void run() {
        getTextChannel().sendMessage("\uD83D\uDD25").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });
    }
}
