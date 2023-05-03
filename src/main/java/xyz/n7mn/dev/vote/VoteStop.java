package xyz.n7mn.dev.vote;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class VoteStop {

    YamlMapping ConfigYml = null;

    public VoteStop(){
        File config = new File("./config-vote.yml");
        try {
            if (!config.exists()){
                config.createNewFile();

                YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
                ConfigYml = builder.add(
                        "RedisServer", "127.0.0.1"
                ).add(
                        "RedisPort", String.valueOf(Protocol.DEFAULT_PORT)
                ).add(
                        "RedisPass", ""
                ).build();

                try {
                    PrintWriter writer = new PrintWriter(config);
                    writer.print(ConfigYml.toString());
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                ConfigYml = Yaml.createYamlInput(config).readYamlMapping();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(SlashCommandInteractionEvent event){
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("ななみちゃんbot 投票機能");
        builder.setColor(Color.PINK);
        builder.clearFields();
        if (ConfigYml == null){
            builder.setColor(Color.RED);
            builder.setDescription("内部エラー");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        JedisPool pool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
        Jedis jedis = pool.getResource();
        jedis.auth(ConfigYml.string("RedisPass"));

        String[] ChannelId = new String[]{event.getChannel().getId()};
        String[] MessageId = new String[]{null};
        String VoteId = null;

        if (event.getOption("メッセージリンク") != null){
            String[] split = event.getOption("メッセージリンク").getAsString().split("/");

            ChannelId[0] = split[split.length - 2];
            MessageId[0] = split[split.length - 1];
        } else {
            // 最新の投票の投稿を持ってくる
            event.getGuild().getTextChannelById(ChannelId[0]).getHistoryBefore(event.getId(), 100).queue((messageHistory -> {
                List<Message> history = messageHistory.getRetrievedHistory();
                for (Message message : history){
                    for (String key : jedis.keys("nanamibot:vote:contents:*")) {
                        if (jedis.get(key).equals(message.getId())){
                            MessageId[0] = jedis.get(key);
                            break;
                        }
                    }
                }
            }));
        }

        for (String key : jedis.keys("nanamibot:vote:contents:*")){
            if (jedis.get(key).equals(MessageId[0])){
                String[] split = key.split(":");
                VoteId = split[split.length - 1];
                break;
            }
        }

        VoteContents contents = new Gson().fromJson(jedis.get("nanamibot:vote:data:" + VoteId), VoteContents.class);
        VoteResult result = new Vote(event.getJDA()).check(MessageId[0]);

        if (ChannelId[0] == null || MessageId[0] == null || VoteId == null){
            builder.setColor(Color.RED);
            builder.setDescription("投票が見つかりませんでしたっ");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        if (contents.isEndFlag()){
            builder.setColor(Color.RED);
            builder.setDescription("すでに終了済みです！");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        builder.setTitle("ななみちゃんbot 投票機能");
        builder.setColor(Color.PINK);
        builder.clearFields();
        builder.setDescription("「"+contents.getTitle()+"」の投票が投票終了しました！\n結果は以下のとおりです！");

        builder.addField("投票総数", result.getTotalCount()+" 票", false);
        if (contents.getVoteType().equals("only")){
            if (result.getValidityCount() > 0){
                builder.addField("有効投票総数", result.getValidityCount()+" 票 ("+((result.getValidityCount()) / result.getTotalCount() * 100)+" %)", false);
            } else {
                builder.addField("有効投票総数", result.getValidityCount()+" 票 (0 %)", false);
            }
        }
        String[] reactionList = new String[]{"\uD83C\uDDE6", "\uD83C\uDDE7", "\uD83C\uDDE8", "\uD83C\uDDE9", "\uD83C\uDDEA", "\uD83C\uDDEB", "\uD83C\uDDEC", "\uD83C\uDDED", "\uD83C\uDDEE", "\uD83C\uDDEF", "\uD83C\uDDF0", "\uD83C\uDDF1", "\uD83C\uDDF2", "\uD83C\uDDF3", "\uD83C\uDDF4", "\uD83C\uDDF5", "\uD83C\uDDF6", "\uD83C\uDDF7", "\uD83C\uDDF8", "\uD83C\uDDF9"};

        int i = 0;
        for (String name : result.getVoteResult()){
            int count = 0;
            StringBuilder voteNameList = new StringBuilder();

            for (PersonalResult personalResult : result.getPersonalResults()) {
                if (personalResult.getSelectReaction().equals(reactionList[i])){
                    count++;

                    if (voteNameList.length() == 0){
                        voteNameList.append(personalResult.getNickname() == null ? personalResult.getUsername() : personalResult.getNickname());
                    } else {
                        voteNameList.append("\n").append(personalResult.getNickname() == null ? personalResult.getUsername() : personalResult.getNickname());
                    }

                }
            }

            builder.addField(Emoji.fromUnicode(reactionList[i]).getFormatted() + " " + name + "("+count+" 票)", voteNameList.toString(), false);
            i++;
        }


        event.getJDA().getGuildById(contents.getGuildId()).getTextChannelById(contents.getMessageChannelId()).getHistoryAfter(MessageId[0], 1).queue(messageHistory -> {
            if (messageHistory.getMessageById(MessageId[0]) != null && messageHistory.getMessageById(MessageId[0]).getReactions() != null && messageHistory.getMessageById(MessageId[0]).getReactions().size() > 0){
                messageHistory.getMessageById(MessageId[0]).clearReactions().queue();
            }

        });
        event.getJDA().getGuildById(contents.getGuildId()).getTextChannelById(contents.getMessageChannelId()).sendMessageEmbeds(builder.build()).queue();

        contents.setEndFlag(true);
        jedis.set("nanamibot:vote:data:" + VoteId, new Gson().toJson(contents));


        builder.setDescription("投票を終了しました！");
        builder.clearFields();
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();

        pool.close();
        jedis.close();
    }

}
