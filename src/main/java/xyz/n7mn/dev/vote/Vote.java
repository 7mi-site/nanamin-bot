package xyz.n7mn.dev.vote;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.resps.ScanResult;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
                        if (new Date().getTime() >= contents.getEndDate().getTime()){
                            String MessageId = jedis.get(key);

                            builder.setTitle("ななみちゃんbot 投票機能");
                            builder.setColor(Color.PINK);
                            builder.clearFields();
                            builder.setDescription("「"+contents.getTitle()+"」の投票が投票終了しました！\n結果は以下のとおりです！");

                            jda.getGuildById(contents.getGuildId()).getTextChannelById(contents.getMessageChannelId()).getHistoryAfter(MessageId, 1).queue(messageHistory -> {
                                if (messageHistory.getMessageById(MessageId).getReactions() != null && messageHistory.getMessageById(MessageId).getReactions().size() > 0){
                                    messageHistory.getMessageById(MessageId).clearReactions().queue();
                                }

                            });
                            jda.getGuildById(contents.getGuildId()).getTextChannelById(contents.getMessageChannelId()).sendMessageEmbeds(builder.build()).queue();

                            jedis.del("nanamibot:vote:data:" + voteId);
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


            contents = new VoteContents(event.getGuild().getId(), event.getMessageChannel().getId(), event.getOption("タイトル").getAsString(), vote, event.getOption("投票終了日時").getAsString().replaceAll("なし","9999-12-31 23:59:59"), "default");

        } catch (ParseException e) {
            e.printStackTrace();
            builder.setColor(Color.RED);
            builder.setDescription("日付の形式が正しくありません。\n例: `2024-01-01 00:00:00'");

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            jedis.close();
            jedisPool.close();
            return;
        }

        jedis.set("nanamibot:vote:data:"+contents.getVoteID().toString(), new Gson().toJson(contents));
        jedis.close();

        jedisPool.close();

        builder.setDescription(
                "タイトル : "+event.getOption("タイトル").getAsString()+"\n" +
                "終了日付 : "+event.getOption("投票終了日時").getAsString()
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
                jedisPool = new JedisPool(ConfigYml.string("RedisServer"), ConfigYml.integer("RedisPort"));
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

}
