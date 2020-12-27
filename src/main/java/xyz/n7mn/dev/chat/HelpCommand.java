package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

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


    public HelpCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        String helpText = "----- ななみちゃんbot ヘルプ Start -----\n" +
                "**※このメッセージは送信専用です。返信しても何もできませんのでご注意ください。**\n" +
                "**※バグの報告、要望などは https://discord.gg/FnjCMzP7d4 までお願いします。**\n" +
                "`n.vote`、`n.voteNt` -- アンケートを表示する(詳細はn.voteと打って詳細ヘルプを出してください)\n" +
                "`n.voteStop <メッセージリンクのURL>` -- アンケートを終了する\n" +
                "`n.ping` -- 応答を返す\n"+
                "`n.nullpo` または `n.ぬるぽ` -- ガッ\n"+
                "`n.dice` -- さいころを振る\n"+
                "`n.random <文字列1> <文字列2> <...> <文字列n>` -- 指定された文字列の中から一つを表示する\n" +
                "`n.play <URL>` -- 音楽を再生する(停止する場合は`n.stop`)\n" +
                "`n.musicVolume <0～100>` -- 音楽の音量を変更する。\n" +
                "`n.nowPlay` -- 今再生している音楽を取得する\n" +
                "`n.repeat` -- 今再生している音楽をループさせる\n" +
                "`n.burn` -- :fire:\n" +
                "`n.burst` -- どっかーん\n" +
                "`n.check` -- 動作確認\n" +
                "`n.role <ユーザーID or 名前>` -- 指定ユーザーの情報を確認する\n" +
                "`n.role <ユーザーID or 名前> <ロールID or ロールの名前>` -- 指定ユーザーのロールを追加または削除をする\n" +
                "`n.game` -- ゲームメニュー\n";
        StringBuffer sb = new StringBuffer(helpText);

        if (!getGuild().getId().equals("517669763556704258")){
            sb.append("--- 地震情報機能について ---\n"+
                    "地震情報機能は「nanami_setting」という名前でチャンネルを作成し\n" +
                    "「jisin: <情報を流したいテキストチャンネルのID>」とメッセージを送ってください。\n" +
                    "----- ななみちゃんbot ヘルプ End ----");
        } else {
            sb.append("---- ゆるり鯖Discord限定機能 ----\n" +
                    "`n.gold` -- こうたちゃんの金装備は神！\n" +
                    "`n.kouta` -- 金装備のこうたさん\n" +
                    "`n.れにょこ` or `n.renyoko` or `n.ﾚﾆｮｺ` or `n.レニョコ` -- 虫特攻はいい！\n" +
                    "`n.sand` -- Crousandさん\n" +
                    "`n.n3m_` -- :100:\n" +
                    "`n.sc` -- 架空のスパチャを送る\n" +
                    "`n.pan` -- パンマスター\n" +
                    "`n.hentai` -- へんたいっ！\n" +
                    "`n.yululi` -- ゆるり\n" +
                    "`n.poti` -- ポチ\n" +
                    "`n.kuretiki` -- クレチキ\n" +
                    "`n.ys` -- ゆるり鯖全体\n" +
                    "----- ななみちゃんbot ヘルプ End ----");
        }

        PrivateChannel sendUserDM = getUser().openPrivateChannel().complete();
        sendUserDM.sendMessage(sb.toString()).queue();

        getMessage().reply("ななみちゃんbotのヘルプをDMにお送りいたしましたっ！").queue();

    }

    public static void run(PrivateChannel ch){
        if (ch != null){
            String helpText = "----- ななみちゃんbot ヘルプ Start -----\n" +
                    "**※このメッセージは送信専用です。返信しても何もできませんのでご注意ください。**\n" +
                    "**※バグの報告、要望などは https://discord.gg/FnjCMzP7d4 までお願いします。**\n" +
                    "**※導入されているサーバーのどこかのチャンネルで送ってください！！**\n" +
                    "`n.vote`、`n.voteNt` -- アンケートを表示する(詳細はn.voteと打って詳細ヘルプを出してください)\n" +
                    "`n.voteStop <メッセージリンクのURL>` -- アンケートを終了する\n" +
                    "`n.ping` -- 応答を返す\n"+
                    "`n.nullpo` または `n.ぬるぽ` -- ガッ\n"+
                    "`n.dice` -- さいころを振る\n"+
                    "`n.random <文字列1> <文字列2> <...> <文字列n>` -- 指定された文字列の中から一つを表示する\n" +
                    "`n.play <URL>` -- 音楽を再生する(停止する場合は`n.stop`)\n" +
                    "`n.musicVolume <0～100>` -- 音楽の音量を変更する。\n" +
                    "`n.nowPlay` -- 今再生している音楽を取得する\n" +
                    "`n.repeat` -- 今再生している音楽をループさせる\n" +
                    "`n.burn` -- :fire:\n" +
                    "`n.burst` -- どっかーん\n" +
                    "`n.check` -- 動作確認\n" +
                    "`n.role <ユーザーID or 名前>` -- 指定ユーザーの情報を確認する\n" +
                    "`n.role <ユーザーID or 名前> <ロールID or ロールの名前>` -- 指定ユーザーのロールを追加または削除をする\n" +
                    "`n.game` -- ゲームメニュー\n";
            StringBuffer sb = new StringBuffer(helpText);

            List<Guild> guilds = ch.getUser().getMutualGuilds();

            boolean isYululi = false;
            for (Guild guild : guilds){

                if (guild.getId().equals("517669763556704258")){
                    isYululi = true;
                    break;
                }

            }

            if (!isYululi){
                sb.append("--- 地震情報機能について ---\n"+
                        "地震情報機能は「nanami_setting」という名前でチャンネルを作成し\n" +
                        "「jisin: <情報を流したいテキストチャンネルのID>」とメッセージを送ってください。\n" +
                        "----- ななみちゃんbot ヘルプ End ----");
            } else {
                sb.append("---- ゆるり鯖Discord限定機能 ----\n" +
                        "`n.gold` -- こうたちゃんの金装備は神！\n" +
                        "`n.kouta` -- 金装備のこうたさん\n" +
                        "`n.れにょこ` or `n.renyoko` or `n.ﾚﾆｮｺ` or `n.レニョコ` -- 虫特攻はいい！\n" +
                        "`n.sand` -- Crousandさん\n" +
                        "`n.n3m_` -- :100:\n" +
                        "`n.sc` -- 架空のスパチャを送る\n" +
                        "`n.pan` -- パンマスター\n" +
                        "`n.hentai` -- へんたいっ！\n" +
                        "`n.yululi` -- ゆるり\n" +
                        "`n.yululi2` -- ゆるり\n" +
                        "`n.poti` -- ポチ\n" +
                        "`n.kuretiki` -- クレチキ\n" +
                        "`n.ys` -- ゆるり鯖全体\n" +
                        "----- ななみちゃんbot ヘルプ End ----");
            }

            ch.sendMessage(sb.toString()).queue();
        }
    }
}
