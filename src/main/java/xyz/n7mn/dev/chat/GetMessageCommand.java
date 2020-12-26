package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class GetMessageCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public GetMessageCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        // https://discord.com/channels/517669763556704258/543092075336433681/788207229018308678
        String[] split = getMessageText().split("/",-1);

        if (split.length != 7){
            return;
        }

        Guild guild = getJda().getGuildById(split[4]);
        if (guild == null){
            getMessage().reply("見つからないよぉ").queue();
            return;
        }

        TextChannel textChannelById = guild.getTextChannelById(split[5]);
        if (textChannelById == null){
            getMessage().reply("見つからないよぉ").queue();
            return;
        }

        textChannelById.retrieveMessageById(split[6]).queue(message1 ->{
            String contentRaw = message1.getContentRaw();
            // System.out.println(contentRaw);
            boolean edited = message1.isEdited();
            User author = message1.getAuthor();

            if (contentRaw.length() <= 1000){
                getMessage().reply(
                        "```\n" + contentRaw + "\n```\n" +
                        "---- メッセージの情報 ----\n" +
                        "投稿したチャンネル : "+message1.getChannel().getName()+"\n" +
                        "文字数 : " + contentRaw.length()+"\n" +
                        "編集済みかどうか : " + edited+"\n" +
                        "投稿者 : "+author.getAsTag()+"\n"
                ).queue();
                return;
            }

            getMessage().reply(
                            "```\n" + contentRaw.substring(0, 1000) + "\n(1000文字を超えているため省略しています)\n```\n" +
                            "---- メッセージの情報 ----\n" +
                            "投稿したチャンネル : "+message1.getChannel().getName()+"\n" +
                            "文字数 : " + contentRaw.length()+"\n" +
                            "編集済みかどうか : " + edited+"\n" +
                            "投稿者 : "+author.getAsTag()+"\n"
            ).queue();
        });

    }
}
