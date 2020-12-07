package xyz.n7mn.dev;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.api.Earthquake;
import xyz.n7mn.dev.api.data.EarthquakeResult;
import xyz.n7mn.dev.api.data.eq.intensity.Area;
import xyz.n7mn.dev.api.data.eq.intensity.Pref;
import xyz.n7mn.dev.music.GuildMusicManager;
import xyz.n7mn.dev.music.PlayerManager;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.List;

class EventListener extends ListenerAdapter {

    private VoiceChannel voiceChannel;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User author = event.getAuthor();
        String text = event.getMessage().getContentRaw();

        if (event.getChannel().getType() == ChannelType.PRIVATE && event.getAuthor().getId().equals("529463370089234466")){

            if (text.equals("n.check")){

                try {
                    JDA jda = event.getJDA();
                    List<Guild> guilds = jda.getGuilds();

                    StringBuffer sb = new StringBuffer();
                    sb.append("---- ななみちゃんbotが入っているサーバーは以下のとおりです。 ----\n");
                    for (Guild guild : guilds){

                        sb.append(guild.getId());
                        sb.append(" : ");
                        sb.append(guild.getName());

                    }

                    event.getChannel().sendMessage(sb.toString()).queue();
                } catch (Exception e){
                    // e.printStackTrace();
                }
                return;
            }

        }

        if (event.getChannel().getType() == ChannelType.PRIVATE && !event.getAuthor().getId().equals("781323086624456735") && !event.getAuthor().getId().equals("785322639295905792")){
            event.getMessage().getPrivateChannel().sendMessage("ふぬ？なにもおきないですよ？\n" +
                    "\n" +
                    "このbotを入れるには：https://discord.com/api/oauth2/authorize?client_id=781323086624456735&permissions=8&scope=bot\n" +
                    "botについてバグ報告、テスト、要望が出したい！： https://discord.gg/QP2hRSQaVV").queue((message -> {
                        message.suppressEmbeds(true).queue();
            }));

            RestAction<User> nanami = event.getJDA().retrieveUserById("529463370089234466");
            PrivateChannel dm = nanami.complete().openPrivateChannel().complete();
            String debug = "----- Debug ----- \n" +
                    "発言者: " + author.getAsTag() + "\n" +
                    "発言内容：`"+text+"`";
            dm.sendMessage(debug).queue();
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

        new ChatMessage(event.getAuthor(), event.getMessage()).run();

    }

    @Override
    public void onReady(ReadyEvent event) {

        JDA jda = event.getJDA();
        List<Guild> guilds = jda.getGuilds();
        System.out.println("現在 " + guilds.size() + "サーバーで動いてるらしい。");
        int i = 0;
        for (Guild guild : guilds){

            System.out.println(guild.getId() + " : " + guild.getName());

        }

        Earthquake earthquake = new Earthquake();

        long[] lastId = new long[]{-1};

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                EarthquakeResult data1 = earthquake.getData();
                for (Guild guild : jda.getGuilds()){

                    List<TextChannel> textChannels = guild.getTextChannels();
                    for (TextChannel ch : textChannels){

                        if (ch.getName().equals("nanami_setting")){

                            MessageHistory.MessageRetrieveAction after = ch.getHistoryAfter(1, 100);
                            after.queue((messageHistory -> {

                                List<Message> retrievedHistory = messageHistory.getRetrievedHistory();
                                for (Message message : retrievedHistory){
                                    if (message.getContentRaw().toLowerCase().startsWith("jisin: ")){

                                        String[] split = message.getContentRaw().split(" ");

                                        TextChannel channel = guild.getTextChannelById(split[1]);
                                        if (channel != null){

                                            // channel.sendMessage("テスト送信です。 削除お願いします。").queue();
                                            if (earthquake.getLastEventID() != -1){

                                                System.out.println("Debug : 地震情報取得");
                                                EarthquakeResult data = earthquake.getData();
                                                if (new Date().getTime() - data.getHead().getReportDateTime().getTime() > 300000){
                                                    System.out.println("Debug : 時間差 " + (new Date().getTime() - data.getHead().getReportDateTime().getTime()));
                                                    continue;
                                                }

                                                if (data.getHead().getEventID() == lastId[0]){
                                                    System.out.println("Debug : 前と同じ");
                                                    continue;
                                                }

                                                System.out.println("地震情報を送信しました : " + channel.getName());
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

                                                channel.sendMessage(sb.toString()).queue();
                                                System.out.println("Debug : send");

                                            }

                                        }

                                    }
                                }

                            }));
                        }
                    }
                }
                if (data1 != null){
                    lastId[0] = data1.getHead().getEventID();
                }

            }
        };

        timer.scheduleAtFixedRate(task, 0, (1000 * 60));

    }
}

/*
if (earthquake.getLastEventID() != -1){
                                                        EarthquakeResult data = earthquake.getData();

                                                        textChannelById.sendMessage("テスト送信です。削除お願いします。 by 茅野ななみ#2669").queue();

                                                        if (new Date().getTime() - data.getHead().getReportDateTime().getTime() > 300000){
                                                            break;
                                                        }
                                                        if (data.getHead().getEventID() == lastId[0]){
                                                            break;
                                                        }

                                                        lastId[0] = data.getHead().getEventID();

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

                                                        textChannelById.sendMessage(sb.toString()).queue();
                                                    }
 */