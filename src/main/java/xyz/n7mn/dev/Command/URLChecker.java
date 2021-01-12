package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.Command.vote.VoteSystem;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.awt.*;

public class URLChecker extends Chat {
    public URLChecker(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {
        String[] url = getMessageText().split("/", -1);

        JDA jda = getGuild().getJDA();

        Guild guild = getGuild().getJDA().getGuildById(url[4]);
        if (guild == null){
            return;
        }

        TextChannel textChannel = guild.getTextChannelById(url[5]);
        if (textChannel == null) {
            return;
        }

        boolean vote = false;
        try {
            vote = VoteSystem.isVote(textChannel.retrieveMessageById(url[6]).complete());
        } catch (Exception e){
            // e.printStackTrace();
        }

        if (vote){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("投票受付中っ！",textChannel.retrieveMessageById(url[6]).complete().getJumpUrl());
            builder.setDescription(textChannel.retrieveMessageById(url[6]).complete().getJumpUrl());
            builder.setColor(Color.ORANGE);
            getMessage().addReaction("\uD83D\uDCBE").queue();

            getTextChannel().sendMessage(builder.build()).queue();
        }

    }
}
