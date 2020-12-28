package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.NanamiSystem;

import java.awt.*;
import java.util.List;

public class HelpCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    private static EmbedBuilder builder = new EmbedBuilder();

    public HelpCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);

        builder.setTitle("ななみちゃんbot (Ver "+ NanamiSystem.getVersion() +") ヘルプ");
        builder.setDescription("**※このメッセージは送信専用です。返信しても何もできませんのでご注意ください。**");
        builder.addField("バグ報告・要望・質問などの問い合わせ先","https://discord.gg/FnjCMzP7d4",false);

        builder.addField(
                "投票機能 (詳細はn.vote)",
                "`n.vote` -- アンケートをタイトルありで表示する\n" +
                      "`n.voteNt` -- アンケートをタイトルなしで表示する\n" +
                      "`n.voteStop <メッセージリンクのURL>` -- アンケートを終了する",
                false
        );

        builder.addField(
                "お遊び",
                "`n.nullpo` または `n.ぬるぽ` -- ガッ\n" +
                      "`n.dice` -- さいころを振る\n" +
                      "`n.random <文字列1> <文字列2> <...> <文字列n>` -- 指定された文字列の中から一つを表示する\n" +
                      "`n.burn` -- :fire:\n" +
                      "`n.burst` -- どっかーん",
                false
        );

        builder.addField(
                "ロール",
                "`n.role <ユーザーID or 名前>` -- 指定ユーザーの情報を確認する\n" +
                      "`n.role <ユーザーID or 名前> <ロールID or ロールの名前>` -- 指定ユーザーのロールを追加または削除をする",
                false
        );

        builder.addField(
                "システム関係",
                "`n.ping` -- 応答を返す\n" +
                      "`n.check` -- 動作確認",
                false
        );

        builder.addField(
                "音楽機能 (詳細はn.music)",
                "`n.play <URL> <音量>` --- 音楽再生\n" +
                      "`n.stop` --- 音楽停止\n" +
                      "`n.repeat` --- 1曲リピートモード設定\n" +
                      "`n.nowPlay` --- 曲の名前とリンクを表示\n" +
                      "`n.musicVolume <音量>` --- 音量調整",
                false
        );

        builder.addField(
                "ゲーム機能 (詳細はn.game)",
                "`n.money` --- 現在の所持金\n" +
                      "`n.slot` --- スロット\n" +
                      "`n.yosogame <賭け金> <数字>` --- 数字予想ゲーム\n" +
                      "`n.fx <賭け金>` --- FXもどき\n" +
                      "`n.omikuji` --- おみくじ\n" +
                      "`n.rank` --- 所持金ランキング\n" +
                      "`n.nomoney` --- ななみちゃん救済",
                false
        );

        builder.addField(
                "クイズ機能 (詳細はn.quiz)",
                "(近日実装予定)",
                false
        );

        builder.setColor(Color.PINK);


    }

    @Override
    public void run() {
        if (!getGuild().getId().equals("517669763556704258")){

            builder.addField(
                    "地震情報について",
                    "地震情報機能は`nanami_setting`という名前でチャンネルを作成し\n`jisin: <情報を流したいテキストチャンネルのID>`と書いてください。",
                    false
            );

        } else {
            builder.addField(
                    "ゆるり鯖Discord限定機能",
                    "`n.gold` -- こうたちゃんの金装備は神！\n" +
                            "`n.kouta` -- 金装備のこうたさん\n" +
                            "`n.れにょこ` or `n.renyoko` or `n.ﾚﾆｮｺ` or `n.レニョコ` -- 虫特攻はいい！\n" +
                            "`n.sand` -- Crousandさん\n" +
                            "`n.n3m_` or `n.7mi_chan` -- :100:\n" +
                            "`n.sc` -- 架空のスパチャを送る\n" +
                            "`n.pan` -- パンマスター\n" +
                            "`n.hentai` -- へんたいっ！\n" +
                            "`n.yululi` -- ゆるり\n" +
                            "`n.poti` -- ポチ\n" +
                            "`n.kuretiki` -- クレチキ\n" +
                            "`n.ys` -- ゆるり鯖全体\n",
                    false
            );
        }

        PrivateChannel sendUserDM = getUser().openPrivateChannel().complete();
        sendUserDM.sendMessage(builder.build()).queue();
        getMessage().reply("ななみちゃんbotのヘルプをDMにお送りいたしましたっ！").queue();

    }

    public static void run(PrivateChannel ch){
        if (ch != null){

            List<Guild> guilds = ch.getUser().getMutualGuilds();

            boolean isYululi = false;
            for (Guild guild : guilds){

                if (guild.getId().equals("517669763556704258")){
                    isYululi = true;
                    break;
                }

            }

            if (!isYululi){
                builder.addField(
                        "地震情報について",
                        "地震情報機能は`nanami_setting`という名前でチャンネルを作成し\n`jisin: <情報を流したいテキストチャンネルのID>`と書いてください。",
                        false
                );
            } else {
                builder.addField(
                        "ゆるり鯖Discord限定機能",
                        "`n.gold` -- こうたちゃんの金装備は神！\n" +
                                "`n.kouta` -- 金装備のこうたさん\n" +
                                "`n.れにょこ` or `n.renyoko` or `n.ﾚﾆｮｺ` or `n.レニョコ` -- 虫特攻はいい！\n" +
                                "`n.sand` -- Crousandさん\n" +
                                "`n.n3m_` or `n.7mi_chan` -- :100:\n" +
                                "`n.sc` -- 架空のスパチャを送る\n" +
                                "`n.pan` -- パンマスター\n" +
                                "`n.hentai` -- へんたいっ！\n" +
                                "`n.yululi` -- ゆるり\n" +
                                "`n.poti` -- ポチ\n" +
                                "`n.kuretiki` -- クレチキ\n" +
                                "`n.ys` -- ゆるり鯖全体\n",
                        false
                );
            }

            ch.sendMessage(builder.build()).queue();
        }
    }
}
