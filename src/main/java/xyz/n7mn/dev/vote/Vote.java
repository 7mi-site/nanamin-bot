package xyz.n7mn.dev.vote;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;
import java.util.List;

public class Vote {

    private JedisPool jedisPool;
    private YamlMapping ConfigYml = null;
    private EmbedBuilder builder = new EmbedBuilder();

    public Vote(JDA jda){

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

            if (ConfigYml != null){
                jedisPool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
            } else {
                jedisPool = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.gc();
        }

        new Thread(()->{
            if (ConfigYml == null){
                return;
            }

            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    JedisPool pool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
                    Jedis jedis = pool.getResource();
                    jedis.auth(ConfigYml.string("RedisPass"));

                    //System.out.println("----");

                    for (String key : jedis.keys("nanamibot:vote:contents:*")) {
                        String[] temp = key.split(":");
                        String voteId = temp[temp.length - 1];

                        VoteContents contents = new Gson().fromJson(jedis.get("nanamibot:vote:data:" + voteId), VoteContents.class);
                        if (new Date().getTime() >= contents.getEndDate().getTime() && !contents.isEndFlag()){
                            String MessageId = jedis.get(key);

                            builder.setTitle("ななみちゃんbot 投票機能");
                            builder.setColor(Color.PINK);
                            builder.clearFields();
                            builder.setDescription("「"+contents.getTitle()+"」の投票が投票終了しました！\n結果は以下のとおりです！");
                            VoteResult result = check(MessageId);
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


                            jda.getGuildById(contents.getGuildId()).getTextChannelById(contents.getMessageChannelId()).getHistoryAfter(MessageId, 1).queue(messageHistory -> {
                                if (messageHistory.getMessageById(MessageId) != null && messageHistory.getMessageById(MessageId).getReactions() != null && messageHistory.getMessageById(MessageId).getReactions().size() > 0){
                                    messageHistory.getMessageById(MessageId).clearReactions().queue();
                                }

                            });
                            jda.getGuildById(contents.getGuildId()).getTextChannelById(contents.getMessageChannelId()).sendMessageEmbeds(builder.build()).queue();

                            contents.setEndFlag(true);
                            jedis.set("nanamibot:vote:data:" + voteId, new Gson().toJson(contents));
                        }
                    }

                    //System.out.println("----");
                    jedis.close();
                    pool.close();
                    System.gc();

                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            timer.scheduleAtFixedRate(timerTask, 0L, 1000L);

        }).start();
    }

