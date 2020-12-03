package xyz.n7mn.dev.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;
    /**
     * Track scheduler for the player.
     */
    public final TrackScheduler scheduler;

    /**
     * Creates a player and a track scheduler.
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    public AudioSendHandler getSendHandler(){
        return new AudioSendHandler() {
            private final AudioPlayer audioPlayer = player;
            private AudioFrame lastFrame;

            @Override
            public boolean canProvide() {
                lastFrame = audioPlayer.provide();
                return lastFrame != null;
            }

            @Override
            public ByteBuffer provide20MsAudio() {
                return ByteBuffer.wrap(lastFrame.getData());
            }

            @Override
            public boolean isOpus() {
                return true;
            }
        };
    }
}
