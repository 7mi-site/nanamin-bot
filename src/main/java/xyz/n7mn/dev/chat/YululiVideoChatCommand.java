package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.api.data.EarthquakeResult;
import xyz.n7mn.dev.api.data.eq.intensity.Area;
import xyz.n7mn.dev.api.data.eq.intensity.Pref;


import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YululiVideoChatCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public YululiVideoChatCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        if (!getTextChannel().getId().equals("791394344727871488") && !getTextChannel().getId().equals("791844368281108490")){
            return;
        }

        List<TextChannel> textChannelList = getGuild().getTextChannels();
        for (TextChannel channel : textChannelList) {

            if (channel.getName().equals("nanami_setting")) {
                channel.getHistoryAfter(1, 100).queue(messageHistory -> {

                    List<Message> messageList = messageHistory.getRetrievedHistory();
                    for (Message message : messageList) {
                        // System.out.println("デバッグ-0 : " + message.getContentRaw());
                        if (message.getContentRaw().toLowerCase().startsWith("douga:")) {
                            String[] split = message.getContentRaw().split(" ");
                            try {
                                // System.out.println("デバッグ-01 : " + split[1].replaceAll("<", "").replaceAll(">", ""));
                                TextChannel ch = getGuild().getTextChannelById(split[1].replaceAll("<", "").replaceAll(">", ""));
                                // System.out.println("デバッグ-02 : " + (ch != null));

                                String clip = YululiFunction.clip(getMessage());
                                if (clip.length() > 0 && ch != null){
                                    ch.sendMessage(clip).queue();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });

            }
        }

    }

}
