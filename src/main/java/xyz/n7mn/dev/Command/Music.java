package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.awt.*;

public class Music extends Chat {
    public Music(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("音楽メニュー");
        builder.setDescription("" +
                "※ 再生中、ボイスチャンネルに誰もいなくなった場合\n" +
                "　 自動で再生を停止してボイスチャンネルから抜けるようになっています！\n" +
                "`n.play <URL> (音量)` --- 音楽を再生する (音量は省略可能)\n" +
                "`n.stop` --- 音楽再生をやめてボイスチャンネルから退出する\n" +
                "`n.repeat` --- 1曲リピートモードにする / 1曲リピートモードをやめる\n" +
                "`n.nowPlay` --- 今再生している曲の名前とリンクを表示する\n" +
                "`n.musicVolume <音量>` --- 音量調整をする。(`n.volume`でも可能ですっ)\n"+
                "`n.musicSkip` --- 曲をスキップする。(`n.skip`でも可能ですっ)"
        );
        builder.setColor(Color.PINK);

        getMessage().reply("音楽機能でできることはこちらっ！").setEmbeds(builder.build()).queue();
    }
}
