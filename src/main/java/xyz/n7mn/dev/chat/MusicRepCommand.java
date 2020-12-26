package xyz.n7mn.dev.chat;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.music.GuildMusicManager;
import xyz.n7mn.dev.music.PlayerManager;

import java.util.List;

public class MusicRepCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public MusicRepCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        AudioManager audioManager = getGuild().getAudioManager();

        if (audioManager.isConnected()){

            PlayerManager Playermanager = PlayerManager.getINSTANCE();
            Playermanager.Repeat(getTextChannel());

        }

    }
}
