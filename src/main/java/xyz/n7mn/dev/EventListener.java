package xyz.n7mn.dev;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import xyz.n7mn.dev.api.Earthquake;
import xyz.n7mn.dev.api.data.EarthquakeResult;
import xyz.n7mn.dev.api.data.eq.intensity.Area;
import xyz.n7mn.dev.api.data.eq.intensity.Pref;

import java.util.*;
import java.util.List;

class EventListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getChannel().getType() == ChannelType.PRIVATE){
            new DMMessage(event.getMessage(), event.getAuthor()).run();
            return;
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

        final String[] sokuhoText = {"",""};

        TimerTask task = new TimerTask() {
            public void run() {
                EarthquakeResult data1 = earthquake.getData();

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://192.168.0.132:8899/kisyotyo_html.php")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (sokuhoText[0].length() == 0){
                        sokuhoText[0] = response.body().string();
                        sokuhoText[1] = response.body().string();
                    }else{
                        sokuhoText[0] = response.body().string();
                    }

                } catch (Exception e){
                    // e.printStackTrace();
                }

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
                                            if (!sokuhoText[1].equals(sokuhoText[0])){
                                                channel.sendMessage("----- 気象庁速報 (ここから) -----\n" + sokuhoText[0] + "\n----- 気象庁速報 (ここまで) -----").queue();
                                                System.out.println("----- 気象庁速報 (ここから) -----\n" + sokuhoText[0] + "\n----- 気象庁速報 (ここまで) -----");
                                                //System.out.println("debug 0\n" + sokuhoText[0]);
                                                //System.out.println("debug 1 \n" + sokuhoText[1]);
                                            }

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
                                                sb.append("------ 地震情報 (ここから) ------\n");
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

                                                sb.append("------ 地震情報 (ここまで) ------");

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

                if (!sokuhoText[1].equals(sokuhoText[0])){
                    sokuhoText[1] = sokuhoText[0];
                }

            }
        };

        timer.scheduleAtFixedRate(task, 0, (1000 * 60));

    }
}
