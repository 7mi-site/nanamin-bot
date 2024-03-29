package xyz.n7mn.dev.setting;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
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
import java.util.Objects;
import java.util.UUID;

public class Setting {

    private final EmbedBuilder builder = new EmbedBuilder();
    private YamlMapping MySQLConfigYaml = null;
    private YamlMapping RedisConfigYaml = null;

    public Setting(){

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

            statement.setString(1, Objects.requireNonNull(event.getGuild()).getId());
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
            data = new SettingJson(false, "", false, "", new String[]{""}, false, "", false, "");
        }

        /* 確認 */
        if (event.getOption("設定項目") == null && event.getOption("チャンネル") == null){
            if (json == null){
                JedisPool pool = new JedisPool(RedisConfigYaml.string("RedisServer"), RedisConfigYaml.integer("RedisPort"));
                Jedis jedis = pool.getResource();
                jedis.auth(RedisConfigYaml.string("RedisPass"));

                String redis_eew = null;
                String redis_jisin = null;
                String redis_join;
                String redis_leave;

                if (event.getGuild() != null){
                    redis_eew = jedis.get("nanamibot:eew:" + event.getGuild().getId());
                    //System.out.println(redis_eew);
                    if (redis_eew != null && !redis_eew.isEmpty()){
                        data.setEEW(true);
                        data.setEEWSendChannel(redis_eew);
                    }
                    redis_jisin = jedis.get("nanamibot:jisin:" + event.getGuild().getId());
                    if (redis_jisin != null && !redis_jisin.isEmpty()){
                        data.setEarthquake(true);
                        data.setEarthquakeSendChannel(redis_jisin);
                    }

                    redis_join = jedis.get("nanamibot:join:" + event.getGuild().getId());
                    if (redis_join != null && !redis_join.isEmpty()){
                        data.setJoin(true);
                        data.setJoinSendChannel(redis_join);
                    }
                    redis_leave = jedis.get("nanamibot:leave:" + event.getGuild().getId());
                    if (redis_leave != null && !redis_leave.isEmpty()){
                        data.setLeave(true);
                        data.setLeaveSendChannel(redis_leave);
                    }
                }

                jedis.close();
                pool.close();

                if (redis_eew != null || redis_jisin != null){
                    new Thread(()->{
                        Connection con1 = null;
                        try {
                            con1 = DriverManager.getConnection("jdbc:mysql://" + MySQLConfigYaml.string("MySQLServerAddress") + ":" + MySQLConfigYaml.integer("MySQLServerPort") + "/" + MySQLConfigYaml.string("MySQLServerDatabase") + MySQLConfigYaml.string("MySQLServerOption"), MySQLConfigYaml.string("MySQLServerUsername"), MySQLConfigYaml.string("MySQLServerPassword"));
                            con1.setAutoCommit(true);

                            PreparedStatement statement = con1.prepareStatement("INSERT INTO `SettingTable`(`ID`, `GuildID`, `JSON`, `Active`) VALUES (?, ?, ?, ?)");
                            statement.setString(1, UUID.randomUUID().toString());
                            statement.setString(2, event.getGuild().getId());
                            statement.setString(3, new Gson().toJson(data));
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
            TextChannel mention3 = data.isJoin() ? event.getGuild().getTextChannelById(data.getJoinSendChannel()) : null;
            TextChannel mention4 = data.isLeave() ? event.getGuild().getTextChannelById(data.getLeaveSendChannel()) : null;
            builder.setColor(Color.PINK);

            StringBuilder buffer = new StringBuilder();
            if (data.getSettingOKRoleList() != null){

                for (String roleId : data.getSettingOKRoleList()){
                    if (!roleId.equals("") && event.getGuild().getRoleById(roleId) != null){
                        buffer.append(Objects.requireNonNull(event.getGuild().getRoleById(roleId)).getAsMention());
                    }
                }

            } else {
                data.setSettingOKRoleList(new String[]{""});
            }

            builder.addField("設定変更できるロール (設定項目: OKRole ※ここのオーナーのみ)", (data.getSettingOKRoleList()[0].length() != 0 ? "設定済み ("+buffer+")" : "未設定"), false);
            builder.addField("入室通知 (設定項目: join)", (data.isJoin() ? "設定済み ("+ Objects.requireNonNull(mention3).getAsMention()+")" : "未設定"), false);
            builder.addField("退室通知 (設定項目: leave)", (data.isLeave() ? "設定済み ("+ Objects.requireNonNull(mention4).getAsMention()+")" : "未設定"), false);
            builder.addField("緊急地震速報 (設定項目: eew)", (data.isEEW() ? "設定済み (" + (mention1 != null ? mention1.getAsMention() : "不明") + ")" : "未設定"), false);
            builder.addField("地震情報 (設定項目: earthquake)", (data.isEarthquake() ? "設定済み (" + (mention2 != null ? mention2.getAsMention() : "不明") + ")" : "未設定"), false);

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
            return;
        }

        // 設定できるかどうかの権限チェック
        boolean isRole = false;

        if (data != null && !Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("OKRole")){
            List<Role> roles = event.getGuild() != null ? event.getGuild().getRoles() : null;

            if (roles != null){
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
        }

        // ロール設定のときはロール持ちには設定させない (事故防止)
        if (Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("OKRole")){
            isRole = false;
        }

        if (!isRole && !Objects.requireNonNull(event.getMember()).isOwner()){
            builder.setColor(Color.RED);
            builder.setDescription("設定権限がありません。");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        if (!Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("eew") && !Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("earthquake") && !Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("OKRole") && !Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("join") && !Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("leave")){
            builder.setColor(Color.RED);
            builder.setDescription("現在 設定できません。");
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        // ロール設定
        if (Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("OKRole")){
            boolean isAdd = true;

            if (event.getOption("ロール") == null){
                builder.setColor(Color.RED);
                builder.setDescription("ロールを指定してください。");
                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            if (data != null && data.getSettingOKRoleList() != null && !data.getSettingOKRoleList()[0].equals("")){
                for (String s : data.getSettingOKRoleList()) {
                    if (Objects.requireNonNull(event.getOption("ロール")).getAsRole().getId().equals(s)){
                        isAdd = false;
                        break;
                    }
                }

            }

            if (isAdd){
                if (data != null && data.getSettingOKRoleList()[0].equals("")){
                    data.setSettingOKRoleList(new String[]{Objects.requireNonNull(event.getOption("ロール")).getAsRole().getId()});
                }
            }

            Connection con1 = null;
            try {
                con1 = DriverManager.getConnection("jdbc:mysql://" + MySQLConfigYaml.string("MySQLServerAddress") + ":" + MySQLConfigYaml.integer("MySQLServerPort") + "/" + MySQLConfigYaml.string("MySQLServerDatabase") + MySQLConfigYaml.string("MySQLServerOption"), MySQLConfigYaml.string("MySQLServerUsername"), MySQLConfigYaml.string("MySQLServerPassword"));
                con1.setAutoCommit(true);

                PreparedStatement statement1 = con1.prepareStatement("UPDATE `SettingTable` SET `Active`= ? WHERE `GuildID` = ?");
                statement1.setBoolean(1, false);
                statement1.setString(2, Objects.requireNonNull(event.getGuild()).getId());
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
                builder.setDescription("設定可能ロールから"+ Objects.requireNonNull(event.getOption("ロール")).getAsRole().getAsMention() + "を解除しました。");
            } else {
                builder.setDescription("設定可能ロールに" + Objects.requireNonNull(event.getOption("ロール")).getAsRole().getAsMention() + "を追加しました。");
            }

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        // 入室ログ・退出ログ
        if (event.getOption("設定項目") != null && (Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("join") || Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("leave"))){

            JedisPool pool = new JedisPool(RedisConfigYaml.string("RedisServer"), RedisConfigYaml.integer("RedisPort"));
            Jedis jedis = pool.getResource();
            jedis.auth(RedisConfigYaml.string("RedisPass"));

            boolean isAdd = true;
            if (event.getOption("チャンネル") != null){
                if (Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().getType() != ChannelType.TEXT){
                    builder.setColor(Color.RED);
                    builder.setDescription("テキストチャンネルを指定してください。");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                if (!Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().asTextChannel().canTalk()){
                    builder.setColor(Color.RED);
                    builder.setDescription("このテキストチャンネルには書き込めないようです...");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                jedis.set("nanamibot:"+ Objects.requireNonNull(event.getOption("設定項目")).getAsString()+":"+ Objects.requireNonNull(event.getGuild()).getId(), Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().getId());
                if (Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("join")){
                    if (data != null){
                        data.setJoin(true);
                        data.setJoinSendChannel(Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().getId());
                    }
                } else {
                    if (data != null){
                        data.setLeave(true);
                        data.setLeaveSendChannel(Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().getId());
                    }
                }
            } else {
                isAdd = false;
                jedis.del("nanamibot:"+ Objects.requireNonNull(event.getOption("設定項目")).getAsString()+":"+ Objects.requireNonNull(event.getGuild()).getId());
                if (Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("join")){
                    if (data != null){
                        data.setJoin(false);
                        data.setJoinSendChannel("");
                    }
                } else {
                    if (data != null){
                        data.setLeave(false);
                        data.setLeaveSendChannel("");
                    }
                }
            }
            jedis.close();
            pool.close();

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
            builder.setDescription("チャンネル設定を"+(isAdd ? "更新" : "削除")+"しました。");
            builder.addField((Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("join") ? "入室" : "退室")+"通知", Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().getAsMention(), false);
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            return;
        }

        // 地震設定
        if (event.getOption("設定項目") != null && (Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("eew") || Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("earthquake"))){

            // 1つ目が追加/変更 , 2つ目が削除
            boolean[] setting = new boolean[]{false, false};
            boolean isEdit = data != null;

            if (event.getOption("チャンネル") != null){
                setting[0] = true;

                if (Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().getType() != ChannelType.TEXT){
                    builder.setColor(Color.RED);
                    builder.setDescription("テキストチャンネルを指定してください。");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                if (!Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().asTextChannel().canTalk()){
                    builder.setColor(Color.RED);
                    builder.setDescription("このテキストチャンネルには書き込めないようです...");
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

            }

            JedisPool pool = new JedisPool(RedisConfigYaml.string("RedisServer"), RedisConfigYaml.integer("RedisPort"));
            Jedis jedis = pool.getResource();
            jedis.auth(RedisConfigYaml.string("RedisPass"));
            String keyName = "nanamibot:"+(Objects.requireNonNull(event.getOption("設定項目")).getAsString().replaceAll("earthquake","jisin"))+":"+ Objects.requireNonNull(event.getGuild()).getId();

            // 設定解除
            if (!setting[0]){
                setting[1] = true;

                if (jedis.get(keyName) != null){
                    jedis.del(keyName);
                }
            } else {
                jedis.set(keyName, Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().getId());
            }
            jedis.close();
            pool.close();

            if (setting[1]){
                if (Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("eew")){
                    if (data != null){
                        data.setEEW(false);
                        data.setEEWSendChannel("");
                    }
                } else {
                    if (data != null){
                        data.setEarthquake(false);
                        data.setEarthquakeSendChannel("");
                    }
                }
            } else {
                if (Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("eew")){
                    if (data != null){
                        data.setEEW(Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("eew"));
                        data.setEEWSendChannel(Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("eew") ? Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().getId() : "");
                    }
                } else {
                    if (data != null){
                        data.setEarthquake(Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("earthquake"));
                        data.setEarthquakeSendChannel(Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("earthquake") ? Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().getId() : "");
                    }
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

                builder.addField(Objects.requireNonNull(event.getOption("設定項目")).getAsString().equals("eew") ? "緊急地震速報" : "地震情報", Objects.requireNonNull(event.getOption("チャンネル")).getAsChannel().getAsMention(), false);
            }

            event.replyEmbeds(builder.build()).setEphemeral(true).queue();

        }

    }

}
