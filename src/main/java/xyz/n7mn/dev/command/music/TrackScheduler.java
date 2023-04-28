package xyz.n7mn.dev.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.n7mn.dev.music.MusicQueue;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final SlashCommandInteractionEvent event;
    private final List<MusicQueue> musicQueueList;
    private final Guild guild;

    public TrackScheduler(AudioPlayer player, Guild guild, SlashCommandInteractionEvent event, List<MusicQueue> musicQueueList) {
        this.player = player;
        this.guild = guild;
        this.event = event;
        this.musicQueueList = musicQueueList;
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        // Player was paused
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        // Player was resumed
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        // A track started playing

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("ななみちゃんbot 音楽再生機能");
        builder.setColor(Color.PINK);
        builder.setDescription(track.getInfo().title + "を再生します！\nURL : "+track.getInfo().identifier);

        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {

        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        // An already playing track threw an exception (track end event will still be received separately)
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }

    public void play(AudioTrack track){

        List<MusicQueue> list = new ArrayList<>();
        for (MusicQueue q : musicQueueList){
            if (guild.getId().equals(q.getGuildId())){
                list.add(q);
            }
        }

        if (list.size() == 0){
            player.playTrack(track);
        }

        musicQueueList.add(new MusicQueue(guild.getId(), event.getMember().getId(), event.getMember().getAsMention(), event.getMember().getNickname(), track));
    }
}
