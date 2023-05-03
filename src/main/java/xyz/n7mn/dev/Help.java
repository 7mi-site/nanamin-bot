package xyz.n7mn.dev;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import xyz.n7mn.dev.api.ver;

import java.awt.*;

public class Help {

    private final JDA jda;
    private final SlashCommandInteractionEvent event;

    public Help(JDA jda, SlashCommandInteractionEvent event) {
        this.jda = jda;
        this.event = event;
    }

    public void run(){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.PINK);
        builder.setTitle("ななみちゃんbot ヘルプ");
        builder.setDescription("" +
                "質問や要望、バグ報告は https://discord.gg/FnjCMzP7d4 までお願いします。\n" +
                (ver.get().matches(".*dev.*") ? "**このBotはベータ版です！予告なしで停止する場合があります！！*" : "")
        );

        builder.addField("/help", "いまあなたが見ているやつですっ！！", false);
        builder.addField("/vote", "投票機能\n投票期間を「なし」にした場合は/vote-stopで終了させることができます。\n\n例\n/vote 朝ごはん食べる？ 2024-12-01 00:00:00 食べる 食べない なにそれ？ ご飯を食べられない人だっているんですよ！！！ それってあなたの感想ですよね？", false);
        // builder.addField("/game", "ミニゲーム機能\n\n実装し直し中です。しばらく待っててね！", false);
        builder.addField("/music", "音楽再生機能", false);
        builder.addField("/nanami-version", "バージョン情報を出す。\nバグ報告に役にたつのでそのときはお願いしますっ", false);
        //builder.addField("/nanami-setting", "ななみちゃんbotの設定ができるコマンドです。 \n昔のnanami_settingチャンネルの代わりですっ", false);

        OptionMapping option = event.getOption("送信方式");

        if (option != null && option.getAsString().equals("d")){
            jda.getUserById(event.getMember().getId()).openPrivateChannel().queue((s->{
                s.sendMessageEmbeds(builder.build()).queue();
                event.reply("ヘルプをDMに送信しましたっ！").setEphemeral(true).queue();
            }));

            return;
        }

        if (option != null && option.getAsString().equals("a")){
            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            return;
        }

        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }

}
