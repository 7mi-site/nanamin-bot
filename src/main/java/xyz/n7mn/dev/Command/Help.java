package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.NanamiFunction;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Help extends Chat {

    private static List<HelpData> helpData = new ArrayList<>();

    private static final String voteText1 = "" +
            "`n.vote` -- アンケートをタイトルありで表示する\n" +
            "`n.voteNt` -- アンケートをタイトルなしで表示する\n" +
            "`n.voteStop <メッセージリンクのURL>` -- アンケートを終了する" +
            "`n.voteCheck <メッセージリンクのURL>` -- アンケートの途中結果を見る";

    private static final String voteText2 = "" +
            "`n.vote`、`n.voteNt`の使い方\n" +
            "記入例 その1\n```n.vote <時間> <タイトル> <選択肢1> <選択肢2> <選択肢3> <...> <選択肢20>``` \n" +
            "記入例 その2\n```n.vote\n<時間>\n<タイトル>\n<選択肢1>\n<選択肢2>\n<選択肢3>\n<...>\n<選択肢20>```\n" +
            "記入例 その3 (タイトルなしの場合)\n```n.voteNt <時間> <タイトル> <選択肢1> <選択肢2> <選択肢3> <...> <選択肢20>```\n" +
            "\n" +
            "時間指定方法\n" +
            "\n" +
            "`t:(時間)s` または `t:(時間)` --- 秒\n" +
            "`t:(時間)m` --- 分\n" +
            "`t:(時間)h` --- 時\n" +
            "`t:(時間)d` --- 日\n" +
            "```\n" +
            "\n\n" +
            "`n.voteStop`の使い方\n" +
            "`n.voteStop <メッセージリンクURL>` -- 指定した投票を終了させる";

    private static final String roleText1 = "" +
            "`n.role` -- ロール管理コマンド`";

    private static final String roleText2 = "" +
            "`n.role`の使い方\n" +
            "書式：`n.role <ユーザーIDまたは名前> (ロールの名前またはロールのID)`\n" +
            "記入例 その1 (その人のロールを確認する場合)\n```n.role <ユーザーIDまたは名前>``` \n" +
            "記入例 その2 (その人のロールを追加/削除する場合)\n```n.role <ユーザーIDまたは名前> <ロールの名前またはロールのID>```";

    private static final String funText = "" +
            "`n.nullpo` または n.ぬるぽ -- ガッ\n" +
            "`n.dice` -- さいころを振る\n" +
            "`n.random <文字列1> <文字列2> <...> <文字列n>` -- 指定された文字列の中から一つを表示する\n" +
            "`n.burn` -- :fire:\n" +
            "`n.burst` -- どっかーん\n" +
            "`n.aisatu` または `n.あいさつ` -- あいさつ";

    private static final String musicText1 = "" +
            "`n.play` --- 音楽再生\n" +
            "`n.stop` --- 音楽停止\n" +
            "`n.repeat` --- 1曲リピートモード設定\n" +
            "`n.nowPlay` --- 曲の名前とリンクを表示\n" +
            "`n.musicVolume` --- 音量調整\n" +
            "`n.musicSkip` --- 曲をスキップ";

    private static final String musicText2 = "" +
            "`n.play <URL> <音量>` --- 音楽を再生する (音量は省略可能)\n" +
            "`n.stop` --- 音楽再生をやめてボイスチャンネルから退出する\n" +
            "`n.repeat` --- 1曲リピートモードにする / 1曲リピートモードをやめる\n" +
            "`n.nowPlay` --- 今再生している曲の名前とリンクを表示する\n" +
            "`n.musicVolume <音量>` --- 音量調整をする。\n" +
            "`n.musicSkip` --- 曲をスキップする";

    private static final String gameText1 = "" +
            "`n.money` --- 現在の所持金\n" +
            "`n.rank` --- 所持金ランキング\n" +
            "~~`n.bank` --- ななみちゃん銀行~~\n" +
            "`n.slot` --- スロット\n" +
            "`n.yosogame` --- 数字予想ゲーム\n" +
            "`n.fx` --- FXもどき\n" +
            "`n.omikuji` --- おみくじ\n" +
            "`n.nomoney` --- ななみちゃん救済\n" +
            "`n.nomoney2` --- ？？？";

    private static final String gameText2 = "" +
            "`n.money` --- 現在の所持金を表示する\n" +
            "`n.money pay <相手のID or 名前> <金額>` --- 指定した相手に送金をする\n" +
            "~~`n.bank` --- ななみちゃん銀行~~\n" +
            "`n.rank` --- 所持金ランキング\n" +
            "`n.slot` --- スロット (1回100ななみコイン　最大当たり10倍)\n" +
            "`n.yosogame <賭け金> <予想数字>` --- 数字予想ゲーム (当たり10倍)\n" +
            "`n.fx <賭け金>` --- FXもどき\n" +
            "`n.omikuji` --- おみくじ\n" +
            "`n.nomoney` --- ななみちゃん救済 (所持コイン < -1000のときのみ使用可能)\\n\" +\n" +
            "`n.nomoney2` --- ？？？";

    private static final String quizText1 = "" +
            "`n.QuizData`\n" +
            "`n.QuizRank`\n" +
            "`n.QuizPlay`";

    private static final String quizText2 = "" +
            "`n.QuizData`\n" +
            "`n.QuizRank`\n" +
            "`n.QuizPlay`";

    private static final String jisinText = "" +
            "地震情報機能はnanami_settingという名前でチャンネルを作成し\n" +
            "`jisin: <情報を流したいテキストチャンネルのID>`と書いてください。";

    private static final String yululiText = "" +
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
            "`n.ys` -- ゆるり鯖全体\n" +
            "`n.fuck` -- thinking\n" +
            "`n.ban` -- ？" +
            "`n.lost` -- :thinking:\n";


    private static void init(){
        helpData.clear();
        // 全体 (999まで)
        helpData.add(new HelpData(0, "バク報告・要望・質問などの問い合わせ先", "https://discord.gg/FnjCMzP7d4"));
        helpData.add(new HelpData(1, "投票機能 (詳細は`n.vote`)", voteText1));
        helpData.add(new HelpData(2, "投票機能", voteText2));
        helpData.add(new HelpData(3, "ロール管理機能 (詳細は`n.help role`)", roleText1));
        helpData.add(new HelpData(4, "ロール管理機能", roleText2));
        helpData.add(new HelpData(5, "お遊び機能", funText));
        helpData.add(new HelpData(6, "音楽機能 (詳細は`n.music`)", musicText1));
        helpData.add(new HelpData(7, "音楽機能", musicText2));
        helpData.add(new HelpData(8, "ゲーム機能 (詳細は`n.game`)", gameText1));
        helpData.add(new HelpData(9, "ゲーム機能", gameText2));
        // helpData.add(new HelpData(10, "クイズ機能 (詳細は`n.quiz`)", quizText1));
        // helpData.add(new HelpData(11, "クイズ機能", quizText2));
        helpData.add(new HelpData(999, "その他", "" +
                "`n.msg` -- メッセージの情報を取得する\n"
        ));

        // 1コマンドヘルプ(1000-)
        // ゲーム関係は 1100 - 1199
        // クイズ関係は 1200 - 1299
        helpData.add(new HelpData(1000, "n.vote", "" +
                "区切りは全角スペース、半角スペース、改行が使えますっ！\n" +
                "例1 (時間指定をしない場合):\n" +
                "`n.vote 朝は何を食べましたか？ ごはん パン コンフレーク 何も食べていません。 食事を取らない派です。`\n" +
                "例2 (時間指定をする場合。この場合は5分間投票を受け付けます！):\n" +
                "`n.vote t:5m 昼は何を食べましたか？ ごはん パン コンフレーク 何も食べていません。 食事を取らない派です。`\n" +
                "例3 (改行の場合)\n" +
                "`n.vote\n" +
                "time:5m\n" +
                "夜は何を食べましたか？\n" +
                "ごはん\n" +
                "パン\n" +
                "コンフレーク\n" +
                "何も食べていません。\n" +
                "食事を取らない派です`\n" +
                "時間指定の方法は以下のとおりです。\n" +
                "書式：`t:(時間)`または`time:(時間)`\n" +
                "n秒の場合 `t:30`または `t:30s`\n" +
                "n分の場合 `t:5m`\n" +
                "n時間の場合 `t:1h`\n" +
                "n日間の場合 `t:3d`"
        ));
        helpData.add(new HelpData(1001, "n.voteNt", "" +
                "区切りは全角スペース、半角スペース、改行が使えますっ！\n" +
                "**こちらのコマンドではタイトルがいらない場合使ってくださいっ！**\n" +
                "例1 (時間指定をしない場合):\n" +
                "`n.vote ごはん パン コンフレーク 何も食べていません。 食事を取らない派です。`\n" +
                "例2 (時間指定をする場合。この場合は5分間投票を受け付けます！):\n" +
                "`n.vote t:5m ごはん パン コンフレーク 何も食べていません。 食事を取らない派です。`\n" +
                "例3 (改行の場合)\n" +
                "`n.vote\n" +
                "time:5m\n" +
                "ごはん\n" +
                "パン\n" +
                "コンフレーク\n" +
                "何も食べていません。\n" +
                "食事を取らない派です`\n" +
                "時間指定の方法は以下のとおりです。\n" +
                "書式：`t:(時間)`または`time:(時間)`\n" +
                "n秒の場合 `t:30`または `t:30s`\n" +
                "n分の場合 `t:5m`\n" +
                "n時間の場合 `t:1h`\n" +
                "n日間の場合 `t:3d`"
        ));
        helpData.add(new HelpData(1002, "n.voteStop", "" +
                "投票を任意のタイミングで終了させたいときに使うコマンドですっ\n" +
                "書式 `n.voteStop`または`n.voteStop (メッセージリンクURL)`\n" +
                "~~例1 (最新の投票を終了する場合):\n" +
                "`n.voteStop`\n" +
                "~~\n**※ 開発中のため現在は使えません**\n" +
                "例2 (指定した投票を終了する場合)\n" +
                "`n.voteStop https://discord.com/channels/781148066761670666/793118109312745477/793274800185278466`\n" +
                "(ここに書いてあるメッセージリンクのURLは実際のURLではありません！)"
        ));
        helpData.add(new HelpData(1003, "n.ping","" +
                "このコマンドは応答を返すコマンドですっ！\n" +
                "ななみちゃんbotがメッセージを確認、送信できているかをチェックすることができますっ！\n" +
                "(地震情報の設定がされているかをチェックする場合は`n.check`を使用してください。)"
        ));
        helpData.add(new HelpData(1004, "n.nullpo","ｶﾞｯ"));
        helpData.add(new HelpData(1005, "n.ぬるぽ","ｶﾞｯ"));
        helpData.add(new HelpData(1006, "n.dice","" +
                "書式 `n.dice`または`n.dice <数字>`\n" +
                "さいころを振ることができますっ"
        ));
        helpData.add(new HelpData(1007, "n.random","" +
                "書式 `n.random <文字列1> <文字列2> <...> <文字列n>`\n" +
                "指定された文字列の中から一つを表示しますっ"
        ));
        helpData.add(new HelpData(1008, "n.burn",":fire:"));
        helpData.add(new HelpData(1009, "n.burst","どっかーん"));

        helpData.add(new HelpData(1010, "n.play","" +
                "音楽を再生しますっ\n" +
                "書式 `n.play <URL> (音量)`\n" +
                "**※ 音量は省略可能です！**\n" +
                "**※ 一部対応していない音楽などがありますので再生できたらラッキー程度に思っていてくださいっ！**"
        ));
        helpData.add(new HelpData(1011, "n.stop","" +
                "音楽を停止しボイスチャンネルから退出しますっ\n" +
                "書式 `n.stop`"
        ));
        helpData.add(new HelpData(1012, "n.repeat","" +
                "1曲リピートモードの切り替えを行いますっ\n" +
                "書式 `n.repeat`"
        ));
        helpData.add(new HelpData(1013, "n.nowPlay","" +
                "現在の再生している音楽を表示しますっ\n" +
                "書式 `n.nowPlay`"
        ));
        helpData.add(new HelpData(1014, "n.musicVolume","" +
                "音楽の音量を調整しますっ！\n" +
                "書式 `n.musicVolume <音量>`または`n.volume <音量>`"
        ));
        helpData.add(new HelpData(1015, "n.volume","" +
                "音楽の音量を調整しますっ！\n" +
                "書式 `n.musicVolume <音量>`または`n.volume <音量>`"
        ));
        helpData.add(new HelpData(1016, "n.role",roleText2));

        helpData.add(new HelpData(1017, "n.voteCheck","" +
                "途中結果をDMに送りますっ\n]" +
                "書式 `n.voteCheck <メッセージURL>`"
        ));

        helpData.add(new HelpData(1018, "n.aisatu",""+"" +
                "あいさつを送りますっ！"));

        helpData.add(new HelpData(1100, "n.money","" +
                "現在の所持金を表示したり他の人に送金できたりしますっ\n" +
                "書式 `n.money`または`n.money pay <相手の名前またはユーザーID> <金額>`\n" +
                "(ここだけの話 所持金の上限は"+Integer.MAX_VALUE+"だよっ)"
        ));
        helpData.add(new HelpData(1101, "n.rank","" +
                "所持金ランキングを表示しますっ\n" +
                "書式 `n.rank`"
        ));
        helpData.add(new HelpData(1102, "n.slot","" +
                "1回100ななみコインでスロットを遊ぶことができますっ\n" +
                "書式 `n.slot`\n" +
                "あたりについて : \n" +
                "数字が3つ揃った場合は\n" +
                "  7が3つ揃った場合 10倍\n" +
                "  7以外で揃った場合 5倍\n" +
                "数字が`7 7 3`の場合は 8倍ですっ！\n"
        ));
        helpData.add(new HelpData(1103, "n.yosogame","" +
                "数字当てゲームですっ\n" +
                "書式 `n.yosogame <賭け金> <数字>`\n" +
                "数字は1桁の数字を入れてくださいっ\n" +
                "当たった場合は10倍ですっ"
        ));
        helpData.add(new HelpData(1104, "n.fx","" +
                "FXもどきをしますっ！\n" +
                "書式 `n.fx <賭け金>`\n" +
                "賭け金が100未満の場合は元値2倍モード\n" +
                "賭け金が100以上の場合は元値5倍モード\n" +
                "掛け金が1000以上の場合は元値10倍モード\n" +
                "掛け金が10000以上の場合は元値20倍モード\n" +
                "で実施されます。\n" +
                "元値20倍モードになればなるほど当たった場合はかなり獲得できますがその分マイナスになる可能性もあるので注意してくださいねっ！\n" +
                "(このゲームにのみ所持金マイナスが発生する場合があります。)"
        ));
        helpData.add(new HelpData(1105, "n.omikuji","" +
                "おみくじですっ 説明いらないよねっ？\n" +
                "書式 `n.omikuji`"
        ));
        helpData.add(new HelpData(1106, "n.nomoney","" +
                "借金返済ルーレットですっ！\n" +
                "書式 `n.nomoney`\n" +
                "所持金のマイナスが1000を超えてる場合のみルーレットができますっ\n" +
                "獲得金額は10,000～50,000のどれかですっ"
        ));
        helpData.add(new HelpData(1107, "n.bank","" +
                "ななみちゃん銀行\n" +
                "書式 `n.bank`\n" +
                "ああああああああああああああ\n" +
                "テキストテキストテキストテキスト\n" +
                "ああああああああああああああ\n" +
                "テキストテキストテキストテキスト\n"
        ));

        helpData.add(new HelpData(1200, "n.quizRank","" +
                ""
        ));
        helpData.add(new HelpData(1201, "n.rank2","" +
                ""
        ));

        // 限定隠し機能ヘルプ(2000-)
        helpData.add(new HelpData(2000, "地震情報について", jisinText));
        helpData.add(new HelpData(2001, "ゆるり鯖Discord向け機能", yululiText));
        helpData.add(new HelpData(2002, "せぶんくまん server向け機能", "" +
                "`n.sebunku` -- セブンク\n" +
                "`n.playsebunku` --- 台パン動画再生"
        ));
    }

    public Help(TextChannel textChannel, Message message) {
        super(textChannel, message);
        init();
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    public HelpData getHelpMessage(long id){
        for (HelpData data : helpData){

            if (id == data.getHelpNo()){
                return data;
            }

        }

        return null;
    }

    public static HelpData getHelpData(long id){
        init();
        for (HelpData data : helpData){

            if (id == data.getHelpNo()){
                return data;
            }

        }

        return null;
    }

    @Override
    public void run() {
        EmbedBuilder builder = new EmbedBuilder().clear().clearFields();
        if (getMessageText().toLowerCase().equals("n.help")){
            builder.setTitle("ななみちゃんbot (Ver "+ NanamiFunction.getVersion() +") ヘルプ").setDescription("" +
                    "**※ このメッセージは送信専用です。返信しても何もできませんのでご注意ください。**\n" +
                    "**※ 一部コマンドは`n.help <「n.」を除いたコマンド名>`にて詳細ヘルプが表示されます。**"
            ).setColor(Color.PINK);

            for (HelpData data : helpData){

                if (data.getHelpNo() == 2 || data.getHelpNo() == 4 || data.getHelpNo() == 7 || data.getHelpNo() == 9 || data.getHelpNo() == 11 || data.getHelpNo() >= 1000){
                    continue;
                }

                builder.addField(data.getHelpTitle(), data.getHelpMessage(), false);
            }

            HelpData extHelp = getHelpMessage(2000);
            if (getMessage().getGuild().getId().equals("517669763556704258")){
                extHelp = getHelpMessage(2001);
            } else if (getMessage().getGuild().getId().equals("711923508015398973")){
                getHelpMessage(2002);
            }
            builder.addField(extHelp.getHelpTitle(), extHelp.getHelpMessage(), false);

            PrivateChannel sendUserDM = getUser().openPrivateChannel().complete();
            sendUserDM.sendMessage(builder.build()).queue();
            getMessage().reply("ななみちゃんbotのヘルプをDMにお送りいたしましたっ！\n(もっと詳細なヘルプが欲しい場合は`n.vote`、`n.game`、`n.quiz`、`n.help <コマンド名(n.除く)>`のどれかを実行してくださいっ！！)").queue();
        } else {
            String[] text = getMessageText().split(" ", -1);
            if (text.length != 2){
                return;
            }

            for (HelpData help : helpData){
                if (help.getHelpTitle().toLowerCase().startsWith(text[1].toLowerCase()) || help.getHelpTitle().toLowerCase().endsWith(text[1].toLowerCase())){
                    builder.setTitle("ななみちゃんbot (Ver "+ NanamiFunction.getVersion() +") コマンド詳細ヘルプ");
                    builder.setDescription(text[1] + "の詳細ヘルプを表示しますっ");
                    builder.addField(help.getHelpTitle(), help.getHelpMessage(), false);
                    PrivateChannel sendUserDM = getUser().openPrivateChannel().complete();
                    sendUserDM.sendMessage(builder.build()).queue();
                    getMessage().reply("ななみちゃんbotのコマンド詳細ヘルプをDMにお送りいたしましたっ！").queue();
                    return;
                }
            }

            getMessage().reply("詳細ヘルプが存在しないコマンドみたい...").queue();
        }

    }

    public static void run(PrivateChannel channel, String text){
        init();
        EmbedBuilder builder = new EmbedBuilder().clear().clearFields();
        if (text.toLowerCase().equals("n.help")){
            builder.setTitle("ななみちゃんbot (Ver "+ NanamiFunction.getVersion() +") ヘルプ").setDescription("" +
                    "**※ このメッセージは送信専用です。返信しても何もできませんのでご注意ください。**\n" +
                    "**※ 一部コマンドは`n.help <「n.」を除いたコマンド名>`にて詳細ヘルプが表示されます。**"
            ).setColor(Color.PINK);

            for (HelpData data : helpData){

                if (data.getHelpNo() == 2 || data.getHelpNo() == 4 || data.getHelpNo() == 7 || data.getHelpNo() == 9 || data.getHelpNo() == 11 || data.getHelpNo() >= 1000){
                    continue;
                }

                builder.addField(data.getHelpTitle(), data.getHelpMessage(), false);
            }

            List<Guild> guildList = channel.getUser().getMutualGuilds();
            for (Guild guild : guildList) {

                if (guild.getId().equals("517669763556704258")){
                    HelpData extHelp = getHelpData(2001);
                    builder.addField(extHelp.getHelpTitle(), extHelp.getHelpMessage(), false);
                }

                if (guild.getId().equals("711923508015398973")){
                    HelpData extHelp = getHelpData(2002);
                    builder.addField(extHelp.getHelpTitle(), extHelp.getHelpMessage(), false);
                }

            }

            HelpData extHelp = getHelpData(2000);
            builder.addField(extHelp.getHelpTitle(), extHelp.getHelpMessage(), false);
            channel.sendMessage(builder.build()).queue();
        } else {
            String[] textSplit = text.split(" ", -1);
            if (textSplit.length != 2){
                return;
            }

            for (HelpData help : helpData){
                if (help.getHelpTitle().toLowerCase().equals(textSplit[1].toLowerCase()) || help.getHelpTitle().toLowerCase().endsWith(textSplit[1].toLowerCase())){
                    builder.setTitle("ななみちゃんbot (Ver "+ NanamiFunction.getVersion() +") コマンド詳細ヘルプ");
                    if (!textSplit[1].toLowerCase().startsWith("n.")){
                        builder.setDescription("n." + textSplit[1] + "の詳細ヘルプを表示しますっ");
                    } else {
                        builder.setDescription(textSplit[1] + "の詳細ヘルプを表示しますっ");
                    }
                    builder.addField(help.getHelpTitle(), help.getHelpMessage(), false);
                    channel.sendMessage(builder.build()).queue();
                    return;
                }
            }

            channel.sendMessage("詳細ヘルプが存在しないコマンドみたい...").queue();
        }
    }
}
