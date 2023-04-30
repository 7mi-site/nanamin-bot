package xyz.n7mn.dev.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.n7mn.dev.music.NicoVideoInfo;

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
                        .url("https://ext.nicovideo.jp/api/getthumbinfo/"+NicoId)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    NicoVideoInfo videoInfo = NicoVideoInfo.newInstance(response.body().string());
                    response.close();
                    return videoInfo.getTitle();
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
                        .url("https://ext.nicovideo.jp/api/getthumbinfo/"+NicoId)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    NicoVideoInfo videoInfo = NicoVideoInfo.newInstance(response.body().string());
                    response.close();
                    return videoInfo.getVideoId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return track.getInfo().uri;
    }
}
