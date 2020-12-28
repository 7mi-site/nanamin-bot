package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class MusicCommand extends CommandClassInterface {
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

    public MusicCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("音楽メニュー");
        builder.setDescription(
                        "`n.play <URL> <音量>` --- 音楽を再生する (音量は省略可能)\n" +
                        "`n.stop` --- 音楽再生をやめてボイスチャンネルから退出する\n" +
                        "`n.repeat` --- 1曲リピートモードにする / 1曲リピートモードをやめる\n" +
                        "`n.nowPlay` --- 今再生している曲の名前とリンクを表示する\n" +
                        "`n.musicVolume <音量>` --- 音量調整をする。"
        );
        builder.setColor(Color.PINK);

        getMessage().reply("音楽機能でできることはこちらっ！").embed(builder.build()).queue();

    }
}
