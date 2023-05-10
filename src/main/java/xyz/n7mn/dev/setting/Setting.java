package xyz.n7mn.dev.setting;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class Setting {

    private JDA jda;
    private final EmbedBuilder builder = new EmbedBuilder();
    private YamlMapping MySQLConfigYaml = null;
    private YamlMapping RedisConfigYaml = null;

    public Setting(JDA jda){
        this.jda = jda;

        File config = new File("./config.yml");
        try {
            MySQLConfigYaml = Yaml.createYamlInput(config).readYamlMapping();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File config2 = new File("./config-redis.yml");
        try {
            RedisConfigYaml = Yaml.createYamlInput(config2).readYamlMapping();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run(SlashCommandInteractionEvent event){
        builder.setTitle("ななみちゃんbot 設定");
        builder.setDescription("※一部設定は管理者または指定された人のみ変更できます。");

        if (event.getOption("設定項目") == null && event.getOption("チャンネル") == null){
            Connection con = null;
            String json = null;
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + MySQLConfigYaml.string("MySQLServerAddress") + ":" + MySQLConfigYaml.integer("MySQLServerPort") + "/" + MySQLConfigYaml.string("MySQLServerDatabase") + MySQLConfigYaml.string("MySQLServerOption"), MySQLConfigYaml.string("MySQLServerUsername"), MySQLConfigYaml.string("MySQLServerPassword"));

                PreparedStatement statement = con.prepareStatement("SELECT * FROM SettingTable WHERE GuildID = ? and Active = 1");

                statement.setString(1, event.getGuild().getId());
                ResultSet set = statement.executeQuery();

                if (set.next()){
                    json = set.getString("JSON");
                }
                set.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null && !con.isClosed()){
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            SettingJson data = new Gson().fromJson(json, SettingJson.class);
            if (json == null){
                data = new SettingJson(false, "", false, "");
                JedisPool pool = new JedisPool(RedisConfigYaml.string("RedisServer"), RedisConfigYaml.integer("RedisPort"));
                Jedis jedis = pool.getResource();
                jedis.auth(RedisConfigYaml.string("RedisPass"));

                String redis_eew = jedis.get("nanamibot:eew:" + event.getGuild().getId());
                //System.out.println(redis_eew);
                if (redis_eew != null && !redis_eew.isEmpty()){
                    data.setEEW(true);
                    data.setEEWSendChannel(redis_eew);
                }
                String redis_jisin = jedis.get("nanamibot:jisin:" + event.getGuild().getId());
                if (redis_jisin != null && !redis_jisin.isEmpty()){
                    data.setEarthquake(true);
                    data.setEarthquakeSendChannel(redis_jisin);
                }

                jedis.close();
                pool.close();

                if (redis_eew != null || redis_jisin != null){
                    SettingJson finalData = data;
                    new Thread(()->{
                        Connection con1 = null;
                        try {
                            con1 = DriverManager.getConnection("jdbc:mysql://" + MySQLConfigYaml.string("MySQLServerAddress") + ":" + MySQLConfigYaml.integer("MySQLServerPort") + "/" + MySQLConfigYaml.string("MySQLServerDatabase") + MySQLConfigYaml.string("MySQLServerOption"), MySQLConfigYaml.string("MySQLServerUsername"), MySQLConfigYaml.string("MySQLServerPassword"));
                            con1.setAutoCommit(true);

                            PreparedStatement statement = con1.prepareStatement("INSERT INTO `SettingTable`(`ID`, `GuildID`, `JSON`, `Active`) VALUES (?, ?, ?, ?)");
                            statement.setString(1, UUID.randomUUID().toString());
                            statement.setString(2, event.getGuild().getId());
                            statement.setString(3, new Gson().toJson(finalData));
                            statement.setBoolean(4, true);
                            statement.execute();
                            statement.close();

                            con1.close();
                        } catch (SQLException ex){
                            ex.printStackTrace();
                        } finally {
                            try {
                                if (con1 != null && !con1.isClosed()){
                                    con1.close();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }

            //System.out.println(data.getEEWSendChannel());

            TextChannel mention1 = data.isEEW() ? event.getGuild().getTextChannelById(data.getEEWSendChannel()) : null;
            TextChannel mention2 = data.isEarthquake() ? event.getGuild().getTextChannelById(data.getEarthquakeSendChannel()) : null;
            builder.setColor(Color.PINK);

            builder.addField("緊急地震速報", (data.isEEW() ? "設定済み (" + (mention1 != null ? mention1.getAsMention() : "不明") + ")" : "未設定"), false);
            builder.addField("地震情報", (data.isEarthquake() ? "設定済み (" + (mention2 != null ? mention2.getAsMention() : "不明") + ")" : "未設定"), false);

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        if (!event.getMember().isOwner()){
            builder.setColor(Color.RED);
            builder.setDescription("現在 設定できません。");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        if (!event.getOption("設定項目").getAsString().equals("eew") && !event.getOption("設定項目").getAsString().equals("earthquake")){
            builder.setColor(Color.RED);
            builder.setDescription("現在 設定できません。");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        if (event.getOption("設定項目") != null && event.getOption("チャンネル") == null){
            new Thread(()->{
                JedisPool pool = new JedisPool(RedisConfigYaml.string("RedisServer"), RedisConfigYaml.integer("RedisPort"));
                Jedis jedis = pool.getResource();
                jedis.auth(RedisConfigYaml.string("RedisPass"));
                String keyName = "nanamibot:"+event.getOption("設定項目").getAsString()+":"+event.getGuild().getId();
                if (jedis.get(keyName) != null){
                    jedis.del(keyName);
                }

                jedis.close();
                pool.close();
            }).start();

            Connection con = null;
            String json = null;
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + MySQLConfigYaml.string("MySQLServerAddress") + ":" + MySQLConfigYaml.integer("MySQLServerPort") + "/" + MySQLConfigYaml.string("MySQLServerDatabase") + MySQLConfigYaml.string("MySQLServerOption"), MySQLConfigYaml.string("MySQLServerUsername"), MySQLConfigYaml.string("MySQLServerPassword"));
                con.setAutoCommit(true);

                PreparedStatement statement = con.prepareStatement("SELECT * FROM SettingTable WHERE GuildID = ? and Active = 1");

                statement.setString(1, event.getGuild().getId());
                ResultSet set = statement.executeQuery();

                if (set.next()){
                    json = set.getString("JSON");
                }
                set.close();
            } catch (SQLException ex){
                ex.printStackTrace();
                try {
                    if (con != null && !con.isClosed()){
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            SettingJson data = json != null ? new Gson().fromJson(json, SettingJson.class) : new SettingJson(event.getOption("設定項目").getAsString().equals("eew"), (event.getOption("設定項目").getAsString().equals("eew") ? event.getOption("チャンネル").getAsChannel().getId() : ""), event.getOption("設定項目").getAsString().equals("earthquake"), (event.getOption("設定項目").getAsString().equals("earthquake") ? event.getOption("チャンネル").getAsChannel().getId() : ""));
            if (event.getOption("設定項目").getAsString().equals("eew")){
                data.setEEWSendChannel("");
                data.setEEW(false);
            } else {
                data.setEarthquakeSendChannel("");
                data.setEarthquake(false);
            }

            try {
                PreparedStatement statement1 = con.prepareStatement("UPDATE `SettingTable` SET `Active` = ? WHERE GuildId = ? AND Active = 1");
                statement1.setBoolean(1, false);
                statement1.setString(2, event.getGuild().getId());
                statement1.execute();
                statement1.close();

                PreparedStatement statement2 = con.prepareStatement("INSERT INTO `SettingTable`(`ID`, `GuildID`, `JSON`, `Active`) VALUES (?, ?, ?, ?)");
                statement2.setString(1, UUID.randomUUID().toString());
                statement2.setString(2, event.getGuild().getId());
                statement2.setString(3, new Gson().toJson(data));
                statement2.setBoolean(4, true);
                statement2.execute();
                statement2.close();

                con.close();
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                try {
                    if (con != null && !con.isClosed()){
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            builder.setColor(Color.GREEN);
            builder.setDescription("チャンネル設定を解除しました。");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();

            return;
        }

        if (event.getOption("チャンネル").getAsChannel().getType() != ChannelType.TEXT){
            builder.setColor(Color.RED);
            builder.setDescription("テキストチャンネルを指定してください。");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        TextChannel channel = event.getOption("チャンネル").getAsChannel().asTextChannel();
        if (!channel.canTalk()){
            builder.setColor(Color.RED);
            builder.setDescription("このテキストチャンネルには書き込めないようです...");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        Connection con = null;
        String json = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + MySQLConfigYaml.string("MySQLServerAddress") + ":" + MySQLConfigYaml.integer("MySQLServerPort") + "/" + MySQLConfigYaml.string("MySQLServerDatabase") + MySQLConfigYaml.string("MySQLServerOption"), MySQLConfigYaml.string("MySQLServerUsername"), MySQLConfigYaml.string("MySQLServerPassword"));
            con.setAutoCommit(true);

            PreparedStatement statement = con.prepareStatement("SELECT * FROM SettingTable WHERE GuildID = ? and Active = 1");

            statement.setString(1, event.getGuild().getId());
            ResultSet set = statement.executeQuery();

            if (set.next()){
                json = set.getString("JSON");
            }
            set.close();
            con.close();

        } catch (SQLException ex){
            ex.printStackTrace();
        } finally {
            try {
                if (con != null && !con.isClosed()){
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        SettingJson data = json != null ? new Gson().fromJson(json, SettingJson.class) : new SettingJson(event.getOption("設定項目").getAsString().equals("eew"), (event.getOption("設定項目").getAsString().equals("eew") ? event.getOption("チャンネル").getAsChannel().getId() : ""), event.getOption("設定項目").getAsString().equals("earthquake"), (event.getOption("設定項目").getAsString().equals("earthquake") ? event.getOption("チャンネル").getAsChannel().getId() : ""));

        new Thread(()->{
            JedisPool pool = new JedisPool(RedisConfigYaml.string("RedisServer"), RedisConfigYaml.integer("RedisPort"));
            Jedis jedis = pool.getResource();
            jedis.auth(RedisConfigYaml.string("RedisPass"));
            String keyName = "nanamibot:"+event.getOption("設定項目").getAsString()+":"+event.getGuild().getId();
            if (jedis.get(keyName) != null){
                jedis.del(keyName);
            }

            jedis.set(keyName, channel.getId());

            jedis.close();
            pool.close();
        }).start();

        new Thread(()->{
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://" + MySQLConfigYaml.string("MySQLServerAddress") + ":" + MySQLConfigYaml.integer("MySQLServerPort") + "/" + MySQLConfigYaml.string("MySQLServerDatabase") + MySQLConfigYaml.string("MySQLServerOption"), MySQLConfigYaml.string("MySQLServerUsername"), MySQLConfigYaml.string("MySQLServerPassword"));
                connection.setAutoCommit(true);

                PreparedStatement statement = connection.prepareStatement("INSERT INTO `SettingTable`(`ID`, `GuildID`, `JSON`, `Active`) VALUES (?, ?, ?, ?)");
                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, event.getGuild().getId());
                statement.setString(3, new Gson().toJson(data));
                statement.setBoolean(4, true);
                statement.execute();
                statement.close();

                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }).start();

        builder.setColor(Color.GREEN);
        builder.setDescription((event.getOption("設定項目").getAsString().equals("eew") ? "緊急地震速報" : "地震情報")+"の設定が完了しました。");
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();

    }

}
