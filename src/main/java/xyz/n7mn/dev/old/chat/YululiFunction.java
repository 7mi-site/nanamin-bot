package xyz.n7mn.dev.old.chat;

import net.dv8tion.jda.api.entities.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YululiFunction {
    
    public static String clip(Message message){
        if (message != null) {

            String title = "";
            String content = "";
            String url = "";
            String rule = "";

            Matcher matcher = Pattern.compile("タイトル: (.*)").matcher(message.getContentRaw());
            Matcher matcher2 = Pattern.compile("タイトル： (.*)").matcher(message.getContentRaw());
            Matcher matcher3 = Pattern.compile("(.*)とか").matcher(message.getContentRaw());
            Matcher matcher4 = Pattern.compile("タイトル：　(.*)").matcher(message.getContentRaw());
            Matcher matcher5 = Pattern.compile("(.*)のやつ").matcher(message.getContentRaw());
            Matcher matcher6 = Pattern.compile("(.*)やつ").matcher(message.getContentRaw());
            Matcher matcher7 = Pattern.compile("タイトル　(.*)").matcher(message.getContentRaw());
            Matcher matcher8 = Pattern.compile("あと、(.*)とか").matcher(message.getContentRaw());
            Matcher matcher9 = Pattern.compile("あと(.*)とか").matcher(message.getContentRaw());
            Matcher matcher10 = Pattern.compile("(.*)はどう").matcher(message.getContentRaw());
            Matcher matcher11 = Pattern.compile("(.*)どう").matcher(message.getContentRaw());
            Matcher matcher12 = Pattern.compile("内容　(.*)").matcher(message.getContentRaw());
            Matcher matcher13 = Pattern.compile("配布リンク: (.*)").matcher(message.getContentRaw());
            Matcher matcher14 = Pattern.compile("ルール(.*)").matcher(message.getContentRaw());
            Matcher matcher15 = Pattern.compile("内容：(.*)").matcher(message.getContentRaw());
            Matcher matcher16 = Pattern.compile("いいなら(.*)とか").matcher(message.getContentRaw());

            // System.out.println("デバッグ-1");
            if (message.getContentRaw().startsWith("```")){
                message.addReaction("\u2705").queue();
                return message.getContentRaw().replaceAll("```","\n考案者: " + message.getMember().getUser().getAsTag() + "\n```") +
                        "\n" +
                        "原文リンク：https://discord.com/channels/"+message.getGuild().getId()+"/"+message.getTextChannel().getId()+"/"+message.getId();
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
            } else if (matcher16.find()){
                title = matcher16.group(1).replaceAll("いいなら","").replaceAll("とか","");
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

            if (title.length() > 0 && url.length() > 0){
                message.addReaction("\u2705").queue();
            } else if (content.length() > 0 || rule.length() > 0) {
                message.addReaction("\u26A0").queue();
            } else if (title.length() == 0 && url.length() == 0){
                message.addReaction("\u2757").queue();
            } else {
                message.addReaction("\u26A0").queue();
            }

            if (title.length() == 0 && url.length() == 0 && content.length() == 0 && rule.length() == 0){
                return "想定していないテキスト\n```\n" +
                        message.getContentRaw() +
                        "\n```\n" +
                        "原文リンク：https://discord.com/channels/"+message.getGuild().getId()+"/"+message.getTextChannel().getId()+"/"+message.getId();
            }

            return "```\n" +
                    "タイトル: " + title + "\n" +
                    "ダウンロードリンク: " + url + "\n" +
                    "内容: " + content + "\n" +
                    "ルール: " + rule + "\n" +
                    "考案者: " + message.getMember().getUser().getAsTag() + "\n" +
                    "\n```\n" +
                    "原文リンク：https://discord.com/channels/"+message.getGuild().getId()+"/"+message.getTextChannel().getId()+"/"+message.getId();
        }

        return "";
    }
    
}
