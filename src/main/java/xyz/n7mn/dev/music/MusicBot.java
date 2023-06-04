package xyz.n7mn.dev.music;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlSequence;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;
import okhttp3.*;
import xyz.n7mn.nico_proxy.BilibiliCom;
import xyz.n7mn.nico_proxy.BilibiliTv;
import xyz.n7mn.nico_proxy.NicoNicoVideo;
import xyz.n7mn.nico_proxy.ShareService;

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
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    nicoVideoHeartBeatList.forEach((id, token) -> {
                        String[] split = token.split("::");
                        new NicoNicoVideo().SendHeartBeatVideo(split[0], split[1], null);
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

        final String InputURL = VideoURL;
        if (channel.getType().isAudio()){
            AudioManager manager = event.getGuild().getAudioManager();

            Matcher nico_video = Pattern.compile("(nico\\.ms|nicovideo\\.jp)").matcher(VideoURL);
            Matcher nico_live = Pattern.compile("(nico\\.ms/lv|live\\.nicovideo\\.jp|live\\.sp\\.nicovideo\\.jp)").matcher(VideoURL);
            Matcher bili_com = Pattern.compile("bilibili.com").matcher(VideoURL);
            Matcher bili_tv = Pattern.compile("bilibili.tv").matcher(VideoURL);

            try {
                if (nico_live.find()){
                    VideoURL = new NicoNicoVideo().getLive(VideoURL, null);
                } else if (nico_video.find()) {
                    NicoNicoVideo nicoVideo = new NicoNicoVideo();
                    String[] video = nicoVideo.getVideo(VideoURL, null, false);
                    VideoURL = video[0];

                    nicoVideo.SendHeartBeatVideo(video[1], video[2], null);
                    nicoVideoHeartBeatList.put(VideoURL, video[1]+"::"+video[2]);
                } else if (bili_com.find()) {
                    VideoURL = new BilibiliCom().getVideo(VideoURL, null);
                } else if (bili_tv.find()){
                    VideoURL = new BilibiliTv().getVideo(VideoURL, null);
                }
            } catch (Exception e) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("ななみちゃんbot 音楽再生機能");
                builder.setColor(Color.RED);

                builder.setDescription("内部エラーです。\nエラーメッセージ : " + e.getMessage());
                event.replyEmbeds(builder.build()).setEphemeral(false).queue();

                return;
            }


            manager.openAudioConnection(channel.asVoiceChannel());
            manager.setSendingHandler(new AudioPlayerSendHandler(player));

            //System.out.println(VideoURL);

            playerManager.loadItem(VideoURL, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    trackScheduler.play(track);
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("ななみちゃんbot 音楽再生機能");
                    builder.setColor(Color.PINK);

                    //System.out.println("URL : " + track.getIdentifier());
                    builder.setDescription(getTitle(track) + "を追加しました！\nURL : "+InputURL);
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
                    throwable.printStackTrace();
                }
            });
        }

        System.gc();
    }

}
