package xyz.n7mn.dev.music;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.n7mn.dev.command.music.AudioPlayerSendHandler;
import xyz.n7mn.dev.command.music.TrackScheduler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.n7mn.dev.command.music.MusicBotFunction.getTitle;
import static xyz.n7mn.dev.command.music.MusicBotFunction.getURL;

public class MusicBot {

    private final EmbedBuilder builder = new EmbedBuilder();
    private final List<MusicQueue> musicQueueList;
    private final AudioPlayerManager playerManager;
    private AudioPlayer player;

    public MusicBot(List<MusicQueue> musicQueueList) {
        this.musicQueueList = musicQueueList;

        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        player = playerManager.createPlayer();
    }

    public void run(SlashCommandInteractionEvent event, OptionMapping URL, OptionMapping option){
        System.gc();

        String VideoURL = null;

        if (!URL.getAsString().startsWith("stop") && !URL.getAsString().startsWith("volume") && !URL.getAsString().startsWith("nowplay") && !URL.getAsString().startsWith("skip") && !URL.getAsString().startsWith("http")){

            builder.setColor(Color.RED);
            builder.setTitle("ななみちゃんbot エラー");
            builder.setDescription("URLを指定してください！");

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            return;
        } else {
            VideoURL = URL.getAsString();
        }

        if (URL.getAsString().startsWith("stop")){
            AudioManager manager = event.getGuild().getAudioManager();

            List<MusicQueue> list = new ArrayList<>(musicQueueList);

            for (int i = 0; i < list.size(); i++){
                if (list.get(i).getGuildId().equals(event.getGuild().getId())){
                    musicQueueList.remove(i);
                }
            }
            player = playerManager.createPlayer();

            if (manager.getConnectedChannel() != null) {

                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("ななみちゃんbot 音楽再生機能");
                builder.setColor(Color.PINK);
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

                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("ななみちゃんbot 音楽再生機能");
                builder.setColor(Color.PINK);

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

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("ななみちゃんbot 音楽再生機能");
            builder.setColor(Color.PINK);
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
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("ななみちゃんbot 音楽再生機能");
            builder.setColor(Color.PINK);

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

        // ここから音楽再生
        int volume = 20;
        if (option != null){
            try {
                volume = Math.max(Math.min(option.getAsInt(), 100), 0);
            } catch (Exception e){
                volume = 20;
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

        TrackScheduler trackScheduler = new TrackScheduler(player, event.getGuild(), event, musicQueueList);
        player.addListener(trackScheduler);

        if (player.getPlayingTrack() == null){
            player.setVolume(volume);
        }

        if (channel.getType().isAudio()){
            AudioManager manager = event.getGuild().getAudioManager();

            if (VideoURL.startsWith("http://nico.ms/") || VideoURL.startsWith("https://nico.ms/") || VideoURL.startsWith("http://nicovideo.jp/") || VideoURL.startsWith("https://nicovideo.jp/") || VideoURL.startsWith("http://www.nicovideo.jp/") || VideoURL.startsWith("https://www.nicovideo.jp/")){
                VideoURL = "https://nico.7mi.site/proxy/?"+VideoURL;
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
