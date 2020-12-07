package xyz.n7mn.dev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.RestAction;
import xyz.n7mn.dev.music.GuildMusicManager;
import xyz.n7mn.dev.music.PlayerManager;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ChatMessage {

    private final JDA jda;
    private final Guild guild;

    private final User author;
    private final Message message;
    private final String text;

    public ChatMessage(User author, Message message){

        this.author = author;
        this.message = message;
        this.text = message.getContentRaw();

        this.jda = message.getJDA();
        this.guild = message.getGuild();

    }

    public void run(){
        // コマンドの最初の受ける地点。新コマンドはメソッドを増やしてここに追記する

        if (text.equals("n.help")){
            help();
        }

        if (text.toLowerCase().startsWith("n.vote")){
            vote();
        }

        if (text.equals("n.ping")){
            ping();
        }

        if (text.startsWith("n.nullpo") || text.startsWith("n.ぬるぽ")){
            nullpo();
        }

        if (text.toLowerCase().startsWith("n.dice")){
            dice();
        }

        if (text.toLowerCase().startsWith("n.random")){
            random();
        }

        if (text.toLowerCase().startsWith("n.play")){
            music(true);
        }

        if (text.toLowerCase().startsWith("n.stop")){
            music(false);
        }

        if (text.equals("n.check")){
            systemCheck();
        }

    }

    private void help(){

        String helpText = "----- ななみちゃんbot ヘルプ Start -----\n" +
                "**※このメッセージは送信専用です。返信しても何もできませんのでご注意ください。**\n" +
                "**※バグの報告、要望などは https://discord.gg/QP2hRSQaVV までお願いします。**\n" +
                "`n.vote`、`n.voteNt` -- アンケートを表示する(詳細はn.voteと打って詳細ヘルプを出してください)\n" +
                "`n.ping` -- 応答を返す\n"+
                "`n.nullpo` または `n.ぬるぽ` -- ガッ\n"+
                "`n.dice` -- さいころを振る\n"+
                "`n.random <文字列1> <文字列2> <...> <文字列n>` -- 指定された文字列の中から一つを表示する\n" +
                "`n.play <URL>` -- 音楽を再生する(停止する場合は`n.stop`)\n" +
                "`n.check` -- 動作確認\n\n" +
                "--- 地震情報機能について ---\n" +
                "地震情報機能は「nanami_setting」という名前でチャンネルを作成し\n" +
                "「jisin: <情報を流したいテキストチャンネルのID>」とメッセージを送ってください。\n\n" +
                "----- ななみちゃんbot ヘルプ End ----";

        PrivateChannel sendUserDM = author.openPrivateChannel().complete();
        sendUserDM.sendMessage(helpText).queue();

        message.reply("ななみちゃんbotのヘルプをDMにお送りいたしましたっ！").queue();

    }

    private void dice(){

        String[] split = text.split(" ");

        if (split.length == 1){

            int i = (new Random().nextInt(Integer.MAX_VALUE) % 6) + 1;
            message.getTextChannel().sendMessage("さいころの結果は" + i + "です。").queue();
            return;

        }

        if (split.length == 2){

            int i = (new Random().nextInt(Integer.MAX_VALUE) % Integer.parseInt(split[1])) + 1;
            message.getTextChannel().sendMessage("さいころの結果は" + i + "です。").queue();

        }
    }

    private void random(){

        String[] split = text.split(" ", -1);
        int i = (new Random().nextInt(Integer.MAX_VALUE) % split.length);
        message.getTextChannel().sendMessage("選ばれたのは「" + split[i]+"」だよっ！").queue();

    }

    private void ping(){

        OffsetDateTime idLong = message.getTimeCreated();

        Date date = Date.from(idLong.toInstant());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        message.getTextChannel().sendMessage("応答したよっ\n(送信元メッセージの日時：" + sdf.format(date) + " (JST))").queue();

    }

    private void nullpo(){

        String[] msg = new String[]{
                "がっ！",
                "ガッ！",
                "`" +
                        "." +
                        "　　Λ＿Λ　　＼＼\n" +
                        "　 （　・∀・）　　　|　|　ｶﾞｯ\n" +
                        "　と　　　　）　 　 |　|\n" +
                        "　　 Ｙ　/ノ　　　 人\n" +
                        "　　　 /　）　 　 < 　>_Λ∩\n" +
                        "　 ＿/し'　／／. Ｖ｀Д´）/\n" +
                        "　（＿フ彡　　　　　 　　/　`"
        };

        message.getChannel().sendMessage(msg[new Random().nextInt(msg.length)]).queue();

    }

    private void msgSend(){

        message.delete().queue();

        String[] split = text.split(" ", -1);

        if (split.length == 4){

            try {

                RestAction<User> user = jda.retrieveUserById(split[1]);
                PrivateChannel dm = user.complete().openPrivateChannel().complete();

                int count = Integer.parseInt(split[2]);

                for (int i = 1; i <= count; i++){

                    dm.sendMessage(split[3]).queue();

                }

            } catch (Exception e){

                e.printStackTrace();

            }

        }

    }

    private void music(boolean play){

        if (play){
            if (message.isWebhookMessage() || author.isBot()){
                return;
            }

            AudioManager audioManager = guild.getAudioManager();

            String[] split = text.split(" ", -1);
            if (split.length != 2 && split.length != 3){
                message.reply("音楽を再生するには\nボイスチャンネルに入ってから\n`n.play <URL>` または `n.play <URL> <0-100>`でお願いしますっ！").queue();
                return;
            }

            boolean find = false;

            List<VoiceChannel> voiceChannels = guild.getVoiceChannels();
            // voiceChannel = voiceChannels.get(0);

            VoiceChannel voiceChannel = null;
            for (VoiceChannel vc : voiceChannels){

                try {

                    // System.out.println(vc.getName() + " : " + vc.getMembers().size());

                    if (vc.getMembers().size() != 0){
                        List<Member> members = vc.getMembers();

                        for (Member member : members){

                            if (author.getId().equals(member.getId())){
                                find = true;
                                voiceChannel = vc;
                            }

                        }

                    }

                } catch (Exception e){
                    // e.printStackTrace();
                }

            }

            if (!find){
                message.reply("どこかのボイスチャンネルに入ってくださいっ！！").queue();
                return;
            }

            if (!split[1].toLowerCase().startsWith("http")){
                message.reply("わたしの知ってるURLじゃないみたい...").queue();
                return;
            }

            message.delete().queue();
            //Guild guild = event.getJDA().getGuildById(event.getGuild().getId());
            audioManager.openAudioConnection(voiceChannel);

            PlayerManager Playermanager = PlayerManager.getINSTANCE();
            Playermanager.loadAndPlay(message.getTextChannel(), split[1]);

            if (split.length == 3){
                Playermanager.getGuildMusicManager(guild).player.setVolume(Integer.parseInt(split[2]));
            } else {
                Playermanager.getGuildMusicManager(guild).player.setVolume(100);
            }
        } else {
            PlayerManager Playermanager = PlayerManager.getINSTANCE();
            GuildMusicManager guildMusicManager = Playermanager.getGuildMusicManager(guild);
            guildMusicManager.player.stopTrack();

            AudioManager audioManager = guild.getAudioManager();
            audioManager.closeAudioConnection();

            message.delete().queue();
            message.getTextChannel().sendMessage("再生を終了しましたっ！").queue();
        }
    }

    private void systemCheck(){

        StringBuffer sb = new StringBuffer();
        sb.append("---- ななみちゃんbot実行テスト結果 ----\n");
        sb.append("応答 : OK");
        message.reply(sb.toString()).queue();

        TextChannel jisinChannel = null;

        boolean chCheck = false;
        try {
            List<TextChannel> textChannels = message.getGuild().getTextChannels();


            for (TextChannel channel : textChannels) {
                if (channel.getName().equals("nanami_setting")) {
                    jisinChannel = channel;
                    chCheck = true;
                    break;
                }
            }


        } catch (Exception e){
            message.reply("(テキストチャンネルの一覧取得に失敗しました。一部機能の実行に影響が出る可能性があります。)").queue();
        }

        if (chCheck){
            jisinChannel.getHistoryAfter(1, 100).queue((messageHistory -> {
                List<Message> retrievedHistory = messageHistory.getRetrievedHistory();
                for (Message message1 : retrievedHistory){

                    String contentRaw = message1.getContentRaw();
                    if (contentRaw.startsWith("jisin: ")){

                        String st = contentRaw.replaceAll("jisin: ", "");
                        TextChannel textChannelById = message1.getGuild().getTextChannelById(st);
                        if (textChannelById != null){

                            try {
                                textChannelById.sendMessage("test").queue((message2 -> {
                                    message2.delete().queue();
                                    message1.reply("地震情報 : OK").queue();
                                }));

                            } catch (Exception e){
                                message.reply("地震情報 : NG (チャンネルへの送信権限(、削除権限)がないようです？)").queue();
                            }

                        } else {
                            message.reply("地震情報 : NG (指定チャンネルが存在しません。)").queue();
                        }

                    }

                }
            }));
        }
        if (jisinChannel == null) {
            message.reply("地震情報 : NG(チャンネルの存在が確認できません。)\n").queue();
        }

    }

    private void vote(){

        // 前身のn7mn-VoteBot対策
        TextChannel textChannel = message.getTextChannel();
        List<Member> members = textChannel.getMembers();
        for (Member member : members){

            if (member.getUser().getId().equals("781130665906274317")){
                return;
            }

        }

        if (text.toLowerCase().equals("n.vote") || text.equals("n.voteNt")){

            message.delete().queue();

            String msg = message.getAuthor().getAsTag()+"さんっ！\n" +
                    "n.voteのヘルプですっ！\n" +
                    "コマンドの書き方は\n" +
                    "`n.vote <タイトル> <選択肢1> <選択肢2> <選択肢3> <...> <選択肢19>` または\n" +
                    "`n.vote\n<タイトル>\n<選択肢1>\n<選択肢2>\n<選択肢3>\n<...>\n<選択肢19>`\nですっ！\n" +
                    "タイトルが必要じゃない場合はn.voteNtとつけて同じようにしてくださいっ！！";

            textChannel.sendMessage(msg).queue();
            return;

        }

        String[] regional = new String[]{
                "\uD83C\uDDE6",
                "\uD83C\uDDE7",
                "\uD83C\uDDE8",
                "\uD83C\uDDE9",
                "\uD83C\uDDEA",
                "\uD83C\uDDEB",
                "\uD83C\uDDEC",
                "\uD83C\uDDED",
                "\uD83C\uDDEE",
                "\uD83C\uDDEF",
                "\uD83C\uDDF0",
                "\uD83C\uDDF1",
                "\uD83C\uDDF2",
                "\uD83C\uDDF3",
                "\uD83C\uDDF4",
                "\uD83C\uDDF5",
                "\uD83C\uDDF6",
                "\uD83C\uDDF7",
                "\uD83C\uDDF8",
                "\uD83C\uDDF9"
        };


        if (text.toLowerCase().startsWith("n.vote") && !text.startsWith("n.voteNt")){

            String[] string;
            if (text.split("\n",-1).length >= 3){
                string = text.split("\n", -1);
            } else {
                string = text.split(" ", -1);
            }

            if ((string.length - 2) > regional.length){
                message.reply("えらーですっ！選択肢が多すぎます！！").queue();
                return;
            }

            message.delete().queue();

            StringBuffer sb = new StringBuffer();
            sb.append("--- 以下の内容で投票を開始しました。 リアクションで投票してください。 ---\n投票タイトル：");
            sb.append(string[1]);
            sb.append("\n\n");


            for (int i = 0; i < (string.length - 2); i++){

                sb.append(regional[i]);
                sb.append(" : ");
                sb.append(string[i + 2]);
                sb.append("\n");

            }
            sb.append("\n(");
            sb.append(author.getName());
            sb.append(" さんが投票を開始しました)");

            textChannel.sendMessage(sb.toString()).queue(message -> {

                for (int i = 1; i < (string.length - 1); i++){
                    message.addReaction(regional[i - 1]).queue();
                }

            });
            return;
        }

        if (text.startsWith("n.voteNt")){

            String[] string;
            if (text.split("\n",-1).length >= 2){
                string = text.split("\n", -1);
            } else {
                string = text.split(" ", -1);
            }

            if ((string.length - 1) > regional.length){
                message.reply("えらーですっ！選択肢が多すぎます！！").queue();
                return;
            }

            message.delete().queue();

            StringBuffer sb = new StringBuffer();
            sb.append("--- 以下の内容で投票を開始しました。 リアクションで投票してください。 ---");
            sb.append("\n\n");

            for (int i = 0; i < (string.length - 1); i++){

                sb.append(regional[i]);
                sb.append(" : ");
                sb.append(string[i + 1]);
                sb.append("\n");

            }
            sb.append("\n(");
            sb.append(author.getName());
            sb.append(" さんが投票を開始しました)");

            textChannel.sendMessage(sb.toString()).queue(message -> {

                for (int i = 0; i < (string.length - 1); i++){
                    message.addReaction(regional[i]).queue();
                }

            });
        }

    }
}
