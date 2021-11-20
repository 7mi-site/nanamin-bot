package xyz.n7mn.dev.Command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.Command.music.PlayerManager;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.awt.*;

public class MusicNowPlay extends Chat {
    public MusicNowPlay(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return new Help(getTextChannel(), getMessage()).getHelpMessage(1013);
    }

    @Override
    public void run() {
        AudioManager audioManager = getGuild().getAudioManager();
        if (audioManager.isConnected()){
            PlayerManager Playermanager = PlayerManager.getINSTANCE();
            AudioTrack track = Playermanager.getGuildMusicManager(getGuild()).player.getPlayingTrack();
            if (track.getInfo().title != null){
                String title = track.getInfo().title;
                String uri = track.getInfo().uri;

                EmbedBuilder builder = new EmbedBuilder();

                if (uri.toLowerCase().startsWith("https://youtu") || uri.toLowerCase().startsWith("https://www.youtube")){
                    builder.setColor(Color.RED);
                }

                if (uri.toLowerCase().startsWith("https://nico") || uri.toLowerCase().startsWith("https://www.nicovideo")){
                    builder.setColor(Color.WHITE);
                }

                if (uri.toLowerCase().startsWith("https://www.twitch") || uri.toLowerCase().startsWith("https://twitch")){
                    builder.setColor(Color.MAGENTA);
                }

                MessageEmbed build = builder.setTitle(title, uri).setDescription(uri).build();
                getMessage().reply("いま再生されている曲はこちらっ！").setEmbeds(build).queue();
                return;
            }
        }

        getMessage().reply("えらーですっ\n今は再生していません！！").queue();
    }
}
