package xyz.n7mn.dev.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicBotFunction {


    public static String getTitle(AudioTrack track){
        Matcher matcher1 = Pattern.compile("(.*)nicovideo(.*)").matcher(track.getInfo().uri);
        if (matcher1.find()) {
            Matcher matcher2 = Pattern.compile("nicovideo-([a-z]{2}\\d+)_").matcher(track.getInfo().uri);

            String NicoId = null;
            if (matcher2.find()){
                NicoId = matcher2.group(1);
            }
            if (NicoId == null) {
                return track.getInfo().title;
            } else {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://www.nicovideo.jp/watch/"+NicoId)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String HtmlText = response.body() != null ? response.body().string() : "";
                    Matcher matcher = Pattern.compile("<meta property=\"og:title\" content=\"(.*)\">").matcher(HtmlText);
                    response.close();
                    return matcher.find() ? matcher.group(1) : "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return track.getInfo().title;
    }

    public static String getURL(AudioTrack track){
        Matcher matcher1 = Pattern.compile("(.*)nicovideo(.*)").matcher(track.getInfo().uri);
        if (matcher1.find()) {
            Matcher matcher2 = Pattern.compile("nicovideo-([a-z]{2}\\d+)_").matcher(track.getInfo().uri);
            String NicoId = null;
            if (matcher2.find()){
                NicoId = matcher2.group(1);
            }
            if (NicoId == null) {
                return track.getInfo().title;
            } else {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://www.nicovideo.jp/watch/"+NicoId)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String HtmlText = response.body() != null ? response.body().string() : "";
                    Matcher matcher = Pattern.compile("<meta property=\"video:duration\" content=\"(\\d+)\">").matcher(HtmlText);
                    if (!matcher.find()){
                        return null;
                    }
                    response.close();
                    return "https://www.nicovideo.jp/watch/"+NicoId;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return track.getInfo().uri;
    }

    public static String getLengthStr(long length){

        long length1 = length / 1000;
        String str;

        long hour = -1;
        long minute = -1;
        long sec = 0;


        if (length1 >= 3600){
            hour = length1 / 3600;
            minute = (length1 - (hour * 3600L)) / 60L;
            sec = length1 - (hour * 3600L) - (minute * 60);
        }

        if (length1 >= 60){
            minute = length1 / 60L;
            sec = length1 - (minute * 60);
        }

        if (length >= 1000){
            str = (hour >= 0 ? hour + ":" : "") + (minute >= 0 ? minute + ":" : "") + sec;
        } else {
            str = "0."+length;
        }
        return str;

    }


}
