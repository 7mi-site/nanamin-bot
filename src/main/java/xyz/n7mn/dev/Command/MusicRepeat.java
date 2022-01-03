package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.Command.music.GuildMusicManager;
import xyz.n7mn.dev.Command.music.PlayerManager;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

public class MusicRepeat extends Chat {
    public MusicRepeat(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return new Help(getTextChannel(), getMessage()).getHelpMessage(1012);
    }

    @Override
    public void run() {
        // System.out.println("あ");
        AudioManager audioManager = getGuild().getAudioManager();

        if (audioManager.isConnected()){

            GuildMusicManager guildMusicManager = PlayerManager.getINSTANCE().getGuildMusicManager(getGuild());

            boolean repeat = !guildMusicManager.scheduler.repeat;

            getMessage().reply(repeat ? "1曲ループモードになりましたっ！" : "1曲ループモードをやめましたっ！").queue();

            guildMusicManager.scheduler.repeat = repeat;
        } else {
            getMessage().reply("現在再生していません！").queue();
        }
    }
}
