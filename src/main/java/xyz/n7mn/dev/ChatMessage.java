package xyz.n7mn.dev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.RestAction;
import xyz.n7mn.dev.data.Vote;
import xyz.n7mn.dev.data.VoteComparator;
import xyz.n7mn.dev.data.VoteReaction;
import xyz.n7mn.dev.data.VoteReactionList;
import xyz.n7mn.dev.game.Money;
import xyz.n7mn.dev.game.MoneyList;
import xyz.n7mn.dev.game.Omikuji;
import xyz.n7mn.dev.game.Slot;
import xyz.n7mn.dev.music.GuildMusicManager;
import xyz.n7mn.dev.music.PlayerManager;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.*;

import static java.util.Collections.*;

public class ChatMessage {

    private final JDA jda;
    private final Guild guild;

    private final User author;
    private final Message message;
    private final String text;

    private final VoteReactionList voteReactionList;
    private final MoneyList moneyList;

    public ChatMessage(User author, Message message, VoteReactionList voteReactionList, MoneyList moneyList){

        this.author = author;
        this.message = message;
        this.text = message.getContentRaw();

        this.jda = message.getJDA();
        this.guild = message.getGuild();

        this.voteReactionList = voteReactionList;
        this.moneyList = moneyList;

    }

    public void run(){
        // コマンドの最初の受ける地点。新コマンドはメソッドを増やしてここに追記する
        if (text.startsWith("n.") || text.endsWith(".gold")){
            System.out.println("---- Debug ----\n" + author.getAsTag() + "\n" + text + "\n----- Debug -----");
            try {
                RestAction<User> nanami = jda.retrieveUserById("529463370089234466");
                PrivateChannel dm = nanami.complete().openPrivateChannel().complete();

                String debug = "----- Debug ----- \n" +
                        "サーバ名: " + guild.getName() +"\n" +
                        "発言チャンネル名: " + message.getTextChannel().getName() + "\n" +
                        "発言者: " + author.getAsTag() + "\n" +
                        "発言内容：`"+text+"`";
                dm.sendMessage(debug).queue();
            } catch (Exception e){
                e.printStackTrace();
            }
        }


        if (author.isBot()){
            return;
        }

        if (text.equals("n.ping")){
            ping();
            return;
        }

        if (text.equals("n.help")){
            help();
            return;
        }

        if (text.toLowerCase().startsWith("n.vote") && !text.toLowerCase().startsWith("n.votestop")){
            vote();
            return;
        }


        if (text.startsWith("n.nullpo") || text.startsWith("n.ぬるぽ")){
            nullpo();
            return;
        }

        if (text.toLowerCase().startsWith("n.dice")){
            dice();
            return;
        }

        if (text.toLowerCase().startsWith("n.random")){
            random();
            return;
        }

        if (text.toLowerCase().startsWith("n.play")){
            music(true);
            return;
        }

        if (text.toLowerCase().startsWith("n.stop")){
            music(false);
            return;
        }

        if (text.equals("n.check")){
            systemCheck();
            return;
        }

        if (text.startsWith("n.send")){
            msgSend();
            return;
        }

        if (text.equals("n.burn")){
            burn();
            return;
        }

        if (text.equals("n.gold")){
            gold();
            return;
        }

        if (text.equals("n.れにょこ") || text.equals("n.renyoko") || text.equals("n.ﾚﾆｮｺ") || text.equals("n.レニョコ")){
            renyoko();
            return;
        }

        if (text.equals("n.sand")){
            sand();
            return;
        }

        if (text.equals("n.sc")){
            superChat();
            return;
        }

        if (text.equals("n.hentai")){
            hentai();
            return;
        }

        if (text.endsWith(".gold") && !text.equals(".gold") && !text.equals("z.gold") && guild.getId().equals("517669763556704258")){
            gold();
            return;
        }

        if (text.equals("n.burst")){
            burst();
            return;
        }

        if (text.equals("n.pan")){
            pan();
            return;
        }

        if (text.startsWith("n.msg")){
            msg();
            return;
        }

        if (text.startsWith("n.kouta")){
            kouta();
            return;
        }

        if (text.toLowerCase().startsWith("n.votestop")){
            stopVote();
            return;
        }

        if (text.toLowerCase().startsWith("n.role")){
            role();
            return;
        }

        if (text.toLowerCase().startsWith("n.n3m_") || text.toLowerCase().startsWith("n.7mi_chan")){
            n3m();
            return;
        }

        if (text.toLowerCase().equals("n.game")){
            game();
            return;
        }

        if (text.toLowerCase().startsWith("n.money")){
            money();
            return;
        }

        if (text.toLowerCase().equals("n.slot")){
            slot();
            return;
        }

        if (text.toLowerCase().equals("n.baka")){
            baka();
            return;
        }

        if (text.toLowerCase().equals("n.omikuji")){
            omikuji();
        }
    }

