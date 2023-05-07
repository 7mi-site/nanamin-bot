package xyz.n7mn.dev.setting;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Setting {

    private JDA jda;
    private final EmbedBuilder builder = new EmbedBuilder();

    public Setting(JDA jda){
        this.jda = jda;
    }

    public void run(SlashCommandInteractionEvent event){
        builder.setTitle("ななみちゃんbot 設定");
        builder.setDescription("準備中...");
        event.replyEmbeds(builder.build()).setEphemeral(false).queue();
    }

}
