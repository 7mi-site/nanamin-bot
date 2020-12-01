package xyz.n7mn.dev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.n7mn.dev.api.Earthquake;
import xyz.n7mn.dev.api.data.EarthquakeResult;
import xyz.n7mn.dev.api.data.eq.intensity.Area;
import xyz.n7mn.dev.api.data.eq.intensity.Pref;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.*;

class EventListener extends ListenerAdapter {

    private final Earthquake earthquake;

    public EventListener(Earthquake earthquake){
        this.earthquake = earthquake;
    }

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
                    "`n.vote <タイトル> <選択肢1> <選択肢2> <選択肢3> <...> <選択肢20>` または\n" +
                    "`n.vote\n<タイトル>\n<選択肢1>\n<選択肢2>\n<選択肢3>\n<...>\n<選択肢20>`\nですっ！\n" +
                    "タイトルが必要じゃない場合はn.voteNtとつけて同じようにしてくださいっ！！";

            event.getMessage().getTextChannel().sendMessage(msg).queue();


        }

        if (text.toLowerCase().startsWith("n.vote") && !text.startsWith("n.voteNt")){

            boolean matches = text.matches(" ");

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

        TimerTask task = new TimerTask() {
            public void run() {

                if (chCount[0] != jda.getTextChannels().size()){
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

                    for (TextChannel ch : EarthChannel){

                        StringBuffer sb = new StringBuffer();
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