    private void help(){

        String helpText = "----- ななみちゃんbot ヘルプ Start -----\n" +
                "**※このメッセージは送信専用です。返信しても何もできませんのでご注意ください。**\n" +
                "**※バグの報告、要望などは https://discord.gg/QP2hRSQaVV までお願いします。**\n" +
                "`n.vote`、`n.voteNt` -- アンケートを表示する(詳細はn.voteと打って詳細ヘルプを出してください)\n" +
                "`n.voteStop <メッセージリンクのURL>` -- アンケートを終了する\n" +
                "`n.ping` -- 応答を返す\n"+
                "`n.nullpo` または `n.ぬるぽ` -- ガッ\n"+
                "`n.dice` -- さいころを振る\n"+
                "`n.random <文字列1> <文字列2> <...> <文字列n>` -- 指定された文字列の中から一つを表示する\n" +
                "`n.play <URL>` -- 音楽を再生する(停止する場合は`n.stop`)\n" +
                "`n.burn` -- :fire:\n" +
                "`n.burst` -- どっかーん\n" +
                "`n.check` -- 動作確認\n" +
                "`n.role <ユーザーID or 名前>` -- 指定ユーザーの情報を確認する\n" +
                "`n.role <ユーザーID or 名前> <ロールID or ロールの名前>` -- 指定ユーザーのロールを追加または削除をする\n" +
                "`n.game` -- ゲームメニュー";
        StringBuffer sb = new StringBuffer(helpText);

        if (!message.getGuild().getId().equals("517669763556704258")){
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
                    "`n.sc` -- 架空のスパチャを送る\n" +
                    "`n.pan` -- パンマスター\n" +
                    "`n.hentai` -- へんたいっ！\n" +
                    "----- ななみちゃんbot ヘルプ End ----");
        }

        PrivateChannel sendUserDM = author.openPrivateChannel().complete();
        sendUserDM.sendMessage(sb.toString()).queue();

