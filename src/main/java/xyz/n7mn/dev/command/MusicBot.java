package xyz.n7mn.dev.command;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.Locale;

public class MusicBot {

    private final SlashCommandInteractionEvent event;
    private final OptionMapping URL;
    private final OptionMapping Volume;
    private final EmbedBuilder builder = new EmbedBuilder();

    public MusicBot(SlashCommandInteractionEvent event, OptionMapping option1, OptionMapping option2) {

        this.event = event;
        this.URL = option1;
        this.Volume = option2;

    }

    public void run(){
        String VideoURL = null;
        if (!URL.getAsString().startsWith("http")){

            builder.setColor(Color.RED);
            builder.setTitle("ななみちゃんbot エラー");
            builder.setDescription("URLを指定してください！");

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            return;
        } else {
            VideoURL = URL.getAsString();
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

        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();
        player.setVolume(volume);
        TrackScheduler trackScheduler = new TrackScheduler(player, event.getChannel());
        player.addListener(trackScheduler);

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
                    trackScheduler.queue(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    for (AudioTrack track : playlist.getTracks()) {
                        trackScheduler.queue(track);
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
