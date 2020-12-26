package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.music.GuildMusicManager;
import xyz.n7mn.dev.music.PlayerManager;

import java.util.List;

public class MusicPlayCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public MusicPlayCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        AudioManager audioManager = getGuild().getAudioManager();

        boolean play = audioManager.isConnected();

        if (play){
            PlayerManager Playermanager = PlayerManager.getINSTANCE();
            GuildMusicManager guildMusicManager = Playermanager.getGuildMusicManager(getGuild());
            try {
                if (guildMusicManager.player.getPlayingTrack().getInfo().title != null){
                    play = true;
                } else {
                    play = false;
                }
            } catch (Exception e){
                play = false;
            }
        }

        if (!play){
            if (getMessage().isWebhookMessage() || getUser().isBot()){
                return;
            }

            String[] split = getMessageText().split(" ", -1);
            if (split.length != 2 && split.length != 3){
                getMessage().reply("音楽を再生するには\nボイスチャンネルに入ってから\n`n.play <URL>` または `n.play <URL> <0-100>`でお願いしますっ！").queue();
                return;
            }

            boolean find = false;

            List<VoiceChannel> voiceChannels = getGuild().getVoiceChannels();
            // voiceChannel = voiceChannels.get(0);

            VoiceChannel voiceChannel = null;
            for (VoiceChannel vc : voiceChannels){

                try {
                    // System.out.println(vc.getName() + " : " + vc.getMembers().size());

                    if (vc.getMembers().size() != 0){
                        List<Member> members = vc.getMembers();

                        for (Member member : members){

                            if (getMember().getId().equals(member.getId())){
                                find = true;
                                voiceChannel = vc;
                            }

                        }

                    }

                } catch (Exception e){
                    // e.printStackTrace();
                }

            }

            if (!find){
                getMessage().reply("どこかのボイスチャンネルに入ってくださいっ！！").queue();
                return;
            }

            if (!split[1].toLowerCase().startsWith("http")){
                getMessage().reply("わたしの知ってるURLじゃないみたい...").queue();
                return;
            }

            getMessage().delete().queue();
            //Guild guild = event.getJDA().getGuildById(event.getGuild().getId());
            audioManager.openAudioConnection(voiceChannel);

            PlayerManager Playermanager = PlayerManager.getINSTANCE();
            Playermanager.loadAndPlay(getTextChannel(), split[1]);



            if (split.length == 3){
                Playermanager.getGuildMusicManager(getGuild()).player.setVolume(Integer.parseInt(split[2]));
            } else {
                Playermanager.getGuildMusicManager(getGuild()).player.setVolume(100);
            }
        }

    }
}