        message.reply("ななみちゃんbotのヘルプをDMにお送りいたしましたっ！").queue();

    }

    private void dice(){

        String[] split = text.split(" ");
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new SecureRandom().nextInt(Integer.MAX_VALUE));

        if (split.length == 1){

            int i = (secureRandom.nextInt(Integer.MAX_VALUE) % 6) + 1;
            message.getTextChannel().sendMessage("さいころの結果は" + i + "です。").queue();
            return;

        }

        if (split.length == 2){

            int i = (secureRandom.nextInt(Integer.MAX_VALUE) % Integer.parseInt(split[1])) + 1;
            message.getTextChannel().sendMessage("さいころの結果は" + i + "です。").queue();

        }
    }

    private void random(){
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new SecureRandom().nextInt(Integer.MAX_VALUE));

        String[] split = text.split(" ", -1);
        List<String> wordList = new ArrayList<>();
        wordList.addAll(Arrays.asList(split).subList(1, split.length));

        shuffle(wordList);


        int i = (secureRandom.nextInt(wordList.size()));
        message.getTextChannel().sendMessage("選ばれたのは「" + wordList.get(i)+"」だよっ！").queue();

    }

    private void ping(){

        OffsetDateTime idLong = message.getTimeCreated();

        Date date = Date.from(idLong.toInstant());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        message.getTextChannel().sendMessage("応答したよっ\n(送信元メッセージの日時：" + sdf.format(date) + " (JST))\n(応答時間計測中...)").queue(message1 -> {
            OffsetDateTime time = message1.getTimeCreated();
            Date to = Date.from(time.toInstant());
            message1.editMessage("応答したよっ\n(送信元メッセージの日時：" + sdf.format(date) + " (JST))\n" +
                    "(応答時間 : " + (to.getTime() - date.getTime()) + " ms)").queue();
        });

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

        if (text.toLowerCase().equals("n.vote") || text.toLowerCase().equals("n.votent")){

            message.delete().queue();

            String msg = message.getAuthor().getName()+"さんっ！\n" +
                    "n.voteのヘルプですっ！\n" +
                    "コマンドの書き方は\n" +
                    "`n.vote <時間> <タイトル> <選択肢1> <選択肢2> <選択肢3> <...> <選択肢19>` または\n" +
                    "`n.vote\n<時間>\n<タイトル>\n<選択肢1>\n<選択肢2>\n<選択肢3>\n<...>\n<選択肢19>`\nですっ！\n" +
                    "時間の指定は以下の通りでできます！ (`t:`は`time:`に置き換えてもできます！)\n" +
                    "`t:(時間)s または t:(時間) --- 秒\n" +
                    "t:(時間)m --- 分\n" +
                    "t:(時間)h --- 時\n" +
                    "t:(時間)d --- 日`\n" +
                    "タイトルが必要じゃない場合はn.voteNtとつけて同じようにしてくださいっ！！\n" +
                    "(投票を終了するには「n.voteStop <メッセージリンクのURL>」と入力してください)";

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

        String[] string;

        if (text.split("\n",-1).length >= 3){
            string = text.split("\n", -1);
        } else {
            String text1 = text.replaceAll("　"," ");
            string = text1.split(" ", -1);
        }

        if (string.length <= 3 && (string[1].toLowerCase().startsWith("t:") || string[1].toLowerCase().startsWith("time:"))){
            message.reply("えらーですっ！選択肢が見つかりませんっ！").queue();
            return;
        }

        List<String> vote;
        final String title;
        final String time;
        if (!text.toLowerCase().startsWith("n.votent")){
            if (string[1].toLowerCase().startsWith("t:") || string[1].toLowerCase().startsWith("time:")){
                vote = new ArrayList<>(Arrays.asList(string).subList(3, string.length));
                title = string[2];
                time = string[1];
            } else if (string[string.length - 1].toLowerCase().startsWith("t:") || string[string.length - 1].toLowerCase().startsWith("time:")){
                vote = new ArrayList<>(Arrays.asList(string).subList(2, string.length - 1));
                title = string[1];
                time = string[string.length - 1];
            } else {
                int i = 0;
                vote = new ArrayList<>();
                String time2 = null;
                String title2 = "";

                for (String t : string){
                    if (i == 0) {
                        i++;
                        continue;
                    }

                    if (i == 1){
                        title2 = t;
                        i++;
                        continue;
                    }

                    if (time2 == null && t.toLowerCase().startsWith("t:") || t.toLowerCase().startsWith("time:")){
                        time2 = t;
                    } else {
                        vote.add(t);
                    }

                    i++;
                }

                time = time2;
                title = title2;
            }
        } else {
            title = "";
            if (string[1].toLowerCase().startsWith("t:") || string[1].toLowerCase().startsWith("time:")){
                vote = new ArrayList<>(Arrays.asList(string).subList(2, string.length));
                time = string[1];
            } else if (string[string.length - 1].toLowerCase().startsWith("t:") || string[string.length - 1].toLowerCase().startsWith("time:")){
                vote = new ArrayList<>(Arrays.asList(string).subList(1, string.length - 1));
                time = string[string.length - 1];
            } else {
                int i = 0;
                vote = new ArrayList<>();
                String time2 = null;

                for (String t : string){

                    if (i == 0) {
                        i++;
                        continue;
                    }

                    if (time2 == null && t.toLowerCase().startsWith("t:") || t.toLowerCase().startsWith("time:")){
                        time2 = t;
                    } else {
                        vote.add(t);
                    }

                    i++;
                }

                time = time2;
            }
        }

        if (vote.size() == 0){
            message.reply("えらーですっ！選択肢が見つかりませんっ！").queue();
            return;
        }

        if (vote.size() == 1){
            message.reply("選択肢がひとつしか見つからない...").queue();
            return;
        }

        if (vote.size() > regional.length){
            message.reply("えらーですっ！選択肢が多すぎます！！").queue();
            return;
        }

        message.delete().queue();

        StringBuffer sb = new StringBuffer();
        if (text.toLowerCase().startsWith("n.votent")){
            sb.append("--- 以下の内容で投票を開始しました。 リアクションで投票してください。 ---");
        } else {
            sb.append("--- 以下の内容で投票を開始しました。 リアクションで投票してください。 ---\n投票タイトル：");
            sb.append(title);
        }

        sb.append("\n\n");


        for (int i = 0; i < vote.size(); i++){

            sb.append(regional[i]);
            sb.append(" : ");
            sb.append(vote.get(i));
            sb.append("\n");

        }
        sb.append("\n(");
        sb.append(author.getName());
        sb.append(" さんが投票を開始しました");

        if (time != null && getMs(time) != -1){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = new Date();
            long time1 = date.getTime() + getMs(time);;

            sb.append(" ");
            sb.append(simpleDateFormat.format(time1));
            sb.append("まで投票受付中です。");

        }
        sb.append(")");
        sb.append("\n**リアクション数が1ではない場合はしばらく待ってから投票してください！**");

        textChannel.sendMessage(sb.toString()).queue(message -> {

            for (int i = 0; i < vote.size(); i++){
                message.addReaction(regional[i]).queue();
            }

            if (time != null){

                long ms = getMs(time);

                if (ms != -1){

                    TimerTask task = new TimerTask() {
                        public void run() {
                            StringBuffer sb = new StringBuffer(message.getContentRaw());
                            message.getChannel().retrieveMessageById(message.getId()).queue(message1 -> {

                                List<MessageReaction> reactions = message1.getReactions();
                                message1.clearReactions().queue();

                                List<Vote> voteResultList = new ArrayList<>();
                                List<VoteReaction> list = voteReactionList.getList();

                                String[] raw = message.getContentRaw().split("\n");

                                int i = 3;
                                if (title.length() == 0){
                                    i = 2;
                                }

                                for (MessageReaction reaction : reactions){
                                    List<String> nlist = new ArrayList<>();
                                    for (VoteReaction voteReaction : list){
                                        if (voteReaction.getMessageId().equals(message.getId()) && voteReaction.getReactionEmote().getEmoji().equals(reaction.getReactionEmote().getEmoji())){
                                            if (voteReaction.getMember().getNickname() != null){
                                                nlist.add(voteReaction.getMember().getNickname());
                                            } else {
                                                nlist.add(voteReaction.getMember().getUser().getName());
                                            }
                                            // System.out.println("a");
                                        }
                                    }
                                    // System.out.println(nlist.size());
                                    voteResultList.add(new Vote(reaction.getReactionEmote().getEmoji(), raw[i], nlist.size(), nlist));
                                    i++;
                                }

                                voteResultList.sort(new VoteComparator());


                                sb.append("\n\n---- 投票結果 ----\n");
                                for (Vote vote : voteResultList){
                                    sb.append(vote.getTitle());
                                    sb.append(" ");
                                    sb.append(vote.getNameList().size());
                                    sb.append("票");
                                    if (vote.getNameList().size() != 0){
                                        sb.append(" (");
                                        for (String name : vote.getNameList()){
                                            sb.append(name);
                                            sb.append("さん,");
                                        }
                                        sb.append(")\n");
                                    } else {
                                        sb.append("\n");
                                    }
                                }


                                message1.editMessage(sb.toString().replaceAll(",\\)",")").replaceAll("まで投票受付中です。","まで投票受付しました。")).queue(message2 -> {
                                    message2.addReaction("\u2705").queue();
                                });

                            });


                        }
                    };

                    Timer timer = new Timer();
                    timer.schedule(task, ms);
                }
            }

        });
    }

    private void stopVote(){

        String[] split = text.split(" ", -1);
        if (split.length != 2){
            message.reply("えらーです！").queue();
            return;
        }

        String[] url = split[1].split("/", -1);

        Guild guild = jda.getGuildById(url[4]);
        if (guild == null){
            message.reply("見つからないよぉ").queue();
            return;
        }

        TextChannel textChannelById = guild.getTextChannelById(url[5]);
        if (textChannelById == null){
            message.reply("見つからないよぉ").queue();
            return;
        }


        textChannelById.retrieveMessageById(url[6]).queue(message1 -> {
            textChannelById.clearReactionsById(url[6]).queue();

            String raw = message1.getContentRaw();
            List<Member> members = guild.getMembers();

            boolean b = false;
            for (Member member : members){
                User user = jda.getUserById(member.getId());
                if (user != null){

                    if (raw.contains(user.getName())){
                        if (author.getName().equals(user.getName())){
                            b = true;
                            break;
                        }
                    }

                }

            }


            StringBuffer sb = new StringBuffer();
            sb.append(message1.getContentRaw());

            List<MessageReaction> reactions = message1.getReactions();

            if (!message1.getAuthor().getId().equals("781323086624456735") && !message1.getAuthor().getId().equals("785322639295905792")){
                message.reply("それはななみちゃんのメッセージじゃないよぉ").queue();
                return;
            }

            if (!b){
                message.reply("❗投票開始した人以外は投票終了できません！").queue();
                return;
            }

            if (reactions.size() <= 1){
                if (reactions.size() == 1 && reactions.get(0).getReactionEmote().getEmoji().equals("✅")){
                    message.reply("それは結果発表済みみたい...").queue();
                    return;
                }
                message.reply("？").queue();
                return;
            }

            message1.clearReactions().queue();

            List<Vote> voteResultList = new ArrayList<>();
            String[] raw1 = message1.getContentRaw().split("\n");
            int i = 3;
            if (!raw1[1].startsWith("投票タイトル")){
                i = 2;
            }
            for (MessageReaction reaction : reactions){
                List<VoteReaction> list = voteReactionList.getList();

                List<String> nlist = new ArrayList<>();
                for (VoteReaction voteReaction : list){
                    if (voteReaction.getMessageId().equals(message1.getId())){
                        if (voteReaction.getMember().getNickname() != null){
                            nlist.add(voteReaction.getMember().getNickname());
                        } else {
                            nlist.add(voteReaction.getMember().getUser().getName());
                        }
                    }
                }
                voteResultList.add(new Vote(reaction.getReactionEmote().getEmoji(), raw1[i],  nlist.size(), nlist));
                i++;
            }

            voteResultList.sort(new VoteComparator());

            sb.append("\n\n---- 投票結果 ----\n");
            for (Vote vote : voteResultList){
                sb.append(vote.getTitle());
                sb.append(" : ");
                sb.append(vote.getCount());
                sb.append("票");
                if (vote.getCount() != 0 && vote.getNameList().size() != 0){
                    sb.append(" (");
                    for (String name : vote.getNameList()){
                        sb.append(name);
                        sb.append("さん,");
                    }
                    sb.append(")\n");
                } else {
                    sb.append("\n");
                }

            }

            message1.editMessage(sb.toString().replaceAll(",\\)",")")).queue(message2 -> {
                message2.addReaction("✅").queue();
                message.delete().queue();
                message.getChannel().sendMessage("投票を終了しました！\n" + split[1]).queue();
            });
        });


    }

    private void gold(){

        message.getChannel().sendMessage("https://goldarmor-is.best/").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
            message1.suppressEmbeds(true).queue();
        });

    }

    private void burn(){

        message.getChannel().sendMessage("\uD83D\uDD25").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void renyoko(){

        message.getChannel().sendMessage("れにょこの虫特攻はいい！").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void sand(){

        message.getChannel().sendMessage("https://cdn.discordapp.com/emojis/620909371265908797.png?v=1").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void superChat(){

        int f = (new SecureRandom().nextInt(15) & 5) + 1;
        int count = (new SecureRandom().nextInt(16) & 4) + 1;


        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(f);

        for (int i = 0; i < count; i++){
            stringBuffer.append("0");
        }

        message.getChannel().sendMessage("ゆるりさんに "+stringBuffer.toString() + "円スパチャしました！").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void hentai(){

        // String[] list = new String[]{"しおぽぽ","ふーぷれす","ゆるり","みやゆ",author.getName()};
        List<String> a = new ArrayList<>();
        a.add("しおぽぽ");
        a.add("ふーぷれす");
        a.add("ゆるり");
        a.add("みやゆ");
        a.add(author.getName());
        shuffle(a);

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new SecureRandom().nextInt(Integer.MAX_VALUE));
        int c = secureRandom.nextInt(a.size() - 1);

        message.getChannel().sendMessage("変態は「"+a.get(c)+"さん」です！").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void burst(){

        message.getChannel().sendMessage("＼どっかーん！／").queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void pan(){

        //
        String[] split = new String[]{
                "https://yululi.n7mn.xyz/nana/PANmaster.png",
                "https://yululi.n7mn.xyz/nana/PanMasterNETA01COMP.png",
                "https://yululi.n7mn.xyz/nana/puyCRzt9_400x400.jpg"
        };

        List<String> list = Arrays.asList(split);
        shuffle(list);

        SecureRandom secureRandom = new SecureRandom();
        int c = secureRandom.nextInt(list.size() - 1);

        message.getChannel().sendMessage(list.get(c)).queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void msg(){

        // https://discord.com/channels/517669763556704258/543092075336433681/788207229018308678
        String[] split = text.split("/",-1);

        if (split.length != 7){
            return;
        }

        Guild guild = jda.getGuildById(split[4]);
        if (guild == null){
            message.reply("見つからないよぉ").queue();
            return;
        }

        TextChannel textChannelById = guild.getTextChannelById(split[5]);
        if (textChannelById == null){
            message.reply("見つからないよぉ").queue();
            return;
        }

        textChannelById.retrieveMessageById(split[6]).queue(message1 ->{
            String contentRaw = message1.getContentRaw();
            boolean edited = message1.isEdited();
            User author = message1.getAuthor();

            message.reply(
                    "---- メッセージの情報 ----\n" +
                            "投稿したチャンネル : "+message1.getChannel().getName()+"\n" +
                            "文字数 : " + contentRaw.length()+"\n" +
                            "編集済みかどうか : " + edited+"\n" +
                            "投稿者 : "+author.getAsTag()+"\n"
            ).queue();
        });

    }

    private void kouta(){

        String text = "金装備の Kouta1212 ちゃん　は　神\n金装備の Kouta1212 ちゃん　は　かわいい";
        message.getChannel().sendMessage(text).queue(message1 -> {
            message1.addReaction("\uD83D\uDCAF").queue();
        });

    }

    private void n3m(){

        List<Emote> emotes = guild.getEmotes();
        for (Emote emote : emotes){
            //System.out.println(emote.getName());
            if (!emote.getName().startsWith("n3m_")){
                continue;
            }

            message.getChannel().sendMessage(emote.getAsMention()+"\n:fire:").queue(message1 -> {
                message1.addReaction("\uD83D\uDCAF").queue();
            });
            break;
        }



    }

    private void role(){

        String[] split = text.split(" ", -1);

        Member member = null;
        if (split.length == 2 || split.length == 3){
            try {
                member = guild.getMemberById(split[1]);

                if (member == null){
                    for (Member m : guild.getMembers()){
                        if (m.getNickname() != null){
                            if (m.getNickname().contains(split[1])){
                                member = m;
                                break;
                            }
                        } else {
                            if (m.getUser().getAsTag().startsWith(split[1])){
                                member = m;
                                break;
                            }
                        }
                    }
                }

            } catch (Exception e){
                for (Member m : guild.getMembers()){
                    if (m.getNickname() != null){
                        if (m.getNickname().contains(split[1])){
                            member = m;
                            break;
                        }
                    } else {
                        if (m.getUser().getAsTag().startsWith(split[1])){
                            member = m;
                            break;
                        }
                    }
                }
            }

            if (member == null){
                message.reply("このDiscord鯖には存在しないユーザーらしいですよ？").queue();
                return;
            }
        }

        if (split.length == 2){
            // 確認

            StringBuffer sb = new StringBuffer();

            sb.append("----- ");
            if (member.getNickname() != null){
                sb.append(member.getNickname());
                sb.append(" (");
                sb.append(member.getUser().getAsTag());
                sb.append(")さんの情報 -----\n");
            } else {
                sb.append(member.getUser().getAsTag());
                sb.append("さんの情報 -----\n");
            }

            Date joinTime = Date.from(member.getTimeJoined().toInstant());
            boolean isBot = member.getUser().isBot();
            List<Role> roles = member.getRoles();

            sb.append("botかどうか : `");
            sb.append(isBot);
            sb.append("`\n");
            sb.append("入室日時 : `");
            sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(joinTime));
            sb.append("\n`");
            sb.append("ロール：\n");
            for (Role role : roles){
                sb.append("`");
                sb.append(role.getName());
                sb.append("`\n");
            }

            message.reply(sb.toString()).queue(message1 -> {
                message1.addReaction("✅").queue();
            });
        }

        if (split.length == 3){
            // 追加
            Member na = guild.getMemberById("781323086624456735");
            if (na != null && !na.hasPermission(Permission.MANAGE_ROLES)){
                message.reply("この鯖ではロール追加機能は使わせてもらえないです；；").queue();
                return;
            }

            Member id = guild.getMemberById(author.getId());
            if (id != null && !id.hasPermission(Permission.MANAGE_ROLES)){
                message.reply("あなたはロール追加ができないみたいです。").queue();
                return;
            }

            Role setRole;
            try {
                setRole = guild.getRoleById(split[2]);
            } catch (Exception e){
                setRole = null;
            }

            if (setRole == null){
                List<Role> roles = guild.getRoles();

                for (Role role : roles){

                    if (role.getName().equals(split[2])){
                        setRole = role;
                        break;
                    }

                }
            }

            if (setRole != null){

                boolean isRole = false;

                List<Role> roles = member.getRoles();
                for (Role role : roles){
                    if (setRole.getId().equals(role.getId())){
                        isRole = true;
                        break;
                    }
                }

                if (!isRole){
                    guild.addRoleToMember(member, setRole).queue();
                    message.reply(member.getUser().getName() + "さんをロール「"+setRole.getName()+"」に追加をしました！").queue();
                    return;
                }

                guild.removeRoleFromMember(member, setRole).queue();
                message.reply(member.getUser().getName() + "さんからロール「"+setRole.getName()+"」を削除しました！").queue();

            }

        }
    }

    private void game(){
        String text = "" +
                "----- ななみちゃんbot ゲームメニュー -----\n" +
                "`n.money` --- 現在の所持金をチェックする\n" +
                "`n.slot` --- 1回 100"+moneyList.getCurrency()+"でスロットが遊べる (当たりで最大10倍戻り)\n" +
                "~~`n.yosogame <賭け金> <数字>` --- 一つの数字を予想して当てるゲーム (当たりで10倍戻り)~~ 開発中！\n" +
                "~~`n.fx` --- ~~ 開発中！\n" +
                "`n.omikuji` --- おみくじ (結果によって"+moneyList.getCurrency()+"がもらえます)\n" +
                "(今後さらに実装予定です！)";
        message.reply(text).queue();

    }

    private void money(){

        String t = text.replaceAll("　"," ");
        String[] split = t.split(" ", -1);

        if (split.length == 1){
            Money money = moneyList.getMoney(author.getId());
            message.reply("あなたが今持っている所持金は " + money.getMoney() + " " + moneyList.getCurrency() + "ですっ！\n他の人に渡したいときは`n.money pay <相手の名前 or 相手のID> <金額>`でできますっ！").queue();
        } else if (split.length == 4){
            if (split[1].equals("pay")){
                String memberStr = split[2];
                Member targetMember = null;
                try {
                    targetMember = guild.getMemberById(memberStr);
                } catch (Exception e){
                    List<Member> members = guild.getMembers();

                    for (Member member1 : members){
                        if (member1.getNickname() != null && member1.getNickname().startsWith(memberStr)){
                            targetMember = member1;
                            break;
                        }
                        if (member1.getUser().getName().startsWith(memberStr)){
                            targetMember = member1;
                            break;
                        }
                    }
                }

                if (moneyList.getMoney(author.getId()).getMoney() - Integer.parseInt(split[3]) <= 0){
                    message.reply("所持金が足りませんよ！！").queue();
                    return;
                }

                if (Integer.parseInt(split[3]) <= 0){
                    message.reply("よくない。").queue();
                    return;
                }

                if (targetMember != null){
                    Money fromMoney = moneyList.getMoney(author.getId());
                    Money targetMoney = moneyList.getMoney(targetMember.getId());

                    moneyList.setMoney(fromMoney.getDiscordUserID(), targetMoney.getMoney() - Integer.parseInt(split[3]));
                    moneyList.setMoney(targetMoney.getDiscordUserID(), targetMoney.getMoney() + Integer.parseInt(split[3]));

                    message.reply(targetMember.getNickname() + "さんに "+split[3] + " "+moneyList.getCurrency()+"を送金しましたっ").queue();
                } else {
                    message.reply("その人 実は存在しないらしい。").queue();
                }

            }

        }

    }

    private void slot(){
        Slot slot = new Slot();
        String run = slot.run(moneyList, moneyList.getMoney(author.getId()));
        message.reply(run).queue();
    }

    private void baka(){

        List<Member> members = guild.getMembers();
        // Collections.shuffle(members);

        SecureRandom secureRandom = new SecureRandom();
        Member me = members.get(secureRandom.nextInt(members.size() - 1));

        if (me.getNickname() != null){
            message.getTextChannel().sendMessage(me.getNickname()+"さんが馬鹿に選ばれましたっ！").queue();
        } else {
            message.getTextChannel().sendMessage(me.getUser().getName()+"さんが馬鹿に選ばれましたっ！").queue();
        }
    }

    private void omikuji(){
        Omikuji omikuji = new Omikuji();
        String run = omikuji.run(moneyList, moneyList.getMoney(author.getId()));
        message.reply(run).queue();
    }

    private long getMs(String time){

        long ms = -1;

        try {
            // 秒
            if (time.toLowerCase().endsWith("s")){
                ms = Long.parseLong(time.toLowerCase().replaceAll("t:","").replaceAll("time:","").replaceAll("s","")) * 1000;
            }
            // 分
            if (time.toLowerCase().endsWith("m")){
                ms = (Long.parseLong(time.toLowerCase().replaceAll("t:","").replaceAll("time:","").replaceAll("m","")) * 60) * 1000;
            }
            // 時
            if (time.toLowerCase().endsWith("h")){
                ms = ((Long.parseLong(time.toLowerCase().replaceAll("t:","").replaceAll("time:","").replaceAll("h","")) * 60) * 60) * 1000;
            }
            // 日
            if (time.toLowerCase().endsWith("d")){
                ms = (((Long.parseLong(time.toLowerCase().replaceAll("t:","").replaceAll("time:","").replaceAll("h","")) * 60) * 60) * 24) * 1000;
            } else {
                ms = Long.parseLong(time.toLowerCase().replaceAll("t:","").replaceAll("time:","").replaceAll("s","")) * 1000;
            }

        } catch (Exception e){
            ms = -1;
        }

        return ms;

    }


    

}
