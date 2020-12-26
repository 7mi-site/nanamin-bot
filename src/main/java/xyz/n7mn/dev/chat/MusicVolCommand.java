package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.music.PlayerManager;

public class MusicVolCommand extends CommandClassInterface {
    // 必要に応じて上のextendsするクラスを変える
    // 通常コマンド : CommandClassInterface
    // 投票系コマンド : VoteCommandClassInterface
    // ゲーム系コマンド : GameCommandClassInterface

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public MusicVolCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        String[] split = getMessageText().split(" ", -1);
        if (!getMessageText().toLowerCase().equals("n.musicvolume") && split.length == 1){
            getMessage().reply("えらーですっ\n`n.musicVolume <0～100>`でお願いしますっ！！").queue();
            return;
        }

        if (split.length != 2){
            getMessage().reply("えらーですっ\n`n.musicVolume <0～100>`でお願いしますっ！！").queue();
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
