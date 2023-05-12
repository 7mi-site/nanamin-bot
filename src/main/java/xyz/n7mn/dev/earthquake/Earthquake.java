package xyz.n7mn.dev.earthquake;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;
import xyz.n7mn.dev.earthquake.eew.EEWData;
import xyz.n7mn.dev.earthquake.eew.KyoushinMonitorJson;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Earthquake {
    private final JDA jda;
    private final OkHttpClient client = new OkHttpClient();
    private final YamlMapping ConfigYml;

    public Earthquake(JDA jda){
        YamlMapping ConfigYml1 = null;
        try {
            File config = new File("./config-vote.yml");
            if (!config.exists()){
                config.createNewFile();

                YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
                ConfigYml1 = builder.add(
                        "RedisServer", "127.0.0.1"
                ).add(
                        "RedisPort", String.valueOf(Protocol.DEFAULT_PORT)
                ).add(
                        "RedisPass", ""
                ).build();

                try {
                    PrintWriter writer = new PrintWriter(config);
                    writer.print(ConfigYml1.toString());
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                ConfigYml1 = Yaml.createYamlInput(config).readYamlMapping();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConfigYml = ConfigYml1;

        this.jda = jda;

        Timer timer = new Timer();
        Timer timer2 = new Timer();
        String[] id = new String[]{""};
        TimerTask task_nhk = new TimerTask() {
            @Override
            public void run() {
                // NHKからごにょる
                //System.out.println("情報待ち...");
                try {
                    Request request = new Request.Builder()
                            .url("http://www3.nhk.or.jp/sokuho/jishin/data/JishinReport.xml")
                            .build();
                    Response response = client.newCall(request).execute();

                    Matcher matcher = Pattern.compile("<item time=\"(.*)\" shindo=\"(.*)\" url=\"(.*)\">(.*)\\</item\\>").matcher(response.body().string());
                    if (matcher.find()){
                        Request request1 = new Request.Builder()
                                .url(matcher.group(3))
                                .build();
                        Response response_xml = client.newCall(request1).execute();
                        String xml = new String(new String(response_xml.body().bytes(), "MS932").getBytes(), StandardCharsets.UTF_8);
                        Matcher matcher1 = Pattern.compile("<Timestamp>(.*)</Timestamp>").matcher(xml);
                        //System.out.println("!");
                        response_xml.close();
                        if (matcher1.find()){
                            try {
                                Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(matcher1.group(1));
                                if ((new Date().getTime() - date.getTime()) <= 60000){
                                    if (!id[0].equals(matcher1.group(1))){
                                        //System.out.println("!!");
                                        //System.out.println(xml);
                                        Matcher matcher_date = Pattern.compile("Time=\"(.*)\" Intensity").matcher(xml);
                                        Matcher matcher_intensity = Pattern.compile("Intensity=\"(\\d+|\\d+[+|\\-])\" Epicenter").matcher(xml);
                                        Matcher matcher_epicenter = Pattern.compile("Epicenter=\"(.*)\" Latitude").matcher(xml);
                                        Matcher matcher_latitude = Pattern.compile("Latitude=\"(.*)\" Longitude").matcher(xml);
                                        Matcher matcher_longitude = Pattern.compile("Longitude=\"(.*)\" Magnitude").matcher(xml);
                                        Matcher matcher_magnitude = Pattern.compile("Magnitude=\"(\\d+\\.\\d+)\" Depth").matcher(xml);
                                        Matcher matcher_depth = Pattern.compile("Depth=\"(\\d+)km\" >").matcher(xml);

                                        Matcher matcher_detail = Pattern.compile("<Detail>(.*)</Detail>").matcher(xml);
                                        Matcher matcher_local = Pattern.compile("<Local>(.*)</Local>").matcher(xml);
                                        Matcher matcher_global = Pattern.compile("<Global>(.*)</Global>").matcher(xml);

                                        final String Date;
                                        if (matcher_date.find()){
                                            Date = matcher_date.group(1);
                                        } else {
                                            Date = null;
                                        }
                                        final String Intensity;
                                        if (matcher_intensity.find()){
                                            Intensity = matcher_intensity.group(1);
                                        } else {
                                            Intensity = null;
                                        }
                                        final String Epicenter;
                                        if (matcher_epicenter.find()){
                                            Epicenter = matcher_epicenter.group(1);
                                        } else {
                                            Epicenter = null;
                                        }
                                        final String Latitude;
                                        if (matcher_latitude.find()){
                                            Latitude = matcher_latitude.group(1);
                                        } else {
                                            Latitude = null;
                                        }
                                        final String Longitude;
                                        if (matcher_longitude.find()){
                                            Longitude = matcher_longitude.group(1);
                                        } else {
                                            Longitude = null;
                                        }
                                        final String Magnitude;
                                        if (matcher_magnitude.find()){
                                            Magnitude = matcher_magnitude.group(1);
                                        } else {
                                            Magnitude = null;
                                        }
                                        final String Depth;
                                        if (matcher_depth.find()){
                                            Depth = matcher_depth.group(1) + "km";
                                        } else {
                                            Depth = null;
                                        }

                                        final String Detail;
                                        if (matcher_detail.find()){
                                            Detail = "https://www3.nhk.or.jp/sokuho/jishin/"+matcher_detail.group(1);
                                        } else {
                                            Detail = null;
                                        }
                                        final String Local;
                                        if (matcher_local.find()){
                                            Local = "https://www3.nhk.or.jp/sokuho/jishin/"+matcher_local.group(1);
                                        } else {
                                            Local = null;
                                        }
                                        final String Global;
                                        if (matcher_global.find()){
                                            Global = "https://www3.nhk.or.jp/sokuho/jishin/"+matcher_global.group(1);
                                        } else {
                                            Global = null;
                                        }

                                        new Thread(()->{

                                            jisin_send(Date, Intensity, Epicenter, Latitude, Longitude, Magnitude, Depth, Detail, Local, Global);
                                        }).start();
                                        id[0] = matcher1.group(1);
                                    }
                                }
                                if (id[0].equals("")){
                                    id[0] = matcher1.group(1);
                                }

                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }


                    }
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer2.scheduleAtFixedRate(task_nhk, 0L, 10000L);

        TimerTask task_eew = new TimerTask() {
            @Override
            public void run() {
                // 強震モニタからごにょる
                try {
                    Request request = new Request.Builder()
                            .url("http://www.kmoni.bosai.go.jp/webservice/server/pros/latest.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    KyoushinMonitorJson json = new Gson().fromJson(response.body().string(), KyoushinMonitorJson.class);
                    response.close();

                    eew(json.getLatest_time().replaceAll("/","").replaceAll(" ","").replaceAll(":",""));

                } catch (IOException e){
                    e.printStackTrace();
                }
                System.gc();
            }
        };

        timer.scheduleAtFixedRate(task_eew, 0L, 2000L);
    }

    private void eew(String Timestamp){
        try {
            Request request = new Request.Builder()
                    .url("http://www.kmoni.bosai.go.jp/webservice/hypo/eew/"+ Timestamp +".json")
                    //.url("http://www.kmoni.bosai.go.jp/webservice/hypo/eew/20230503053834.json")
                    .build();

            Response response = client.newCall(request).execute();
            EEWData data = new Gson().fromJson(response.body().string(), EEWData.class);

            if (data.getResult().getMessage().equals("データがありません")){
                response.close();
                return;
            }

            if (data.getIs_training() || !data.getAlertflg().equals("予報")){
                response.close();
                return;
            }

            System.out.println("緊急地震速報 受信");
            System.out.println(data.getReport_num() + "," + data.getRegion_name() + "," + data.getLatitude() + "," + data.getLongitude() + "," + data.getDepth() + "," + data.getMagunitude() + "," +data.getCalcintensity());
            eew_send(data.getReport_time(), data.getReport_num(), data.getIs_final(), data.getRegion_name(), data.getLatitude(), data.getLongitude(), data.getDepth(), data.getMagunitude(), data.getCalcintensity(), data.getIs_cancel());

            response.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String temp_date = "";
    private String report_Num = "";
    private void eew_send(String reportTime, String reportNum, Boolean isFinal, String regionName, String latitude, String longitude, String depth, String magunitude, String calcintensity, Boolean isCancel){
        //System.out.println("eew0");
        if (temp_date.equals(reportTime) && report_Num.equals(reportNum)){
            return;
        }
        temp_date = reportTime;
        report_Num = reportNum;

        new Thread(()->{
            //System.out.println("eew1");
            EmbedBuilder builder1 = new EmbedBuilder();
            builder1.setTitle("緊急地震情報");
            builder1.setColor(Color.ORANGE);
            if (isCancel){
                builder1.setDescription(
                        "先程の緊急地震速報はキャンセルされました。"
                );
            } else {
                builder1.setDescription("※ 緊急地震速報の仕組み上 誤差がある可能性があります。ご注意ください\n"+
                        "第 "+ reportNum + "報" + (isFinal ? "(最終報)" : "")+"\n" +
                        "震源地 : " + regionName + "("+latitude+" "+longitude+")\n" +
                        "震源の深さ : " + depth + "\n" +
                        "マグニチュード : " + magunitude+"\n" +
                        "最大予想震度 : " + calcintensity
                );
            }
            builder1.setFooter("情報元 : NIED 強震モニタ(https://www.bosai.go.jp/)");
            //System.out.println("eew2");

            new Thread(()->{

                //System.out.println("eew3");
                JedisPool pool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
                Jedis jedis = pool.getResource();
                jedis.auth(ConfigYml.string("RedisPass"));

                for (String key : jedis.keys("nanamibot:eew:*")){
                    //System.out.println("eew4");
                    String[] split = key.split(":");
                    String guildId = split[split.length - 1];
                    String channelId = jedis.get(key);

                    //System.out.println("eew5");
                    jda.getGuildById(guildId).getTextChannelById(channelId).sendMessageEmbeds(builder1.build()).queue();
                }

                jedis.close();
                pool.close();
            }).start();
        }).start();
    }

    private void jisin_send(String date, String intensity, String epicenter, String latitude, String longitude, String magnitude, String depth, String detailImage, String localImage, String globalImage){
        //System.out.println("j1");

        detailImage = detailImage.equals("https://www3.nhk.or.jp/sokuho/jishin/") ? null : detailImage;
        localImage = localImage.equals("https://www3.nhk.or.jp/sokuho/jishin/") ? null : localImage;
        globalImage = globalImage.equals("https://www3.nhk.or.jp/sokuho/jishin/") ? null : globalImage;

        EmbedBuilder builder1 = new EmbedBuilder();

        String title = "情報";
        if (intensity == null){
            title = "速報";
        }
        if (intensity != null && epicenter != null && globalImage == null){
            title = "情報 (震源に関する情報)";
        }

        builder1.setTitle("地震"+title);
        builder1.setDescription(
                "地震発生時間 : "+date + "\n" +
                (intensity == null ? "" : "最大震度 : "+intensity + "\n") +
                (epicenter != null ? "震源地 : "+epicenter+" ("+latitude + " "+ longitude +")\n" : "") +
                (magnitude != null ? "マグニチュード : " + magnitude + "\n" : "") +
                (depth != null ? "震源の深さ : " + depth : "")
        );
        builder1.setImage(detailImage);
        builder1.setFooter("情報元 : NHK地震情報 (https://www3.nhk.or.jp/sokuho/jishin/)");

        EmbedBuilder builder2 = new EmbedBuilder();
        if (globalImage != null && !globalImage.isEmpty()){
            builder2.setTitle("地震情報 (広域)");
            builder2.setImage(globalImage);
            builder2.setFooter("情報元 : NHK地震情報 (https://www3.nhk.or.jp/sokuho/jishin/)");
        }

        EmbedBuilder builder3 = new EmbedBuilder();
        if (localImage != null && !localImage.isEmpty()){
            builder3.setTitle("地震情報 (詳細)");
            builder3.setImage(localImage);
            builder3.setFooter("情報元 : NHK地震情報 (https://www3.nhk.or.jp/sokuho/jishin/)");
        }

        new Thread(()->{
            //System.out.println("j2");
            JedisPool pool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
            Jedis jedis = pool.getResource();
            jedis.auth(ConfigYml.string("RedisPass"));

            for (String key : jedis.keys("nanamibot:jisin:*")){
                String[] split = key.split(":");
                String guildId = split[split.length - 1];
                String channelId = jedis.get(key);

                if (builder2.isEmpty()){
                    jda.getGuildById(guildId).getTextChannelById(channelId).sendMessageEmbeds(builder1.build()).queue();
                    continue;
                }
                jda.getGuildById(guildId).getTextChannelById(channelId).sendMessageEmbeds(builder1.build(), builder2.build(), builder3.build()).queue();
            }

            jedis.close();
            pool.close();
        }).start();

    }
}
