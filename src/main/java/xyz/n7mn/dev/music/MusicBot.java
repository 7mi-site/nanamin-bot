package xyz.n7mn.dev.music;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import com.sun.glass.ui.ClipboardAssistance;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.command.music.AudioPlayerSendHandler;
import xyz.n7mn.dev.command.music.TrackScheduler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public void run(SlashCommandInteractionEvent event, OptionMapping option1, OptionMapping option2){
        OptionMapping URL = option1;
        OptionMapping Volume = option2;

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
            if (manager.getConnectedChannel() != null){
                manager.closeAudioConnection();
                List<MusicQueue> list = new ArrayList<>();
                list.addAll(musicQueueList);

                for (int i = 0; i < list.size(); i++){
                    if (list.get(i).getGuildId().equals(event.getGuild().getId())){
                        musicQueueList.remove(i);
                    }
                }

                player = playerManager.createPlayer();

                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("ななみちゃんbot 音楽再生機能");
                builder.setColor(Color.PINK);
                builder.setDescription("再生停止しました！");

                event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            }

            builder.setColor(Color.RED);
            builder.setTitle("ななみちゃんbot エラー");
            builder.setDescription("・・・ボイスチャンネルに入っていませんよ？\n(入っているのにこのメッセージが出る場合は管理している人に切断をお願いしてねっ！)");

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();

            return;
        }

        int volume = 20;
        if (Volume != null){
            try {
                volume = Volume.getAsInt();
            } catch (Exception e){
                volume = 20;
            }
        }

        if (event.getMember() == null || event.getMember().getVoiceState() == null || event.getMember().getVoiceState().getChannel() == null){

            builder.setColor(Color.RED);
            builder.setTitle("ななみちゃんbot エラー");
            builder.setDescription("・・・ボイスチャンネルに入っていませんね？\n(入っているのにこのメッセージが出る場合は見えないチャンネルの可能性があります。管理している人に設定をお願いしてねっ！)");

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            return;
        }

        AudioChannelUnion channel = event.getMember().getVoiceState().getChannel();

        TrackScheduler trackScheduler = new TrackScheduler(player, event.getGuild(), event, musicQueueList);
        player.addListener(trackScheduler);

        player.setVolume(volume);

        if (channel.getType().isAudio()){
            AudioManager manager = event.getGuild().getAudioManager();

            if (VideoURL.toLowerCase(Locale.JAPANESE).equals("stop")){
                manager.closeAudioConnection();
                return;
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
                    builder.setDescription(track.getInfo().title + "を追加しました！");

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
    }
}
