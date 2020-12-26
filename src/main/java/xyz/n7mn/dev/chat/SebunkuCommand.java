package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.music.GuildMusicManager;
import xyz.n7mn.dev.music.PlayerManager;

import java.util.List;

public class SebunkuCommand extends CommandClassInterface {
    // 必要に応じて上のextendsするクラスを変える
    // 通常コマンド : CommandClassInterface
    // 投票系コマンド : VoteCommandClassInterface
    // ゲーム系コマンド : GameCommandClassInterface

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public SebunkuCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        if (getMessageText().toLowerCase().startsWith("n.sebunku")){

            getTextChannel().sendMessage("https://youtu.be/lwOnJUqBQhY").queue();
            return;

        }

        if (getMessageText().toLowerCase().startsWith("n.playsebunku")){

            AudioManager audioManager = getGuild().getAudioManager();
            if (!audioManager.isConnected()){
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
                                    voiceChannel = vc;
                                }

                            }

                        }
                    } catch (Exception e){
                        // e.printStackTrace();
                    }

                }

                if (voiceChannel != null){
                    audioManager.openAudioConnection(voiceChannel);
                }
            }

            PlayerManager Playermanager = PlayerManager.getINSTANCE();
            Playermanager.loadAndPlay(getTextChannel(), "https://www.youtube.com/watch?v=lwOnJUqBQhY");

        }

    }
}
