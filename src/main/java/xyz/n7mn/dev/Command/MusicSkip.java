package xyz.n7mn.dev.Command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.Command.music.PlayerManager;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

public class MusicSkip extends Chat {

    public MusicSkip(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {
        AudioManager audioManager = getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            PlayerManager Playermanager = PlayerManager.getINSTANCE();

            AudioTrack track = Playermanager.getGuildMusicManager(getGuild()).player.getPlayingTrack();

            String oldTitle = track.getInfo().title;
            String oldUrl = track.getInfo().uri;

            Playermanager.getGuildMusicManager(getGuild()).scheduler.nextTrack();


            AudioTrack track2 = Playermanager.getGuildMusicManager(getGuild()).player.getPlayingTrack();

            getMessage().delete().queue();

            getTextChannel().sendMessage("「"+oldTitle+"」 ( "+oldUrl+" )をスキップして「"+track2.getInfo().title+"」( "+track2.getInfo().uri+" )を再生しました！").queue();
        }
    }
}
