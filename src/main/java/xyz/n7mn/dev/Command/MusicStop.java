package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.Command.music.GuildMusicManager;
import xyz.n7mn.dev.Command.music.PlayerManager;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

public class MusicStop extends Chat {
    public MusicStop(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return new Help(getTextChannel(), getMessage()).getHelpMessage(1011);
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
