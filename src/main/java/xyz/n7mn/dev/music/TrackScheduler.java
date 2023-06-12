package xyz.n7mn.dev.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final SlashCommandInteractionEvent event;
    private final List<MusicQueue> musicQueueList;
    private final Guild guild;
    private final Map<String, String> nicoVideoHeartBeatList;

    public TrackScheduler(AudioPlayer player, Guild guild, SlashCommandInteractionEvent event, List<MusicQueue> musicQueueList, Map<String, String> nicoVideoHeartBeatList) {
        this.player = player;
        this.guild = guild;
        this.event = event;
        this.musicQueueList = musicQueueList;
        this.nicoVideoHeartBeatList = nicoVideoHeartBeatList;
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
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {

            //System.out.println("d 1-1");

            List<MusicQueue> temp = new ArrayList<>();
            for (MusicQueue m : musicQueueList){
                if (Objects.requireNonNull(event.getGuild()).getId().equals(m.getGuildId())){
                    temp.add(m);
                }
            }

            int del = -1;
            for (int i = 0; i < musicQueueList.size(); i++){
                if (temp.get(i).getAudioTrack().getInfo().uri.equals(track.getInfo().uri)){
                    del = i;
                }
            }

            if (del != -1){

                if (musicQueueList.get(del).isNicovideo()){
                    nicoVideoHeartBeatList.remove(musicQueueList.get(del).getAudioTrack().getInfo().uri);
                }

                musicQueueList.remove(del);
            }

            for (int i = 0; i < temp.size(); i++){
                //System.out.println(temp.get(i).getAudioTrack().getInfo().uri + " / " + track.getInfo().uri);

                if (temp.get(i).getAudioTrack().getInfo().uri.equals(track.getInfo().uri)){

                    if ((i + 1) >= temp.size()){
                        return;
                    }

                    this.player.playTrack(temp.get(i + 1).getAudioTrack());
                    temp.clear();
                    return;
                }
            }

            temp.clear();
            System.gc();

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

        musicQueueList.add(new MusicQueue(guild.getId(), Objects.requireNonNull(event.getMember()).getUser().getId(), event.getMember().getUser().getAsTag(), event.getMember().getNickname(), track, Pattern.compile("(.*)nicovideo(.*)").matcher(track.getInfo().uri).find()));
        //System.out.println("全体残りキュー : "+musicQueueList.size() + " / 残りキュー : " + list.size());

    }
}
