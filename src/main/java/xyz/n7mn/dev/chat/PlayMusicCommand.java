package xyz.n7mn.dev.chat;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.music.PlayerManager;

public class PlayMusicCommand extends CommandClassInterface {
    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public PlayMusicCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
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
                MessageEmbed build = builder.setTitle(title, uri).build();

                getMessage().reply("いま再生されている曲はこちらっ！").embed(build).queue();
                return;
            }
        }

        getMessage().reply("えらーですっ\n今は再生していません！！").queue();
    }
}
