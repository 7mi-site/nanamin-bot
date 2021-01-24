package xyz.n7mn.dev.adminCommand;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class CheckServer extends DMInterface{
    public CheckServer(Message message) {
        super(message);
    }

    @Override
    public void run() {

        EmbedBuilder builder = new EmbedBuilder();
        String[] split = getMessageText().split(" ");

        if (split.length == 1 && getMessageText().toLowerCase().equals("n.checkserver")){

            List<Guild> guilds = getJda().getGuilds();

            builder.setTitle("動作サーバー一覧");
            builder.setDescription("現在 " + guilds.size() + "サーバーで動いてます。");

            for (Guild guild : guilds){
                builder.addField(guild.getName(), "ID : "+guild.getId() + "\nオーナー : " + guild.getOwner().getUser().getName(), false);
            }

            getMessage().getPrivateChannel().sendMessage(builder.build()).queue();
            return;
        }

        if (split.length == 2){

            Guild guildById = getJda().getGuildById(split[1]);

            if (guildById == null){
                getMessage().getPrivateChannel().sendMessage("取得できなかったよ...").queue();
                return;
            }

            builder.setTitle(guildById.getName() + "の情報");
            builder.setDescription("ID : " + guildById.getId());
            builder.setThumbnail(guildById.getIconUrl());
            if (guildById.getDescription() != null){
                builder.addField("概要", guildById.getDescription(), false);
            } else {
                builder.addField("概要", "", false);
            }
            builder.addField("オーナー", guildById.getOwner().getUser().getAsTag(), false);
            builder.addField("Bot導入日時", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date.from(guildById.getMember(getJda().getSelfUser()).getTimeJoined().toInstant())), false);
            builder.addField("チャンネル数", "テキストチャンネル : "+guildById.getTextChannels().size() + "\nボイスチャンネル : " + guildById.getVoiceChannels().size(), false);

            EmbedBuilder builder2 = new EmbedBuilder();
            EmbedBuilder builder3 = new EmbedBuilder();
            EmbedBuilder builder4 = new EmbedBuilder();
            StringBuffer buffer1 = new StringBuffer();
            StringBuffer buffer2 = new StringBuffer();
            StringBuffer buffer3 = new StringBuffer();

            builder2.setTitle("テキストチャンネル一覧");
            int i = 1;
            for (TextChannel textChannel : guildById.getTextChannels()){
                buffer1.append("`");
                buffer1.append(textChannel.getName());
                buffer1.append("`");
                buffer1.append("\nID : ");
                buffer1.append(textChannel.getId());
                buffer1.append("\n発言可能か : ");
                buffer1.append(textChannel.canTalk());
                buffer1.append("\n");

                if (buffer1.length() > 970){
                    builder.addField(i + "ページ目", buffer1.toString(), false);
                    buffer1.delete(0, buffer1.length());
                    i++;
                }
            }
            builder2.addField((i + 1) +"ページ目", buffer1.toString(), false);


            builder3.setTitle("ボイスチャンネル一覧");
            int p = 1;
            for (VoiceChannel voiceChannel : guildById.getVoiceChannels()){
                buffer2.append("`");
                buffer2.append(voiceChannel.getName());
                buffer2.append("`");
                buffer2.append("\nID : ");
                buffer2.append(voiceChannel.getId());
                buffer2.append("\n");

                if (buffer2.length() > 970){
                    builder.addField(p + "ページ目", buffer2.toString(), false);
                    buffer2.delete(0, buffer2.length());
                    p++;
                }
            }
            builder3.addField((p + 1) +"ページ目", buffer2.toString(), false);

            builder4.setTitle("メンバー一覧 (人数 : " + guildById.getMemberCount() + ")");
            int in = 1;
            for (Member member : guildById.getMembers()){
                if (member.getNickname() != null){
                    buffer3.append("`");
                    buffer3.append(member.getNickname());
                    buffer3.append("` (");
                    buffer3.append(member.getUser().getAsTag());
                    buffer3.append(")\n");
                    buffer3.append("ID : ");
                    buffer3.append(member.getId());
                } else {
                    buffer3.append("`");
                    buffer3.append(member.getUser().getAsTag());
                    buffer3.append("`\nID : ");
                    buffer3.append(member.getId());
                }
                buffer3.append("\n");
                if (buffer3.length() > 950){
                    builder4.addField(in + "ページ目", buffer3.toString(), false);
                    buffer3.delete(0, buffer3.length());
                    in++;
                }

            }
            builder4.addField((in + 1) + "ページ目", buffer3.toString(), false);

            getMessage().getPrivateChannel().sendMessage(builder.build()).queue();
            getMessage().getPrivateChannel().sendMessage(builder2.build()).queue();
            getMessage().getPrivateChannel().sendMessage(builder3.build()).queue();
            getMessage().getPrivateChannel().sendMessage(builder4.build()).queue();
        }
    }
}
