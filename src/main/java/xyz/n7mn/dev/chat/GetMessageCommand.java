package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

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

            EmbedBuilder builder = new EmbedBuilder();
            builder.addField("内容", contentRaw, true);
            MessageEmbed build = builder.build();

            getMessage().reply(
                    "---- メッセージの情報 ----\n" +
                            "投稿したチャンネル : " + message1.getChannel().getName() + "\n" +
                            "文字数 : " + contentRaw.length() + "\n" +
                            "編集済みかどうか : " + edited + "\n" +
                            "投稿者 : " + author.getAsTag() + "\n"
            ).embed(build).queue();

        });

    }
}
