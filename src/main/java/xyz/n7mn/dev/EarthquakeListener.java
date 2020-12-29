package xyz.n7mn.dev;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import xyz.n7mn.dev.api.Earthquake;
import xyz.n7mn.dev.api.data.EarthquakeResult;
import xyz.n7mn.dev.api.data.eq.intensity.Area;
import xyz.n7mn.dev.api.data.eq.intensity.City;
import xyz.n7mn.dev.api.data.eq.intensity.Pref;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EarthquakeListener {
    private final Earthquake earthquake;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public EarthquakeListener(JDA jda, Earthquake earthquake){
        this.earthquake = earthquake;
        boolean debugMode = false;

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            private String InfoKind = "";

            @Override
            public void run() {
                long lastEventID = earthquake.getLastEventID();
                if (lastEventID != -1){
                    System.out.println("地震情報受信");
                    EarthquakeResult data = earthquake.getData();

                    Date sendTime = data.getControl().getDateTime();
                    Date nowTime = new Date();

                    System.out.println("発表日時 : " + sdf.format(sendTime));
                    System.out.println("現在日時 : " + sdf.format(nowTime));

                    if (!debugMode){
                        if (nowTime.getTime() - sendTime.getTime() > 300000){
                            System.out.println("---> 過去のものなのでスキップ");
                            return;
                        }

                        if (!InfoKind.equals("") && InfoKind.equals(data.getHead().getInfoKind())){
                            System.out.println("---> 同じモノなのでスキップ");
                            InfoKind = data.getHead().getInfoKind();
                            return;
                        }
                    }
                    InfoKind = data.getHead().getInfoKind();

                    EmbedBuilder builder = new EmbedBuilder();

                    String maxInt = data.getBody().getIntensity().getObservation().getMaxInt();
                    if (maxInt.equals("7")){
                        builder.setColor(Color.RED);
                    } else if (maxInt.equals("6+") || maxInt.equals("6-")){
                        builder.setColor(Color.PINK);
                    } else if (maxInt.equals("5+") || maxInt.equals("5-")){
                        builder.setColor(Color.RED);
                    } else if (maxInt.equals("4")){
                        builder.setColor(Color.ORANGE);
                    } else if (maxInt.equals("3")){
                        builder.setColor(Color.YELLOW);
                    } else if (maxInt.equals("2")){
                        builder.setColor(Color.GREEN);
                    } else {
                        builder.setColor(Color.BLUE);
                    }

                    builder.setTitle(InfoKind);
                    builder.addField("発表日時", sdf.format(sendTime),false);
                    if (InfoKind.equals("地震情報")){
                        builder.addField("地震発生日時", sdf.format(data.getBody().getEarthquake().getOriginTime()),false);
                    }
                    builder.addField("最大震度", maxInt,false);
                    builder.addField("気象庁コメント",  data.getHead().getHeadline() + "\n" + data.getBody().getComments().getObservation(), false);
                    if (InfoKind.equals("地震情報")){
                        builder.addField("震源地", data.getBody().getEarthquake().getHypocenter().getName() + "" +
                                " (北緯 "+data.getBody().getEarthquake().getHypocenter().getLatitude()+"度, 東経 "+data.getBody().getEarthquake().getHypocenter().getLongitude()+"度)",false);
                        builder.addField("マグニチュード", "M "+data.getBody().getEarthquake().getMagnitude(), false);

                    }

                    builder.addField("各地の震度","",false);
                    for (Pref pref : data.getBody().getIntensity().getObservation().getPref()){
                        StringBuffer sb = new StringBuffer();
                        for (Area area : pref.getArea()){
                            sb.append(area.getName());
                            sb.append(" ");
                            sb.append("最大震度 ");
                            sb.append(area.getMaxInt());
                            sb.append("\n");

                            for (City city :area.getCity()){
                                sb.append("`");
                                sb.append(city.getName());
                                sb.append(" 震度 ");
                                sb.append(city.getMaxInt());
                                sb.append("`\n");
                            }
                        }
                        builder.addField(pref.getName(), sb.toString(), true);
                    }

                    System.out.println("地震情報組み立て完了");
                    if (debugMode){
                        builder.setFooter(sdf.format(new Date()) + "に送信しました。");
                        User user = jda.getUserById("529463370089234466");
                        user.openPrivateChannel().complete().sendMessage(builder.build()).queue();
                        System.out.println("デバッグ送信完了");
                        return;
                    }

                    System.out.println("送信チャンネル取得");
                    List<Guild> guilds = jda.getGuilds();
                    for (Guild guild : guilds){
                        List<TextChannel> channels = guild.getTextChannels();
                        for (TextChannel channel : channels){
                            if (channel.getName().equals("nanami_setting")){
                                channel.getHistoryAfter(1, 100).queue(messageHistory -> {
                                    List<Message> retrievedHistory = messageHistory.getRetrievedHistory();
                                    for (Message message : retrievedHistory){
                                        String text = message.getContentRaw();
                                        String[] st = text.split(" ", -1);
                                        if (st[0].toLowerCase().startsWith("jisin")){
                                            TextChannel textChannelById = guild.getTextChannelById(st[1].replaceAll("<","").replaceAll(">",""));
                                            if (textChannelById != null){
                                                if (textChannelById.canTalk()){
                                                    builder.setFooter(sdf.format(new Date()) + "に送信しました。");
                                                    textChannelById.sendMessage(builder.build()).queue();
                                                    System.out.println("送信完了 : " + textChannelById.getName());
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                });

                            }
                        }
                    }

                }
            }
        };

        timer.scheduleAtFixedRate(task, 0L, 60000);
        System.out.println("地震情報受信準備完了...");
    }



}
