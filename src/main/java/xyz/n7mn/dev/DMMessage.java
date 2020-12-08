package xyz.n7mn.dev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;

public class DMMessage {

    private final JDA jda;
    private final User author;
    private final Message message;
    private final String text;

    DMMessage(Message message, User author){
        this.author = author;
        this.message = message;

        jda = message.getJDA();
        text = message.getContentRaw();
    }

    public void run(){
        // 最初にコマンドを受ける地点

        if (author.getId().equals("529463370089234466")){
            admin();
            return;
        }

        reply();
    }

    private void reply(){

        if (author.getId().equals("781323086624456735")){
            return;
        }

        if (author.getId().equals("785322639295905792")){
            return;
        }

        message.getPrivateChannel().sendMessage(
                "ふぬ？なにもおきないですよ？\n" +
                "\n" +
                "このbotを入れるには：https://discord.com/api/oauth2/authorize?client_id=781323086624456735&permissions=8&scope=bot\n" +
                "botについてバグ報告、テスト、要望が出したい！： https://discord.gg/QP2hRSQaVV").queue((message -> {
            message.suppressEmbeds(true).queue();
        }));

        RestAction<User> nanami = jda.retrieveUserById("529463370089234466");
        PrivateChannel dm = nanami.complete().openPrivateChannel().complete();
        String debug = "----- Debug ----- \n" +
                "発言者: " + author.getAsTag() + "\n" +
                "発言内容：`"+text+"`";
        dm.sendMessage(debug).queue();

    }

    private void admin(){

        StringBuffer sb = new StringBuffer();

        if (text.equals("n.checkServer")){

            List<Guild> guilds = jda.getGuilds();

            sb.append("現在 "); sb.append(guilds.size()); sb.append(" サーバーで動いています。\n");

            for (Guild guild : guilds){
                sb.append(guild.getId());
                sb.append(" : ");
                sb.append(guild.getName());
                sb.append("\n");
            }

        }

        if (text.startsWith("n.checkServer")){

            String[] split = text.split(" ");
            if (split.length == 2){

                Guild guildById = jda.getGuildById(split[1]);

                if (guildById == null){
                    return;
                }

                sb.append("----- "); sb.append(guildById.getName()); sb.append("のチャンネル一覧 -----\n");
                for (TextChannel channel : guildById.getTextChannels()){

                    sb.append(channel.getId());
                    sb.append(" : ");
                    sb.append(channel.getName());
                    sb.append(" 発言可能か : ");
                    sb.append(channel.canTalk());
                    sb.append("\n");

                    if (sb.length() >= 1900){

                        message.getPrivateChannel().sendMessage(sb.toString()).queue();
                        sb.delete(0, sb.length());

                    }

                }


            }
        }

        message.getPrivateChannel().sendMessage(sb.toString()).queue();
    }
}
