package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class Game extends xyz.n7mn.dev.i.Game {

    public Game(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(Help.getHelpData(8).getHelpTitle());
        builder.setDescription(Help.getHelpData(8).getHelpMessage());

        getMessage().reply(builder.build()).queue();
    }
}
