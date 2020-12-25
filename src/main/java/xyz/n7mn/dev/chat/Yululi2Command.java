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

        // System.out.println("デバッグ-a");

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
                                if (ch != null) {
                                    String title = "";
                                    String content = "";
                                    String url = "";
                                    String rule = "";

                                    Matcher matcher = Pattern.compile("タイトル: (.*)").matcher(getMessageText());
                                    Matcher matcher2 = Pattern.compile("タイトル： (.*)").matcher(getMessageText());
                                    Matcher matcher3 = Pattern.compile("(.*)とか").matcher(getMessageText());
                                    Matcher matcher4 = Pattern.compile("タイトル：　(.*)").matcher(getMessageText());
                                    Matcher matcher5 = Pattern.compile("(.*)のやつ").matcher(getMessageText());
                                    Matcher matcher6 = Pattern.compile("(.*)やつ").matcher(getMessageText());
                                    Matcher matcher7 = Pattern.compile("タイトル　(.*)").matcher(getMessageText());
                                    Matcher matcher8 = Pattern.compile("あと、(.*)とか").matcher(getMessageText());
                                    Matcher matcher9 = Pattern.compile("あと(.*)とか").matcher(getMessageText());
                                    Matcher matcher10 = Pattern.compile("(.*)はどう").matcher(getMessageText());
                                    Matcher matcher11 = Pattern.compile("(.*)どう").matcher(getMessageText());
                                    Matcher matcher12 = Pattern.compile("内容　(.*)").matcher(getMessageText());
                                    Matcher matcher13 = Pattern.compile("配布リンク: (.*)").matcher(getMessageText());
                                    Matcher matcher14 = Pattern.compile("ルール(.*)").matcher(getMessageText());
                                    Matcher matcher15 = Pattern.compile("内容：(.*)").matcher(getMessageText());

                                    // System.out.println("デバッグ-1");
                                    if (getMessageText().startsWith("```")){
                                        getMessage().addReaction("\u2705").queue();
                                        ch.sendMessage(getMessageText().replaceAll("```","\n考案者: " + getMember().getUser().getAsTag() + "\n```") +
                                                "\n" +
                                                "原文リンク：https://discord.com/channels/"+getGuild().getId()+"/"+getTextChannel().getId()+"/"+getMessage().getId()).queue();
                                        return;
                                    }

                                    if (matcher.find()){
                                        title = matcher.group(1).replaceAll("タイトル: ","");
                                    } else if (matcher2.find()){
                                        title = matcher2.group(1).replaceAll("タイトル： ","");
                                    } else if (matcher3.find()){
                                        title = matcher3.group(0).replaceAll("とか","");
                                    } else if (matcher4.find()){
                                        title = matcher4.group(1).replaceAll("タイトル：　","");
                                    } else if (matcher5.find()){
                                        title = matcher5.group(0).replaceAll("のやつ","");
                                    } else if (matcher6.find()){
                                        title = matcher6.group(0).replaceAll("やつ","");
                                    } else if (matcher7.find()){
                                        title = matcher7.group(1).replaceAll("タイトル　","");
                                    }

                                    if (matcher8.find()){
                                        content = matcher8.group(1).replaceAll("あと、","").replaceAll("とか","");
                                    } else if (matcher9.find()){
                                        content = matcher9.group(1).replaceAll("あと","").replaceAll("とか","");
                                    } else if (matcher10.find()){
                                        content = matcher10.group(0).replaceAll("はどう","");
                                    } else if (matcher11.find()){
                                        content = matcher11.group(0).replaceAll("どう","");
                                    } else if (matcher12.find()){
                                        content = matcher12.group(1).replaceAll("内容　","");
                                    } else if (matcher15.find()){
                                        content = matcher15.group(1).replaceAll("内容：","");
                                    }

                                    if (matcher13.find()){
                                        url = matcher13.group(1).replaceAll("配布リンク: ","");
                                    }

                                    if (matcher14.find()){
                                        rule = matcher14.group().replaceAll("ルール","");
                                    }


                                    // System.out.println("デバッグ-2");

                                    getMessage().addReaction("\u2705").queue();
                                    ch.sendMessage("```\n" +
                                            "タイトル: " + title + "\n" +
                                            "ダウンロードリンク: " + url + "\n" +
                                            "内容: " + content + "\n" +
                                            "ルール: " + rule + "\n" +
                                            "考案者: " + getMember().getUser().getAsTag() + "\n" +
                                            "\n```\n" +
                                            "原文リンク：https://discord.com/channels/"+getGuild().getId()+"/"+getTextChannel().getId()+"/"+getMessage().getId()).queue();
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
