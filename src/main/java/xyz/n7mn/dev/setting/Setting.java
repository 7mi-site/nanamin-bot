package xyz.n7mn.dev.setting;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;
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
        builder.setDescription("※設定は管理者または指定された人のみ変更できます。");

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

        SettingJson data;
        if (json != null){
            data = new Gson().fromJson(json, SettingJson.class);
        } else {
            data = new SettingJson(false, "", false, "", new String[]{""});;
        }

        /* 確認 */
        if (event.getOption("設定項目") == null && event.getOption("チャンネル") == null){
            if (json == null){
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

            StringBuffer buffer = new StringBuffer();
            if (data.getSettingOKRoleList() != null){

                for (String roleId : data.getSettingOKRoleList()){
                    if (!roleId.equals("") && event.getGuild().getRoleById(roleId) != null){
                        buffer.append(event.getGuild().getRoleById(roleId).getAsMention());
                    }
                }

            } else {
                data.setSettingOKRoleList(new String[]{""});
            }

            builder.addField("設定変更できるロール (設定項目: OKRole ※ここのオーナーのみ)", (data.getSettingOKRoleList()[0].length() != 0 ? "設定済み ("+buffer+")" : "未設定"), false);
            // builder.addField("入室ログ (設定項目: join)", "", false);
            // builder.addField("退室ログ (設定項目: leave)", "", false);
            builder.addField("緊急地震速報 (設定項目: eew)", (data.isEEW() ? "設定済み (" + (mention1 != null ? mention1.getAsMention() : "不明") + ")" : "未設定"), false);
            builder.addField("地震情報 (設定項目: earthquake)", (data.isEarthquake() ? "設定済み (" + (mention2 != null ? mention2.getAsMention() : "不明") + ")" : "未設定"), false);

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            return;
        }

        // 設定できるかどうかの権限チェック
        boolean isRole = false;

        if (data != null && !event.getOption("設定項目").getAsString().equals("OKRole")){
            List<Role> roles = event.getGuild().getRoles();

            for (Role role : roles){
                for (String id : data.getSettingOKRoleList()){
                    if (role.getId().equals(id)){
                        isRole = true;
                        break;
                    }
                }
                if (isRole){
                    break;
                }
            }
        }

        // ロール設定のときはロール持ちには設定させない (事故防止)
        if (event.getOption("設定項目").getAsString().equals("OKRole")){
            isRole = false;
        }

        if (!isRole && !event.getMember().isOwner()){
            builder.setColor(Color.RED);
            builder.setDescription("設定権限がありません。");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        if (!event.getOption("設定項目").getAsString().equals("eew") && !event.getOption("設定項目").getAsString().equals("earthquake") && !event.getOption("設定項目").getAsString().equals("OKRole")){
            builder.setColor(Color.RED);
            builder.setDescription("現在 設定できません。");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        // ロール設定
        if (event.getOption("設定項目").getAsString().equals("OKRole")){
            boolean isAdd = true;

            if (event.getOption("ロール") == null){
                builder.setColor(Color.RED);
                builder.setDescription("ロールを指定してください。");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            if (data.getSettingOKRoleList() != null && !data.getSettingOKRoleList()[0].equals("")){
                for (String s : data.getSettingOKRoleList()) {
                    if (event.getOption("ロール").getAsRole().getId().equals(s)){
                        isAdd = false;
                        break;
                    }
                }

            }
            String[] NewList = null;
            if (isAdd){
                if (data.getSettingOKRoleList()[0].equals("")){
                    data.setSettingOKRoleList(new String[]{event.getOption("ロール").getAsRole().getId()});
                } else {
                    NewList = new String[data.getSettingOKRoleList().length + 1];
                    for (int i = 0; i < data.getSettingOKRoleList().length; i++){
                        NewList[i] = data.getSettingOKRoleList()[i];
                    }
                    NewList[NewList.length - 1] = event.getOption("ロール").getAsRole().getId();
                }
            } else {
                NewList = new String[data.getSettingOKRoleList().length - 1];

                for (int i = 0; i < data.getSettingOKRoleList().length; i++){
                    if (data.getSettingOKRoleList()[i].equals(event.getOption("ロール").getAsRole().getId())){
                        continue;
                    }
                    NewList[i] = data.getSettingOKRoleList()[i];
                }
            }

            Connection con1 = null;
            try {
                con1 = DriverManager.getConnection("jdbc:mysql://" + MySQLConfigYaml.string("MySQLServerAddress") + ":" + MySQLConfigYaml.integer("MySQLServerPort") + "/" + MySQLConfigYaml.string("MySQLServerDatabase") + MySQLConfigYaml.string("MySQLServerOption"), MySQLConfigYaml.string("MySQLServerUsername"), MySQLConfigYaml.string("MySQLServerPassword"));
                con1.setAutoCommit(true);

                PreparedStatement statement1 = con1.prepareStatement("UPDATE `SettingTable` SET `Active`= ? WHERE `GuildID` = ?");
                statement1.setBoolean(1, false);
                statement1.setString(2, event.getGuild().getId());
                statement1.execute();
                statement1.close();

                PreparedStatement statement2 = con1.prepareStatement("INSERT INTO `SettingTable`(`ID`, `GuildID`, `JSON`, `Active`) VALUES (?, ?, ?, ?)");
                statement2.setString(1, UUID.randomUUID().toString());
                statement2.setString(2, event.getGuild().getId());
                statement2.setString(3, new Gson().toJson(data));
                statement2.setBoolean(4, true);
                statement2.execute();
                statement2.close();

                con1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con1 != null){
                        con1.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            builder.setColor(Color.GREEN);
            if (!isAdd){
                builder.setDescription("設定可能ロールから"+event.getOption("ロール").getAsRole().getAsMention()+"を解除しました。");
            } else {
                builder.setDescription("設定可能ロールに"+event.getOption("ロール").getAsRole().getAsMention()+"を追加しました。");
            }

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        // 地震設定
        if (event.getOption("設定項目") != null && (event.getOption("設定項目").getAsString().equals("eew") || event.getOption("設定項目").getAsString().equals("earthquake"))){

            // 1つ目が追加/変更 , 2つ目が削除
            boolean[] setting = new boolean[]{false, false};
            boolean isEdit = data != null;

            if (event.getOption("チャンネル") != null){
                setting[0] = true;

                if (event.getOption("チャンネル").getAsChannel().getType() != ChannelType.TEXT){
                    builder.setColor(Color.RED);
                    builder.setDescription("テキストチャンネルを指定してください。");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                if (!event.getOption("チャンネル").getAsChannel().asTextChannel().canTalk()){
                    builder.setColor(Color.RED);
                    builder.setDescription("このテキストチャンネルには書き込めないようです...");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

            }

            JedisPool pool = new JedisPool(RedisConfigYaml.string("RedisServer"), RedisConfigYaml.integer("RedisPort"));
            Jedis jedis = pool.getResource();
            jedis.auth(RedisConfigYaml.string("RedisPass"));
            String keyName = "nanamibot:"+event.getOption("設定項目").getAsString()+":"+event.getGuild().getId();

            // 設定解除
            if (!setting[0]){
                setting[1] = true;

                if (jedis.get(keyName) != null){
                    jedis.del(keyName);
                }
            } else {
                jedis.set(keyName, event.getOption("チャンネル").getAsChannel().getId());
            }
            jedis.close();
            pool.close();

            if (setting[1]){
                if (event.getOption("設定項目").getAsString().equals("eew")){
                    data.setEEW(false);
                    data.setEEWSendChannel("");
                } else {
                    data.setEarthquake(false);
                    data.setEarthquakeSendChannel("");
                }
            } else {
                if (event.getOption("設定項目").getAsString().equals("eew")){
                    data.setEEW(event.getOption("設定項目").getAsString().equals("eew"));
                    data.setEEWSendChannel(event.getOption("設定項目").getAsString().equals("eew") ? event.getOption("チャンネル").getAsChannel().getId() : "");
                } else {
                    data.setEarthquake(event.getOption("設定項目").getAsString().equals("earthquake"));
                    data.setEarthquakeSendChannel(event.getOption("設定項目").getAsString().equals("earthquake") ? event.getOption("チャンネル").getAsChannel().getId() : "");
                }
            }

            Connection con1 = null;
            try {
                con1 = DriverManager.getConnection("jdbc:mysql://" + MySQLConfigYaml.string("MySQLServerAddress") + ":" + MySQLConfigYaml.integer("MySQLServerPort") + "/" + MySQLConfigYaml.string("MySQLServerDatabase") + MySQLConfigYaml.string("MySQLServerOption"), MySQLConfigYaml.string("MySQLServerUsername"), MySQLConfigYaml.string("MySQLServerPassword"));
                con1.setAutoCommit(true);

                PreparedStatement statement1 = con1.prepareStatement("UPDATE `SettingTable` SET `Active`= ? WHERE `GuildID` = ?");
                statement1.setBoolean(1, false);
                statement1.setString(2, event.getGuild().getId());
                statement1.execute();
                statement1.close();

                PreparedStatement statement2 = con1.prepareStatement("INSERT INTO `SettingTable`(`ID`, `GuildID`, `JSON`, `Active`) VALUES (?, ?, ?, ?)");
                statement2.setString(1, UUID.randomUUID().toString());
                statement2.setString(2, event.getGuild().getId());
                statement2.setString(3, new Gson().toJson(data));
                statement2.setBoolean(4, true);
                statement2.execute();
                statement2.close();

                con1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con1 != null){
                        con1.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            builder.setColor(Color.GREEN);
            if (setting[1]){
                builder.setDescription("チャンネル設定を解除しました。");
            } else {
                if (isEdit){
                    builder.setDescription("チャンネル設定を更新しました。");
                } else {
                    builder.setDescription("チャンネル設定を追加しました。");
                }

                builder.addField(event.getOption("設定項目").getAsString().equals("eew") ? "緊急地震速報" : "地震情報", event.getOption("チャンネル").getAsChannel().getAsMention(), false);
            }

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();

        }

    }

}
