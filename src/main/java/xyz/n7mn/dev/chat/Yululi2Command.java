package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.api.data.EarthquakeResult;
import xyz.n7mn.dev.api.data.eq.intensity.Area;
import xyz.n7mn.dev.api.data.eq.intensity.Pref;


import java.util.Date;
import java.util.List;

public class Yululi2Command extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public Yululi2Command(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        List<TextChannel> textChannelList = getGuild().getTextChannels();
        for (TextChannel channel : textChannelList) {

            if (channel.getName().equals("nanami_setting")) {
                channel.getHistoryAfter(1, 100).queue(messageHistory -> {

                    List<Message> messageList = messageHistory.getRetrievedHistory();
                    for (Message message : messageList) {
                        if (message.getContentRaw().toLowerCase().startsWith("douga: ")) {
                            String[] split = message.getContentRaw().split(" ");
                            try {
                                TextChannel ch = getGuild().getTextChannelById(split[1].replaceAll("<", "").replaceAll(">", ""));
                                if (ch != null) {



                                    ch.sendMessage("```\naaaa\n```").queue();
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
