package xyz.n7mn.dev.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.UUID;

public class MusicQueue {

    private final UUID QueueId;
    private String GuildId;
    private String AddDiscordId;
    private String AddDiscordUsername;
    private String AddDiscordNickname;
    private AudioTrack AudioTrack;
    private boolean isNicovideo;


    public MusicQueue(String guildId, String addDiscordId, String addDiscordUsername, String addDiscordNickname, AudioTrack audioTrack, boolean isNicovideo){
        this.QueueId = UUID.randomUUID();
        this.GuildId = guildId;
        this.AddDiscordId = addDiscordId;
        this.AddDiscordUsername = addDiscordUsername;
        this.AddDiscordNickname = addDiscordNickname;
        this.AudioTrack = audioTrack;
        this.isNicovideo = isNicovideo;
    }

    public UUID getQueueId() {
        return QueueId;
    }

    public String getGuildId() {
        return GuildId;
    }

    public void setGuildId(String guildId) {
        GuildId = guildId;
    }

    public String getAddDiscordId() {
        return AddDiscordId;
    }

    public void setAddDiscordId(String addDiscordId) {
        AddDiscordId = addDiscordId;
    }

    public String getAddDiscordUsername() {
        return AddDiscordUsername;
    }

    public void setAddDiscordUsername(String addDiscordUsername) {
        AddDiscordUsername = addDiscordUsername;
    }

    public String getAddDiscordNickname() {
        return AddDiscordNickname;
    }

    public void setAddDiscordNickname(String addDiscordNickname) {
        AddDiscordNickname = addDiscordNickname;
    }

    public AudioTrack getAudioTrack() {
        return AudioTrack;
    }

    public void setAudioTrack(com.sedmelluq.discord.lavaplayer.track.AudioTrack audioTrack) {
        AudioTrack = audioTrack;
    }

    public boolean isNicovideo() {
        return isNicovideo;
    }

    public void setNicovideo(boolean nicovideo) {
        isNicovideo = nicovideo;
    }
}