    public void run(SlashCommandInteractionEvent event){
        builder.setTitle("ななみちゃんbot 投票機能");
        builder.setColor(Color.PINK);
        builder.clearFields();
        if (ConfigYml == null){
            builder.setColor(Color.RED);
            builder.setDescription("内部エラー");
            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            return;
        }

        Jedis jedis = jedisPool.getResource();
        jedis.auth(ConfigYml.string("RedisPass"));
        VoteContents contents;
        String[] vote;
        String[] voteList = new String[]{"\uD83C\uDDE6", "\uD83C\uDDE7", "\uD83C\uDDE8", "\uD83C\uDDE9", "\uD83C\uDDEA", "\uD83C\uDDEB", "\uD83C\uDDEC", "\uD83C\uDDED", "\uD83C\uDDEE", "\uD83C\uDDEF", "\uD83C\uDDF0", "\uD83C\uDDF1", "\uD83C\uDDF2", "\uD83C\uDDF3", "\uD83C\uDDF4", "\uD83C\uDDF5", "\uD83C\uDDF6", "\uD83C\uDDF7", "\uD83C\uDDF8", "\uD83C\uDDF9"};
        try {

             vote = new String[]{
                     (event.getOption("選択肢1") != null ? event.getOption("選択肢1").getAsString() : null),
                     (event.getOption("選択肢2") != null ? event.getOption("選択肢2").getAsString() : null),
                     (event.getOption("選択肢3") != null ? event.getOption("選択肢3").getAsString() : null),
                     (event.getOption("選択肢4") != null ? event.getOption("選択肢4").getAsString() : null),
                     (event.getOption("選択肢5") != null ? event.getOption("選択肢5").getAsString() : null),
                     (event.getOption("選択肢6") != null ? event.getOption("選択肢6").getAsString() : null),
                     (event.getOption("選択肢7") != null ? event.getOption("選択肢7").getAsString() : null),
                     (event.getOption("選択肢8") != null ? event.getOption("選択肢8").getAsString() : null),
                     (event.getOption("選択肢9") != null ?  event.getOption("選択肢9").getAsString(): null),
                     (event.getOption("選択肢10") != null ? event.getOption("選択肢10").getAsString() : null),
                     (event.getOption("選択肢11") != null ? event.getOption("選択肢11").getAsString() : null),
                     (event.getOption("選択肢12") != null ? event.getOption("選択肢12").getAsString() : null),
                     (event.getOption("選択肢13") != null ? event.getOption("選択肢13").getAsString() : null),
                     (event.getOption("選択肢14") != null ? event.getOption("選択肢14").getAsString() : null),
                     (event.getOption("選択肢15") != null ? event.getOption("選択肢15").getAsString() : null),
                     (event.getOption("選択肢16") != null ? event.getOption("選択肢16").getAsString() : null),
                     (event.getOption("選択肢17") != null ? event.getOption("選択肢17").getAsString() : null),
                     (event.getOption("選択肢18") != null ? event.getOption("選択肢18").getAsString() : null),
                     (event.getOption("選択肢19") != null ? event.getOption("選択肢19").getAsString() : null),
                     (event.getOption("選択肢20") != null ? event.getOption("選択肢20").getAsString() : null)
             };

            contents = new VoteContents(event.getGuild().getId(), event.getMessageChannel().getId(), event.getOption("タイトル").getAsString(), vote, event.getOption("投票終了日時").getAsString().replaceAll("なし","9999-12-31 23:59:59"), event.getOption("投票形式") != null ? event.getOption("投票形式").getAsString() : "default");

        } catch (ParseException e) {
            e.printStackTrace();
            builder.setColor(Color.RED);
            builder.setDescription("日付の形式が正しくありません。\n例: `2024-01-01 00:00:00'");

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            jedis.close();
            return;
        }

        jedis.set("nanamibot:vote:data:"+contents.getVoteID().toString(), new Gson().toJson(contents));
        jedis.close();

        builder.setDescription(
                "タイトル : "+event.getOption("タイトル").getAsString()+"\n" +
                "終了日付 : "+event.getOption("投票終了日時").getAsString() + "\n" +
                "※一度投票したものは取り消せません！"
        );

        int i = 0;
        for (String v : vote){
            if (v != null && v.length() > 0){
                builder.addField(Emoji.fromUnicode(voteList[i]).getFormatted(), v, false);
            }
            i++;
        }

        event.replyEmbeds(new EmbedBuilder().setColor(Color.PINK).setTitle("ななみちゃんbot 投票機能").setDescription("投票を開始しましたっ\n終了させる場合は/vote-stopでできます！").build()).setEphemeral(true).queue();

        event.getMessageChannel().sendMessageEmbeds(builder.build()).queue(message -> {
            new Thread(()->{
                JedisPool jedisPool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
                Jedis jedis1 = jedisPool.getResource();
                jedis1.auth(ConfigYml.string("RedisPass"));
                jedis1.set("nanamibot:vote:contents:"+contents.getVoteID().toString(), message.getId());
                jedis1.close();
                jedisPool.close();
            }).start();

            int x = 0;
            for (String v : vote){
                if (v != null && v.length() > 0){
                    message.addReaction(Emoji.fromUnicode(voteList[x])).queue();
                }
                x++;
            }
        });
    }

