package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
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
        AudioManager audioManager = getGuild().getAudioManager();

        if (audioManager.isConnected()){

            PlayerManager Playermanager = PlayerManager.getINSTANCE();
            Playermanager.Repeat(getTextChannel());

        }
    }
}
