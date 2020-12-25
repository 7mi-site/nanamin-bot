package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YululiVideoCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public YululiVideoCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {
        String[] split = getMessageText().split(" ", -1);

        if (split.length != 2){
            getMessage().reply("えらーですっ").queue();
            return;
        }

        if (split[1].startsWith("http")) {
            String[] urlSplit = getMessageText().split("/",-1);

            try {
                if (urlSplit.length != 7){
                    getMessage().reply("えらーですっ").queue();
                    return;
                }

                Guild guild = getJda().getGuildById(urlSplit[4]);
                if (guild == null){
                    getMessage().reply("えらーですっ").queue();
                    return;
                }

                TextChannel textChannelById = guild.getTextChannelById(urlSplit[5]);
                if (textChannelById == null){
                    getMessage().reply("えらーですっ").queue();
                    return;
                }

                textChannelById.retrieveMessageById(urlSplit[6]).queue(message ->{

                    List<TextChannel> textChannelList = getGuild().getTextChannels();
                    for (TextChannel textChannel : textChannelList) {

                        if (textChannel.getName().equals("nanami_setting")) {
                            textChannel.getHistoryAfter(1, 100).queue(messageHistory -> {

                                List<Message> messageList = messageHistory.getRetrievedHistory();
                                for (Message textMessage : messageList) {
                                    // System.out.println("デバッグ-0 : " + message.getContentRaw());
                                    if (textMessage.getContentRaw().toLowerCase().startsWith("douga:")) {
                                        String[] chSplit = textMessage.getContentRaw().split(" ");
                                        try {
                                            TextChannel channel1 = textMessage.getGuild().getTextChannelById(chSplit[1]);
                                            if (channel1 != null){
                                                String clip = YululiFunction.clip(message);
                                                channel1.sendMessage(clip).queue();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            } catch (Exception e){
                getMessage().reply("えらーですっ").queue();
                e.printStackTrace();
            }

        }
    }

}
