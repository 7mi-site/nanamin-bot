package xyz.n7mn.dev.music;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlSequence;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;
import okhttp3.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.n7mn.dev.music.MusicBotFunction.*;

public class MusicBot {

    private final EmbedBuilder builder = new EmbedBuilder();
    private final List<MusicQueue> musicQueueList;
    private final Map<String, String> nicoVideoHeartBeatList = new HashMap<>();
    private final AudioPlayerManager playerManager;
    private AudioPlayer player;

    public MusicBot(List<MusicQueue> musicQueueList) {
        this.musicQueueList = musicQueueList;

        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        player = playerManager.createPlayer();

        new Thread(()->{
            OkHttpClient client = new OkHttpClient();

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    nicoVideoHeartBeatList.forEach((id, token) -> {
                        String[] split = token.split("::");

                        RequestBody body = RequestBody.create(split[1], MediaType.get("application/json; charset=utf-8"));
                        Request request = new Request.Builder()
                                .url("https://api.dmc.nico/api/sessions/"+split[0]+"?_format=json&_method=PUT")
                                .post(body)
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            response.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            //System.out.println("[Debug] 鯖へPost失敗 "+ sdf.format(new Date()));
                        }
                        System.gc();
                    });
                }
            };
            timer.scheduleAtFixedRate(task, 0L, 40000L);
        }).start();
    }

    public void run(SlashCommandInteractionEvent event, OptionMapping URL, OptionMapping option){
        System.gc();
        builder.setTitle("ななみちゃんbot 音楽再生機能");
        builder.setColor(Color.PINK);
        builder.clearFields();

        String VideoURL;

        if (!URL.getAsString().startsWith("stop") && !URL.getAsString().startsWith("volume") && !URL.getAsString().startsWith("nowplay") && !URL.getAsString().startsWith("skip") && !URL.getAsString().startsWith("queue") && !URL.getAsString().startsWith("http")){

            builder.setColor(Color.RED);
            builder.setTitle("ななみちゃんbot エラー");
            builder.setDescription("URLを指定してください！");

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            return;
        } else {
            VideoURL = URL.getAsString();
        }

        if (URL.getAsString().equals("stop")){
            //System.out.println("d1");
            AudioManager manager = event.getGuild().getAudioManager();

            List<MusicQueue> list = new ArrayList<>();
            for (MusicQueue q : musicQueueList){
                list.add(q);
            }

            for (int i = 0; i < list.size(); i++){
                if (list.get(i).getGuildId().equals(event.getGuild().getId())){
                    //System.out.println("d1-1 : "+ i);
                    musicQueueList.remove(list.get(i));
                }
            }
            //System.out.println("d2");
            player = playerManager.createPlayer();

            if (manager.getConnectedChannel() != null) {
                builder.setDescription("再生停止しました！");

                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                manager.closeAudioConnection();

                System.gc();
                return;
            }

            builder.setColor(Color.RED);
            builder.setTitle("ななみちゃんbot エラー");
            builder.setDescription("・・・ボイスチャンネルに入っていませんよ？\n(入っているのにこのメッセージが出る場合は管理している人に切断をお願いしてねっ！)");

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();

            System.gc();
            return;
        }

        if (URL.getAsString().equals("volume")){
            if (player.getPlayingTrack() != null){
                if (option != null){
                    player.setVolume(option.getAsInt());
                    builder.setDescription("音量を" + player.getVolume() + "に変更しました！");
                } else {
                    builder.setDescription("現在音量 : " + player.getVolume());
                }
                event.replyEmbeds(builder.build()).setEphemeral(false).queue();

                System.gc();
                return;
            }

            builder.setDescription("現在、再生していません。");

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();

            System.gc();
            return;
        }

        if (URL.getAsString().equals("skip")){
            List<MusicQueue> list = new ArrayList<>(musicQueueList);
            List<MusicQueue> list2 = new ArrayList<>();

            int i = 0;
            for (MusicQueue queue : list){
                if (queue.getGuildId().equals(event.getGuild().getId())){
                    list2.add(queue);
                }

                if (player.getPlayingTrack() != null){
                    if (queue.getAudioTrack().getInfo().uri.equals(player.getPlayingTrack().getInfo().uri)){
                        musicQueueList.remove(i);
                    }
                }

                i++;
            }

            if (player.getPlayingTrack() != null){
                for (int x = 0; x < list2.size(); x++){
                    if (list2.get(x).getAudioTrack().getInfo().uri.equals(player.getPlayingTrack().getInfo().uri)){
                        if (x + 1 < list2.size()){
                            builder.setDescription(
                                    "「"+getTitle(player.getPlayingTrack())+"」をスキップして\n" +
                                    getTitle(list2.get(x + 1).getAudioTrack()) + "を再生します！"
                            );

                            player.playTrack(list2.get(x + 1).getAudioTrack());
                            event.replyEmbeds(builder.build()).setEphemeral(false).queue();

                            list.clear();
                            list2.clear();
                            System.gc();
                            return;
                        }
                    }
                }
                player = playerManager.createPlayer();
                builder.setDescription("スキップするものがありません！");
                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            } else {
                builder.setDescription("再生していません！");
                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            }
            list.clear();
            list2.clear();
            System.gc();
            return;
        }

        if (URL.getAsString().equals("nowplay")){

            new Thread(()->{
                if (player.getPlayingTrack() != null){

                    //double par = (double) (player.getPlayingTrack().getPosition() / player.getPlayingTrack().getInfo().length);
                    //int n = (int)par * 10;

                    //String bar = "";
                    //for (int i = 0; i < n; i++){
                    //    bar = bar + "■";
                    //}
                    //for (int i = 0; i < (10 - n); i++){
                    //    bar = bar + "□";
                    //}

                    builder.setDescription(
                            "現在再生されている曲:\n" +
                                    "タイトル : "+getTitle(player.getPlayingTrack())+"\n" +
                                    "URL : " + getURL(player.getPlayingTrack())+"\n" +
                                    "再生時間 : " + getLengthStr(player.getPlayingTrack().getInfo().length) + "\n" //+
                            //"現在位置 : "+bar+"("+getLengthStr(player.getPlayingTrack().getPosition())+" / "+getLengthStr(player.getPlayingTrack().getInfo().length)+","+(par * 100)+"%)"
                    );

                } else {
                    builder.setDescription("現在再生していません！");
                }

                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                System.gc();
            }).start();

            return;
        }

        if (URL.getAsString().equals("queue")){
            new Thread(()->{
                System.gc();
                List<MusicQueue> queue = new ArrayList();
                for (MusicQueue q : musicQueueList){
                    if (q.getGuildId().equals(event.getGuild().getId())){
                        queue.add(q);
                    }
                }

                builder.setDescription("現在追加されている曲は "+queue.size()+" 件です！"+(queue.size() > 10 ? "\n再生されている曲から10件表示します！" : ""));
                for (int i = 0; i < Math.min(10, queue.size()); i++){
                    builder.addField(getTitle(queue.get(i).getAudioTrack()),"追加した人 : " + ( queue.get(i).getAddDiscordNickname() != null ? queue.get(i).getAddDiscordNickname() : queue.get(i).getAddDiscordUsername() ) + "\n音楽URL : " + getURL(queue.get(i).getAudioTrack()) + "\n時間 : " + getLengthStr(queue.get(i).getAudioTrack().getInfo().length) ,false);
                }

                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            }).start();

            return;
        }


        // ここから音楽再生
        int volume = 20;
        if (option != null){
            try {
                volume = Math.max(Math.min(option.getAsInt(), 100), 0);
            } catch (Exception e){
            }
        }

        if (event.getMember() == null || event.getMember().getVoiceState() == null || event.getMember().getVoiceState().getChannel() == null){

            builder.setColor(Color.RED);
            builder.setTitle("ななみちゃんbot エラー");
            builder.setDescription("・・・ボイスチャンネルに入っていませんね？\n(入っているのにこのメッセージが出る場合は見えないチャンネルの可能性があります。管理している人に設定をお願いしてねっ！)");

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            System.gc();
            return;
        }

        AudioChannelUnion channel = event.getMember().getVoiceState().getChannel();

        TrackScheduler trackScheduler = new TrackScheduler(player, event.getGuild(), event, musicQueueList, nicoVideoHeartBeatList);
        player.addListener(trackScheduler);

        if (player.getPlayingTrack() == null){
            player.setVolume(volume);
        }

        if (channel.getType().isAudio()){
            AudioManager manager = event.getGuild().getAudioManager();

            if (VideoURL.startsWith("http://nico.ms/") || VideoURL.startsWith("https://nico.ms/") || VideoURL.startsWith("http://nicovideo.jp/") || VideoURL.startsWith("https://nicovideo.jp/") || VideoURL.startsWith("http://www.nicovideo.jp/") || VideoURL.startsWith("https://www.nicovideo.jp/")){

                // Proxy読み込み
                List<String> ProxyList = new ArrayList<>();
                List<String> ProxyList2 = new ArrayList<>();
                File config = new File("./config.yml");
                YamlMapping ConfigYaml = null;
                try {
                    if (config.exists()){
                        ConfigYaml = Yaml.createYamlInput(config).readYamlMapping();
                    } else {

                        System.out.println("ProxyList is Empty!!!");
                        builder.setColor(Color.RED);
                        builder.setTitle("ななみちゃんbot エラー");
                        builder.setDescription("内部エラー : proxy not set");
                        event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    builder.setColor(Color.RED);
                    builder.setTitle("ななみちゃんbot エラー");
                    builder.setDescription("内部エラー : " + e.getMessage());
                    event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                    return;
                }

                YamlSequence list = ConfigYaml.yamlSequence("Proxy");
                for (int i = 0; i < list.size(); i++){
                    ProxyList.add(list.string(i));
                }
                YamlSequence list2 = ConfigYaml.yamlSequence("ProxyForOfficial");
                for (int i = 0; i < list2.size(); i++){
                    ProxyList2.add(list2.string(i));
                }

                try {
                    String id = VideoURL.replaceAll("http://nicovideo.jp/watch/","").replaceAll("https://nicovideo.jp/watch/","").replaceAll("http://www.nicovideo.jp/watch/","").replaceAll("https://www.nicovideo.jp/watch/","").replaceAll("http://nico.ms/","").replaceAll("https://nico.ms/","");
                    id = id.split("\\?")[0];

                    OkHttpClient.Builder builder1 = new OkHttpClient.Builder();
                    String[] split = id.startsWith("so") ? ProxyList2.get(new SecureRandom().nextInt(ProxyList2.size())).split(":") : ProxyList.get(new SecureRandom().nextInt(ProxyList.size())).split(":");
                    String ProxyIP = split[0];
                    int ProxyPort = Integer.parseInt(split[1]);
                    OkHttpClient client = builder1.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyIP, ProxyPort))).build();

                    System.gc();
                    String resUrl = null;

                    final String HtmlText;
                    Request request1;
                    if (!id.startsWith("so")){
                        request1 = new Request.Builder()
                                .url("https://nico.ms/"+id)
                                .build();
                    } else {
                        request1 = new Request.Builder()
                                .url("https://www.nicovideo.jp/watch/"+id)
                                .build();

                    }

                    Response response1 = client.newCall(request1).execute();
                    HtmlText = response1.body().string();

                    Matcher matcher = Pattern.compile("<meta property=\"video:duration\" content=\"(\\d+)\">").matcher(HtmlText);
                    if (!matcher.find()){
                        throw new Exception("動画が存在しないか 再生できません。");
                    }

                    String SessionId = null;
                    String Token = null;
                    String Signature = null;


                    Matcher matcher1   = Pattern.compile("player_id\\\\&quot;:\\\\&quot;nicovideo-(.*)\\\\&quot;,\\\\&quot;recipe_id").matcher(HtmlText);
                    if (matcher1.find()){
                        SessionId = matcher1.group(1);
                        //System.out.println("[Debug] セッションID : "+SessionId+" "+sdf.format(new Date()));
                    }
                    Matcher matcher2 = Pattern.compile("\\{\\\\&quot;service_id\\\\&quot;:\\\\&quot;nicovideo\\\\&quot;(.*)\\\\&quot;transfer_presets\\\\&quot;:\\[\\]\\}").matcher(HtmlText);
                    if (matcher2.find()){
                        Token = matcher2.group().replaceAll("\\\\","").replaceAll("&quot;","\"").replaceAll("\"","\\\\\"");
                        //System.out.println("[Debug] TokenData : \n"+Token+"\n"+ sdf.format(new Date()));
                    }
                    Matcher matcher3 = Pattern.compile("signature&quot;:&quot;(.*)&quot;,&quot;contentId").matcher(HtmlText);
                    if (matcher3.find()){
                        Signature = matcher3.group(1);
                        //System.out.println("[Debug] signature : "+Signature+" "+ sdf.format(new Date()));
                    }

                    if (SessionId == null || Token == null || Signature == null){
                        throw new Exception("動画情報 取得失敗 (アクセスエラー)");
                    }

                    Matcher matcher4 = Pattern.compile("archive_h264_600kbps_360p").matcher(HtmlText);
                    Matcher matcher5 = Pattern.compile("archive_h264_300kbps_360p").matcher(HtmlText);
                    Matcher matcher_http = Pattern.compile("&quot;http&quot;").matcher(HtmlText);

                    String json = "{\"session\":{\"recipe_id\":\"nicovideo-"+id+"\",\"content_id\":\"out1\",\"content_type\":\"movie\",\"content_src_id_sets\":[{\"content_src_ids\":[{\"src_id_to_mux\":{\"video_src_ids\":["+(matcher5.find() ? "\"archive_h264_300kbps_360p\"" : (matcher4.find() ? "\"archive_h264_600kbps_360p\",\"archive_h264_300kbps_360p\"" : "\"archive_h264_360p\",\"archive_h264_360p_low\""))+"],\"audio_src_ids\":[\"archive_aac_64kbps\"]}}]}],\"timing_constraint\":\"unlimited\",\"keep_method\":{\"heartbeat\":{\"lifetime\":120000}},\"protocol\":{\"name\":\"http\",\"parameters\":{\"http_parameters\":{\"parameters\":{\"http_output_download_parameters\":{\"use_well_known_port\":\"yes\",\"use_ssl\":\"yes\",\"transfer_preset\":\"\"}}}}},\"content_uri\":\"\",\"session_operation_auth\":{\"session_operation_auth_by_signature\":{\"token\":\""+Token+"\",\"signature\":\""+Signature+"\"}},\"content_auth\":{\"auth_type\":\"ht2\",\"content_key_timeout\":600000,\"service_id\":\"nicovideo\",\"service_user_id\":\""+SessionId+"\"},\"client_info\":{\"player_id\":\"nicovideo-"+SessionId+"\"},\"priority\":0"+(id.startsWith("so") ? ".2" : "")+"}}";
                    if (!matcher_http.find()){
                        Matcher matcher_hls1 = Pattern.compile("hls_encrypted_key\\\\&quot;:\\\\&quot;(.*)\\\\&quot;}&quot;,&quot;signature").matcher(HtmlText);
                        Matcher matcher_hls2 = Pattern.compile("&quot;keyUri&quot;:&quot;(.*)&quot;},&quot;movie").matcher(HtmlText);
                        Matcher matcher_hls3 = Pattern.compile(",&quot;token&quot;:&quot;(.*)&quot;,&quot;signature&quot;:&quot;").matcher(HtmlText);

                        String hls_encrypted_key = "";
                        if (matcher_hls1.find()){
                            hls_encrypted_key = matcher_hls1.group(1).replaceAll("\\\\","");
                        }
                        String keyUri = "";
                        if (matcher_hls2.find()){
                            keyUri = matcher_hls2.group(1).replaceAll("\\\\","").replaceAll("&amp;","&");
                        }

                        if (matcher_hls3.find()){
                            Token = matcher_hls3.group(1).replaceAll("&quot;","\"");
                        }

                        //System.out.println(hls_encrypted_key);
                        //System.out.println(keyUri);
                        //System.out.println(SessionId);

                        json = "{\"session\":{\"recipe_id\":\"nicovideo-"+id+"\",\"content_id\":\"out1\",\"content_type\":\"movie\",\"content_src_id_sets\":[{\"content_src_ids\":[{\"src_id_to_mux\":{\"video_src_ids\":[\"archive_h264_360p\",\"archive_h264_360p_low\"],\"audio_src_ids\":[\"archive_aac_64kbps\"]}},{\"src_id_to_mux\":{\"video_src_ids\":[\"archive_h264_360p_low\"],\"audio_src_ids\":[\"archive_aac_64kbps\"]}}]}],\"timing_constraint\":\"unlimited\",\"keep_method\":{\"heartbeat\":{\"lifetime\":120000}},\"protocol\":{\"name\":\"http\",\"parameters\":{\"http_parameters\":{\"parameters\":{\"hls_parameters\":{\"use_well_known_port\":\"yes\",\"use_ssl\":\"yes\",\"transfer_preset\":\"\",\"segment_duration\":6000,\"encryption\":{\"hls_encryption_v1\":{\"encrypted_key\":\""+hls_encrypted_key+"\",\"key_uri\":\""+keyUri+"\"}}}}}}},\"content_uri\":\"\",\"session_operation_auth\":{\"session_operation_auth_by_signature\":{\"token\":\""+Token+"\",\"signature\":\""+Signature+"\"}},\"content_auth\":{\"auth_type\":\"ht2\",\"content_key_timeout\":600000,\"service_id\":\"nicovideo\",\"service_user_id\":\""+SessionId+"\"},\"client_info\":{\"player_id\":\"nicovideo-"+SessionId+"\"},\"priority\":0.2}}";
                    }

                    String ResponseJson;
                    RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
                    Request request2 = new Request.Builder()
                            .url("https://api.dmc.nico/api/sessions?_format=json")
                            .post(body)
                            .build();

                    Response response2 = client.newCall(request2).execute();
                    ResponseJson = response2.body().string();
                    //System.out.println(response2.body().string());

                    Matcher video_matcher = Pattern.compile("\"content_uri\":\"(.*)\",\"session_operation_auth").matcher(ResponseJson);
                    VideoURL = null;
                    if (video_matcher.find()){
                        VideoURL = video_matcher.group(1).replaceAll("\\\\","");
                        //System.out.println("[Debug] 動画URL : "+VideoURL+" "+sdf.format(new Date()));
                    }

                    String HeartBeatSession = null;
                    String HeartBeatSessionId = null;
                    Matcher heart_session_matcher = Pattern.compile("\\{\"meta\":\\{\"status\":201,\"message\":\"created\"},\"data\":\\{(.*)\\}").matcher(ResponseJson);
                    if (heart_session_matcher.find()){
                        HeartBeatSession = "{"+heart_session_matcher.group(1); //.replaceAll("\\\\","");
                        //System.out.println("[Debug] ハートビート信号用 セッション : \n"+HeartBeatSession+"\n"+sdf.format(new Date()));
                    }

                    Matcher heart_session_matcher2 = Pattern.compile("\"data\":\\{\"session\":\\{\"id\":\"(.*)\",\"recipe_id\"").matcher(ResponseJson);
                    if (heart_session_matcher2.find()){
                        HeartBeatSessionId = heart_session_matcher2.group(1).replaceAll("\\\\","");
                        //System.out.println("[Debug] ハートビート信号用 セッションID : \n"+HeartBeatSessionId+"\n"+sdf.format(new Date()));
                    }

                    if (VideoURL == null || HeartBeatSession == null || HeartBeatSessionId == null){
                        //System.out.println("[Debug] 動画情報 取得失敗 "+ sdf.format(new Date()));
                        throw new Exception("動画URL取得失敗 (nicovideo apiエラー)");
                    }
                    //System.out.println("[Debug] 動画情報 取得成功\n動画URL : "+VideoURL+" \n"+ sdf.format(new Date()));
                    nicoVideoHeartBeatList.put(VideoURL, HeartBeatSessionId+"::"+HeartBeatSession);
                    System.gc();


                } catch (Exception e) {
                    e.printStackTrace();
                    builder.setColor(Color.RED);
                    builder.setTitle("ななみちゃんbot エラー");
                    builder.setDescription("内部エラー : " + e.getMessage());
                    event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                    return;
                }

            }

            manager.openAudioConnection(channel.asVoiceChannel());
            manager.setSendingHandler(new AudioPlayerSendHandler(player));

            playerManager.loadItem(VideoURL, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    trackScheduler.play(track);
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("ななみちゃんbot 音楽再生機能");
                    builder.setColor(Color.PINK);

                    //System.out.println("URL : " + track.getIdentifier());
                    builder.setDescription(getTitle(track) + "を追加しました！\nURL : "+getURL(track));
                    event.replyEmbeds(builder.build()).setEphemeral(false).queue();
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("ななみちゃんbot 音楽再生機能");
                    builder.setColor(Color.PINK);
                    builder.setDescription(playlist.getName() + "を追加しました！\n("+playlist.getTracks().size()+"曲)");
                    event.replyEmbeds(builder.build()).setEphemeral(false).queue();

                    for (AudioTrack track : playlist.getTracks()) {
                        trackScheduler.play(track);
                    }
                }

                @Override
                public void noMatches() {
                    // Notify the user that we've got nothing
                }

                @Override
                public void loadFailed(FriendlyException throwable) {
                    // Notify the user that everything exploded
                }
            });
        }

        System.gc();
    }

}
