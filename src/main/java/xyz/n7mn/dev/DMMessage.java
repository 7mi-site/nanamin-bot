package xyz.n7mn.dev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;
import java.util.function.BooleanSupplier;

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

        if (author.getId().equals("529463370089234466") && text.startsWith("n.")){
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

        RestAction<User> nanami = jda.retrieveUserById("529463370089234466");
        PrivateChannel dm = nanami.complete().openPrivateChannel().complete();
        String debug = "----- Debug ----- \n" +
                "発言者: " + author.getAsTag() + "\n" +
                "発言内容：`"+text+"`";
        dm.sendMessage(debug).queue();

        if (message.getContentRaw().startsWith("ぱんつ何色") || message.getContentRaw().startsWith("パンツ何色")){
            message.getPrivateChannel().sendMessage("へんたいっ！").queue();
            return;
        }

        message.getPrivateChannel().sendMessage(
                "ふぬ？なにもおきないですよ？\n" +
                "\n" +
                "このbotを入れるには：https://discord.com/api/oauth2/authorize?client_id=781323086624456735&permissions=8&scope=bot\n" +
                "botについてバグ報告、テスト、要望が出したい！： https://discord.gg/QP2hRSQaVV").queue((message -> {
            message.suppressEmbeds(true).queue();
        }));


    }

    private void admin(){

        StringBuffer sb = new StringBuffer();

        if (text.equals("n.checkServer") && author.getId().equals("529463370089234466")){

            List<Guild> guilds = jda.getGuilds();

            sb.append("現在 "); sb.append(guilds.size()); sb.append(" サーバーで動いています。\n");

            for (Guild guild : guilds){
                sb.append(guild.getId());
                sb.append(" : ");
                sb.append(guild.getName());
                sb.append("\n");
            }
            return;
        }

        if (text.startsWith("n.checkServer") && author.getId().equals("529463370089234466")){

            String[] split = text.split(" ");
            if (split.length == 2){

                Guild guildById = jda.getGuildById(split[1]);

                if (guildById == null){
                    return;
                }

                try {

                    sb.append("サーバー名 : ");
                    sb.append(guildById.getName());
                    sb.append("\n");

                    sb.append("サーバーオーナー : ");
                    User ow = jda.getUserById(guildById.getOwnerId());
                    if (guildById.getOwner().getNickname() != null){

                        sb.append(guildById.getOwner().getNickname());
                        if (ow != null){
                            sb.append(" (");
                            sb.append(ow.getAsTag());
                            sb.append(")");
                        }

                        if (sb.length() >= 1900){
                            message.getPrivateChannel().sendMessage(sb.toString()).queue();
                            sb.delete(0, sb.length());
                        }
                    } else {
                        if (ow != null){
                            sb.append(ow.getAsTag());
                        }

                    }

                    sb.append("\n");

                    List<Member> members = guildById.getMembers();
                    sb.append(members.size());
                    sb.append("人このサーバーには入っています。\n");

                    sb.append("----- 入っているメンバー(取得できる分) -----\n");
                    for (Member member : members){

                        User user = jda.getUserById(member.getId());
                        if (member.getNickname() != null){
                            sb.append(member.getNickname());
                            sb.append(" (");
                            sb.append(user.getAsTag());
                            sb.append(")");
                            sb.append("\n");
                        } else {
                            if (user != null){
                                sb.append(user.getAsTag());
                                sb.append("\n");
                            }
                        }

                        if (sb.length() >= 1900){
                            message.getPrivateChannel().sendMessage(sb.toString()).queue();
                            sb.delete(0, sb.length());
                        }
                    }

                } catch (Exception e){

                    e.printStackTrace();

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
