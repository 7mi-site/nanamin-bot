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
import net.dv8tion.jda.api.utils.FileUpload;
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
    private final EmbedBuilder builder = new EmbedBuilder();

    public Vote(JDA jda){

        File config = new File("./config-redis.yml");
        try {
            if (!config.exists()){

                YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
                ConfigYml = builder.add(
                        "RedisServer", "127.0.0.1"
                ).add(
                        "RedisPort", String.valueOf(Protocol.DEFAULT_PORT)
                ).add(
                        "RedisPass", ""
                ).build();

                try {
                    if (config.createNewFile()){
                        PrintWriter writer = new PrintWriter(config);
                        writer.print(ConfigYml.toString());
                        writer.close();
                    }
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

                    stop(jda, jedis);

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
                     (event.getOption("選択肢1") != null ? Objects.requireNonNull(event.getOption("選択肢1")).getAsString() : null),
                     (event.getOption("選択肢2") != null ? Objects.requireNonNull(event.getOption("選択肢2")).getAsString() : null),
                     (event.getOption("選択肢3") != null ? Objects.requireNonNull(event.getOption("選択肢3")).getAsString() : null),
                     (event.getOption("選択肢4") != null ? Objects.requireNonNull(event.getOption("選択肢4")).getAsString() : null),
                     (event.getOption("選択肢5") != null ? Objects.requireNonNull(event.getOption("選択肢5")).getAsString() : null),
                     (event.getOption("選択肢6") != null ? Objects.requireNonNull(event.getOption("選択肢6")).getAsString() : null),
                     (event.getOption("選択肢7") != null ? Objects.requireNonNull(event.getOption("選択肢7")).getAsString() : null),
                     (event.getOption("選択肢8") != null ? Objects.requireNonNull(event.getOption("選択肢8")).getAsString() : null),
                     (event.getOption("選択肢9") != null ?  Objects.requireNonNull(event.getOption("選択肢9")).getAsString(): null),
                     (event.getOption("選択肢10") != null ? Objects.requireNonNull(event.getOption("選択肢10")).getAsString() : null),
                     (event.getOption("選択肢11") != null ? Objects.requireNonNull(event.getOption("選択肢11")).getAsString() : null),
                     (event.getOption("選択肢12") != null ? Objects.requireNonNull(event.getOption("選択肢12")).getAsString() : null),
                     (event.getOption("選択肢13") != null ? Objects.requireNonNull(event.getOption("選択肢13")).getAsString() : null),
                     (event.getOption("選択肢14") != null ? Objects.requireNonNull(event.getOption("選択肢14")).getAsString() : null),
                     (event.getOption("選択肢15") != null ? Objects.requireNonNull(event.getOption("選択肢15")).getAsString() : null),
                     (event.getOption("選択肢16") != null ? Objects.requireNonNull(event.getOption("選択肢16")).getAsString() : null),
                     (event.getOption("選択肢17") != null ? Objects.requireNonNull(event.getOption("選択肢17")).getAsString() : null),
                     (event.getOption("選択肢18") != null ? Objects.requireNonNull(event.getOption("選択肢18")).getAsString() : null),
                     (event.getOption("選択肢19") != null ? Objects.requireNonNull(event.getOption("選択肢19")).getAsString() : null),
                     (event.getOption("選択肢20") != null ? Objects.requireNonNull(event.getOption("選択肢20")).getAsString() : null)
             };

            contents = new VoteContents(Objects.requireNonNull(event.getGuild()).getId(), event.getMessageChannel().getId(), Objects.requireNonNull(event.getOption("タイトル")).getAsString(), vote, Objects.requireNonNull(event.getOption("投票終了日時")).getAsString().replaceAll("なし","9999-12-31 23:59:59"), event.getOption("投票形式") != null ? Objects.requireNonNull(event.getOption("投票形式")).getAsString() : "default");

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
                "タイトル : "+ Objects.requireNonNull(event.getOption("タイトル")).getAsString()+"\n" +
                "終了日付 : "+ Objects.requireNonNull(event.getOption("投票終了日時")).getAsString() + "\n" +
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
        if (event.getMember() != null && (event.getMember().getUser().isBot() || event.getMember().getUser().isSystem())){
            return;
        }

        JedisPool pool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
        Jedis jedis = pool.getResource();
        jedis.auth(ConfigYml.string("RedisPass"));

        boolean isFound = false;

        for (String key : jedis.keys("nanamibot:vote:contents:*")) {
            if (jedis.get(key).equals(event.getMessageId())){
                //System.out.println("found");
                isFound = true;
            }
        }

        if (!isFound){
            //System.out.println("not found");
            jedis.close();
            pool.close();
            return;
        }

        jedis.close();
        pool.close();


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
            event.getReaction().removeReaction(Objects.requireNonNull(event.getUser())).queue();
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
            JedisPool pool1 = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
            Jedis jedis1 = pool1.getResource();
            jedis1.auth(ConfigYml.string("RedisPass"));

            for (String key : jedis1.keys("nanamibot:vote:contents:*")) {
                if (!jedis1.get(key).equals(event.getMessageId())){
                    //System.out.println(jedis.get(key) + " / " + event.getMessageId());
                    continue;
                }
                String[] split = key.split(":");

                PersonalResult result = new PersonalResult(event.getEmoji().getAsReactionCode(), event.getMember().getUser().getId(), event.getMember().getUser().getAsTag(), event.getMember().getUser().getName(), event.getMember().getNickname(), true);
                jedis1.set("nanamibot:vote:result:"+new Date().getTime()+":"+split[split.length - 1], new Gson().toJson(result));
                return;
            }

            jedis1.close();
            pool1.close();
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
            System.arraycopy(temp, 0, temp2, 0, temp2.length);
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

    public void stop(JDA jda, Jedis jedis){

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
                                if (personalResult.getNickname() == null && personalResult.getNickname() == null){
                                    voteNameList.append("(不明)");
                                } else {
                                    voteNameList.append(personalResult.getNickname() == null ? personalResult.getUsername() : personalResult.getNickname());
                                }

                            } else {
                                if (personalResult.getNickname() == null && personalResult.getNickname() == null){
                                    voteNameList.append("\n").append("(不明)");
                                } else {
                                    voteNameList.append("\n").append(personalResult.getNickname() == null ? personalResult.getUsername() : personalResult.getNickname());
                                }

                            }

                        }
                    }

                    builder.addField(Emoji.fromUnicode(reactionList[i]).getFormatted() + " " + name + "("+count+" 票)", voteNameList.toString(), false);
                    i++;
                }

                Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(contents.getGuildId())).getTextChannelById(contents.getMessageChannelId())).getHistoryAfter(MessageId, 1).queue(messageHistory -> {
                    if (messageHistory.getMessageById(MessageId) != null && Objects.requireNonNull(messageHistory.getMessageById(MessageId)).getReactions().size() > 0){
                        Objects.requireNonNull(messageHistory.getMessageById(MessageId)).clearReactions().queue();
                    }

                });

                if (!builder.isValidLength()){
                    StringBuffer buffer = new StringBuffer();

                    for (String name : result.getVoteResult()) {
                        int count = 0;

                        StringBuilder buffer1 = new StringBuilder();

                        for (PersonalResult personalResult : result.getPersonalResults()) {
                            count++;

                            if (buffer1.length() == 0){
                                if (personalResult.getNickname() == null && personalResult.getNickname() == null){
                                    buffer1.append("(不明)");
                                } else {
                                    buffer1.append(personalResult.getNickname() == null ? personalResult.getUsername() : personalResult.getNickname());
                                }

                            } else {
                                if (personalResult.getNickname() == null && personalResult.getNickname() == null){
                                    buffer1.append("\r\n").append("(不明)");
                                } else {
                                    buffer1.append("\r\n").append(personalResult.getNickname() == null ? personalResult.getUsername() : personalResult.getNickname());
                                }

                            }

                        }

                        buffer.append(name).append(" (").append(count).append(" 票)\r\n");
                        buffer.append(buffer1).append("\r\n");
                    }

                    EmbedBuilder builder2 = new EmbedBuilder();
                    builder2.setTitle("ななみちゃんbot 投票機能");
                    builder2.setColor(Color.PINK);
                    builder2.clearFields();
                    builder2.setDescription("「"+contents.getTitle()+"」の投票が投票終了しました！\n結果は以下のとおりです！");
                    VoteResult result2 = check(MessageId);
                    builder2.addField("投票総数", result.getTotalCount()+" 票", false);
                    if (contents.getVoteType().equals("only")){
                        if (result2.getValidityCount() > 0){
                            builder.addField("有効投票総数", result.getValidityCount()+" 票 ("+((result.getValidityCount()) / result.getTotalCount() * 100)+" %)", false);
                        } else {
                            builder.addField("有効投票総数", result.getValidityCount()+" 票 (0 %)", false);
                        }
                    }

                    File file = new File("./vote.txt");
                    try {
                        if (file.createNewFile()){
                            PrintWriter writer = new PrintWriter(file);
                            writer.print(buffer);
                            writer.close();

                            Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(contents.getGuildId())).getTextChannelById(contents.getMessageChannelId())).sendMessageEmbeds(builder2.build()).setFiles(FileUpload.fromData(file)).queue();
                            if (!file.delete()){
                                throw new IOException();
                            }
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } else {
                    Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(contents.getGuildId())).getTextChannelById(contents.getMessageChannelId())).sendMessageEmbeds(builder.build()).queue();
                }



                contents.setEndFlag(true);
                jedis.set("nanamibot:vote:data:" + voteId, new Gson().toJson(contents));
            }
        }

    }

}
