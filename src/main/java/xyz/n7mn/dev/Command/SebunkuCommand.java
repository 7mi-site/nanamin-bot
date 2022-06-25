package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.n7mn.dev.Command.img.ImageData;
import xyz.n7mn.dev.Command.img.ImgSystem;
import xyz.n7mn.dev.Command.music.PlayerManager;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.shuffle;

public class SebunkuCommand extends Chat {
    public SebunkuCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {

        EmbedBuilder builder = new EmbedBuilder();

        if (getMessageText().toLowerCase().equals("n.sebunku")) {

            List<ImageData> sebunku = ImgSystem.getImageDataList("sebunku");
            sebunku.add(new ImageData("https://youtu.be/lwOnJUqBQhY",""));

            int i = new SecureRandom().nextInt(sebunku.size() - 1);

            if (!sebunku.get(i).getImageURL().startsWith("https://youtu.be")){
                builder.setTitle("sebunku");
                builder.setThumbnail("https://n.7mi.site/nana-bot/sebunku/body.png");
                builder.setImage(sebunku.get(i).getImageURL());
                builder.setColor(Color.PINK);
                getTextChannel().sendMessage(builder.build()).queue();
                return;
            }

            getTextChannel().sendMessage(sebunku.get(i).getDescription()).queue();

            return;
        }

        if (getMessageText().toLowerCase().equals("n.playsebunku")){
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
