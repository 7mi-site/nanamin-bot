package xyz.n7mn.dev.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicQueue {

    private String GuildId;
    private String AddDiscordId;
    private String AddDiscordUsername;
    private String AddDiscordNickname;
    private AudioTrack AudioTrack;


    public MusicQueue(String guildId, String addDiscordId, String addDiscordUsername, String addDiscordNickname, AudioTrack audioTrack){
        this.GuildId = guildId;
        this.AddDiscordId = addDiscordId;
        this.AddDiscordUsername = addDiscordUsername;
        this.AddDiscordNickname = addDiscordNickname;
        this.AudioTrack = audioTrack;
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

    public com.sedmelluq.discord.lavaplayer.track.AudioTrack getAudioTrack() {
        return AudioTrack;
    }

    public void setAudioTrack(com.sedmelluq.discord.lavaplayer.track.AudioTrack audioTrack) {
        AudioTrack = audioTrack;
    }

}
