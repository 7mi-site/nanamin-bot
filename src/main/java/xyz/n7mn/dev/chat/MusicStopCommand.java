package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.music.GuildMusicManager;
import xyz.n7mn.dev.music.PlayerManager;

public class MusicStopCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public MusicStopCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        AudioManager audioManager = getGuild().getAudioManager();

        PlayerManager Playermanager = PlayerManager.getINSTANCE();
        GuildMusicManager guildMusicManager = Playermanager.getGuildMusicManager(getGuild());
        guildMusicManager.player.stopTrack();
        audioManager.closeAudioConnection();

        getMessage().delete().queue();
        getTextChannel().sendMessage("再生を終了しましたっ！").queue();

    }
}
