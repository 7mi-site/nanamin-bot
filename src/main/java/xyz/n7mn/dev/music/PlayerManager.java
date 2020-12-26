package xyz.n7mn.dev.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long,GuildMusicManager> musicManagers;
    private Timer timer = new Timer();
    private List<String> repeatGuildIdList = Collections.synchronizedList(new ArrayList<>());

    private PlayerManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId,musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;

    }

    public void Repeat(TextChannel channel){
        boolean rep = false;

        for (String guildId : repeatGuildIdList){
            if (guildId.equals(channel.getGuild().getId())){
                rep = true;
                repeatGuildIdList.remove(guildId);
                break;
            }
        }

        if (rep){
            channel.sendMessage("1曲ループモードをやめましたっ！").queue();
        } else {
            channel.sendMessage("1曲ループモードになりましたっ！").queue();
            repeatGuildIdList.add(channel.getGuild().getId());
        }

        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        AudioPlayer player = musicManager.player;

        long time = player.getPlayingTrack().getInfo().length - player.getPlayingTrack().getPosition();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                boolean flag = false;
                for (String repeat : repeatGuildIdList){
                    if (repeat.equals(channel.getGuild().getId())){
                        flag = true;
                        break;
                    }

                }

                if (!flag){
                    this.cancel();
                    return;
                }
                musicManager.scheduler.queue(player.getPlayingTrack().makeClone());
            }
        };

        if (!rep){
            timer.schedule(task, time, player.getPlayingTrack().getInfo().length);
        } else {
            timer.cancel();
        }

    }

    public void loadAndPlay(TextChannel channel, String trackURL) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("再生する音楽を追加したよっ\n「" + track.getInfo().title+"」").queue();
                play(musicManager,track);
                // System.out.println(musicManager.player.getPlayingTrack().getInfo().length);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                channel.sendMessage("再生するリストを追加したよっ\n「" + playlist.getName()+"」").queue();
                for (AudioTrack track : playlist.getTracks()){
                    play(musicManager, track);
                }

                // play(musicManager,firstTrack, channel);
            }

            @Override
            public void noMatches() {
                //見つからなかった
                channel.sendMessage("再生する音楽が見つからなかったよっ").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                //ロード時にエラー発生
                channel.sendMessage("エラーが発生したよっ").queue();
            }

        });

    }

    private void play(GuildMusicManager musicManager,AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}
