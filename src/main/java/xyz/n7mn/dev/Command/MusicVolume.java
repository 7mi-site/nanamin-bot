package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.Command.music.GuildMusicManager;
import xyz.n7mn.dev.Command.music.PlayerManager;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

public class MusicVolume extends Chat {
    public MusicVolume(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return new Help(getTextChannel(), getMessage()).getHelpMessage(1014);
    }

    @Override
    public void run() {
        String[] split = getMessageText().split(" ", -1);
        if (!getMessageText().toLowerCase().equals("n.musicvolume") && !getMessageText().toLowerCase().equals("n.volume") && split.length == 1){
            getMessage().reply("えらーですっ\n`n.musicVolume <0～100>`または`n.volume <0～100>`でお願いしますっ！！").queue();
            return;
        }

        if (split.length != 2){
            getMessage().reply("えらーですっ\n`n.musicVolume <0～100>`または`n.volume <0～100>`でお願いしますっ！！").queue();
            return;
        }

        AudioManager audioManager = getGuild().getAudioManager();
        if (audioManager.isConnected()){
            PlayerManager Playermanager = PlayerManager.getINSTANCE();
            Playermanager.getGuildMusicManager(getGuild()).player.setVolume(Integer.parseInt(split[1]));
            getMessage().reply("音量を" + split[1] + "に変更しました！").queue();
            return;
        }

        getMessage().reply("えらーですっ\n今は再生していません！！").queue();
    }
}