    public void add(MessageReactionAddEvent event){
        if (event.getMember().getUser().isBot() || event.getMember().getUser().isSystem()){
            return;
        }
        String[] voteList = new String[]{"\uD83C\uDDE6", "\uD83C\uDDE7", "\uD83C\uDDE8", "\uD83C\uDDE9", "\uD83C\uDDEA", "\uD83C\uDDEB", "\uD83C\uDDEC", "\uD83C\uDDED", "\uD83C\uDDEE", "\uD83C\uDDEF", "\uD83C\uDDF0", "\uD83C\uDDF1", "\uD83C\uDDF2", "\uD83C\uDDF3", "\uD83C\uDDF4", "\uD83C\uDDF5", "\uD83C\uDDF6", "\uD83C\uDDF7", "\uD83C\uDDF8", "\uD83C\uDDF9"};

        //System.out.println("a1");
        int addVote = -1;
        int i = 0;
        for (String str : voteList) {
            if (str.equals(event.getEmoji().getAsReactionCode())){
                addVote = i;
                break;
            }
            i++;
        }

        //System.out.println("a2");
        if (addVote == -1){
            event.getReaction().removeReaction().queue();
            return;
        }
        //System.out.println("a3");

        new Thread(()->{
            event.getReaction().removeReaction(event.getUser()).queue();
            builder.setTitle("ななみちゃんbot 投票機能");
            builder.setColor(Color.GREEN);
            builder.clearFields();
            builder.setDescription("投票完了しました！\n(このメッセージは2秒後に消えます...たぶん。)");
            event.getChannel().sendMessage(event.getMember().getAsMention()).setEmbeds(builder.build()).queue((message -> {
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                message.delete().queue();
            }));

        }).start();

        new Thread(()->{
            JedisPool pool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
            Jedis jedis = pool.getResource();
            jedis.auth(ConfigYml.string("RedisPass"));

            for (String key : jedis.keys("nanamibot:vote:contents:*")) {
                if (!jedis.get(key).equals(event.getMessageId())){
                    //System.out.println(jedis.get(key) + " / " + event.getMessageId());
                    continue;
                }
                String[] split = key.split(":");

                PersonalResult result = new PersonalResult(event.getEmoji().getAsReactionCode(), event.getMember().getUser().getId(), event.getMember().getUser().getAsTag(), event.getMember().getUser().getName(), event.getMember().getNickname(), true);
                jedis.set("nanamibot:vote:result:"+new Date().getTime()+":"+split[split.length - 1], new Gson().toJson(result));
                return;
            }

            jedis.close();
            pool.close();
            System.gc();

        }).start();
    }

    public VoteResult check(String MessageId){
        VoteResult result = null;

        JedisPool pool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
        Jedis jedis = pool.getResource();
        jedis.auth(ConfigYml.string("RedisPass"));

        for (String key : jedis.keys("nanamibot:vote:contents:*")) {
            if (!jedis.get(key).equals(MessageId)){
                continue;
            }

            String[] split = key.split(":");
            Set<String> keys = jedis.keys("nanamibot:vote:result:*:" + split[split.length - 1]);
            VoteContents contents = new Gson().fromJson(jedis.get("nanamibot:vote:data:" + split[split.length - 1]), VoteContents.class);
            String[] temp = new String[contents.getVote().length];

            int i = 0;
            for (String t : contents.getVote()){
                if (t == null){
                    break;
                }
                //System.out.println(t);

                temp[i] = t;
                i++;
            }

            String[] temp2 = new String[i];
            for (int x = 0; x < temp2.length; x++){
                temp2[x] = temp[x];
            }
            temp = temp2;

            List<PersonalResult> resultList = new ArrayList<>();
            Map<String, Boolean> map = new HashMap<>();
            long validityCount = 0;
            for (String key1 : keys) {
                PersonalResult json = new Gson().fromJson(jedis.get(key1), PersonalResult.class);

                if (!json.isActive()){
                    // すでに無効の場合は処理スキップ
                    continue;
                }

                if (map.get(json.getUserID()) != null && contents.getVoteType().equals("only")){
                    // 1人1選択肢の場合は除外する
                    continue;
                }
                validityCount++;
                resultList.add(json);
                map.put(json.getUserID(), true);
            }
            result = new VoteResult(contents.getTitle(), temp, validityCount, keys.size(), resultList);
            map.clear();
            break;
        }

        jedis.close();
        pool.close();
        System.gc();

        return result;
    }

}
