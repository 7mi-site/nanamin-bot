package xyz.n7mn.dev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import xyz.n7mn.dev.api.Earthquake;
import xyz.n7mn.dev.api.data.EarthquakeResult;
import xyz.n7mn.dev.api.data.eq.intensity.Area;
import xyz.n7mn.dev.api.data.eq.intensity.Pref;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.*;

class EventListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User author = event.getAuthor();
        String text = event.getMessage().getContentRaw();

        if (event.getChannelType() == ChannelType.PRIVATE && !event.getAuthor().getId().equals("781323086624456735")){
            event.getMessage().getPrivateChannel().sendMessage("ふぬ？\n\nhttps://discord.com/api/oauth2/authorize?client_id=742696480854245387&permissions=8&scope=bot").queue();
            return;
        }

        if (author.isBot()){
            return;
        }

        if (text.toLowerCase().startsWith("n.")){
            System.out.println("---- Debug ----\n" + author.getAsTag() + "\n" + text + "\n----- Debug -----");
            try {

                RestAction<User> nanami = event.getJDA().retrieveUserById("529463370089234466");
                PrivateChannel dm = nanami.complete().openPrivateChannel().complete();

                String debug = "----- Debug ----- \n" +
                        "サーバ名: " + event.getGuild().getName() +"\n" +
                        "発言チャンネル名: " + event.getMessage().getTextChannel().getName() + "\n" +
                        "発言者: " + author.getAsTag() + "\n" +
                        "発言内容：`"+text+"`";
                dm.sendMessage(debug).queue();


            } catch (Exception e){

                e.printStackTrace();

            }


        }

        if (text.toLowerCase().startsWith("n.help")){

            String helpText = "----- ななみちゃんbot ヘルプ Start -----\n" +
                    "**※このメッセージは送信専用です。返信しても何もできませんのでご注意ください。**\n" +
                    "`n.vote`、`n.voteNt` -- アンケートを表示する(詳細はn.voteと打って詳細ヘルプを出してください)\n" +
                    "`n.ping` -- 応答を返す\n"+
                    "`n.nullpo` または `n.ぬるぽ` -- ガッ\n"+
                    "`n.dice` -- さいころを振る\n"+
                    "`n.random <文字列1> <文字列2> <...> <文字列n>` -- 指定された文字列の中から一つを表示する\n\n" +
                    "--- 地震情報機能について ---\n" +
                    "地震情報機能は「nanami_setting」という名前でチャンネルを作成し\n" +
                    "「jisin: <情報を流したいテキストチャンネルのID>」とメッセージを送ってください。\n" +
                    "----- ななみちゃんbot ヘルプ End ----";

            PrivateChannel sendUserDM = author.openPrivateChannel().complete();
            sendUserDM.sendMessage(helpText).queue();

            event.getMessage().reply("ななみちゃんbotのヘルプをDMにお送りいたしましたっ！").queue();
            return;

        }

        if (text.toLowerCase().equals("n.dice")){

            int i = new Random().nextInt(5) + 1;
            event.getMessage().getTextChannel().sendMessage("さいころの結果は" + i + "です。").queue();
            return;

        }

        if (text.toLowerCase().equals("n.ping")){

            OffsetDateTime idLong = event.getMessage().getTimeCreated();

            Date date = Date.from(idLong.toInstant());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            event.getMessage().getTextChannel().sendMessage("応答したよっ\n(送信元メッセージの日時：" + sdf.format(date) + " (JST))").queue();
            return;

        }

        if (text.toLowerCase().equals("n.nullpo") || text.toLowerCase().equals("n.ぬるぽ")){

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

            event.getMessage().getTextChannel().sendMessage(msg[new Random().nextInt(msg.length)]).queue();
            return;

        }

        if (text.toLowerCase().startsWith("n.random")){

            String[] split = text.split(" ", -1);
            event.getMessage().getTextChannel().sendMessage("選ばれたのは「" + split[new Random().nextInt(split.length - 1) + 1]+"」だよっ！").queue();

            return;
        }

        if (text.toLowerCase().startsWith("n.send")){

            event.getMessage().delete().queue();

            String[] split = text.split(" ", -1);

            if (split.length == 4){

                try {

                    RestAction<User> user = event.getJDA().retrieveUserById(split[1]);
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

        // 投票はn7mn-VoteBotがいたら動かさない
        TextChannel textChannel = event.getTextChannel();
        List<Member> members = textChannel.getMembers();
        for (Member member : members){

            if (member.getUser().getId().equals("781130665906274317")){
                return;
            }

        }

        if (text.toLowerCase().equals("n.vote")){

            Message message = event.getMessage();
            message.delete().queue();

            String msg = message.getAuthor().getAsTag()+"さんっ！\n" +
                    "n.voteのヘルプですっ！\n" +
                    "コマンドの書き方は\n" +
                    "`n.vote <タイトル> <選択肢1> <選択肢2> <選択肢3> <...> <選択肢19>` または\n" +
                    "`n.vote\n<タイトル>\n<選択肢1>\n<選択肢2>\n<選択肢3>\n<...>\n<選択肢19>`\nですっ！\n" +
                    "タイトルが必要じゃない場合はn.voteNtとつけて同じようにしてくださいっ！！";

            event.getMessage().getTextChannel().sendMessage(msg).queue();


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

            if ((string.length - 2) >= regional.length){
                event.getMessage().reply("えらーですっ！選択肢が多すぎます！！").queue();
                return;
            }

            event.getMessage().delete().queue();

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
            sb.append(event.getAuthor().getName());
            sb.append(" さんが投票を開始しました)");

            event.getChannel().sendMessage(sb.toString()).queue(message -> {

                for (int i = 1; i < string.length; i++){
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

            if ((string.length - 1) >= regional.length){
                event.getMessage().reply("えらーですっ！選択肢が多すぎます！！").queue();
                return;
            }

            event.getMessage().delete().queue();

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
            sb.append(event.getAuthor().getName());
            sb.append(" さんが投票を開始しました)");

            event.getChannel().sendMessage(sb.toString()).queue(message -> {

                for (int i = 0; i < (string.length - 1); i++){
                    message.addReaction(regional[i]).queue();
                }

            });
        }

    }

    @Override
    public void onReady(ReadyEvent event) {

        JDA jda = event.getJDA();
        Earthquake earthquake = new Earthquake();

        Timer timer = new Timer();
        final List<TextChannel>[] textChannels = new List[]{jda.getTextChannels()};
        List<TextChannel> EarthChannel = new ArrayList<>();
        final long[] chCount = {-1};
        final long[] lastId = {-1};

        TimerTask task = new TimerTask() {
            public void run() {

                if (chCount[0] != jda.getTextChannels().size()){

                    EarthChannel.clear();
                    textChannels[0] = jda.getTextChannels();
                    chCount[0] =textChannels[0].size();

                    for (TextChannel channel : textChannels[0]){
                        if (channel.getName().equals("nanami_setting")){

                            channel.getHistoryAfter(1, 10).queue((messageHistory -> {
                                List<Message> retrievedHistory = messageHistory.getRetrievedHistory();

                                for (Message message : retrievedHistory){

                                    String text = message.getContentRaw();
                                    if (text.startsWith("jisin: ")){

                                        String s = text.replaceAll("jisin: ", "");
                                        EarthChannel.add(jda.getTextChannelById(s));

                                    }

                                }

                            }));

                        }
                    }
                }

                if (earthquake.getLastEventID() != -1){

                    EarthquakeResult data = earthquake.getData();

                    if (new Date().getTime() - data.getHead().getReportDateTime().getTime() > 300000){
                        return;
                    }

                    if (data.getHead().getEventID() == lastId[0]){
                        return;
                    }

                    lastId[0] = data.getHead().getEventID();

                    for (TextChannel ch : EarthChannel){

                        StringBuffer sb = new StringBuffer();
                        sb.append("------ 地震情報 ------\n");
                        sb.append(data.getHead().getHeadline());
                        sb.append("\n");
                        sb.append("震源地は");
                        sb.append(data.getBody().getEarthquake().getHypocenter().getName());
                        sb.append("(");
                        sb.append(data.getBody().getEarthquake().getHypocenter().getLongitude());
                        sb.append(",");
                        sb.append(data.getBody().getEarthquake().getHypocenter().getLatitude());
                        sb.append(")\n");
                        sb.append("マグニチュードは M ");
                        sb.append(data.getBody().getEarthquake().getMagnitude());
                        sb.append("と推定されています。\n");
                        sb.append(data.getBody().getComments().getObservation());
                        sb.append("\n");
                        sb.append("最大震度は ");
                        sb.append(data.getBody().getIntensity().getObservation().getMaxInt());
                        sb.append(" です。\n");

                        sb.append("---- 各地の震度 --- \n");
                        Pref[] prefList = data.getBody().getIntensity().getObservation().getPref();
                        for (Pref perf : prefList){
                            Area[] areaList = perf.getArea();

                            for (Area area : areaList){

                                sb.append(area.getName());
                                sb.append(" 震度 ");
                                sb.append(area.getMaxInt());
                                sb.append("\n");

                            }

                        }


                        ch.sendMessage(sb.toString()).queue();

                    }

                }


            }
        };

        timer.scheduleAtFixedRate(task, 0, (1000 * 60));

    }
}
