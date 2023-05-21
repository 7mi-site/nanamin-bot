package xyz.n7mn.dev;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import redis.clients.jedis.Protocol;
import xyz.n7mn.dev.api.ver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NanamiMain {

    public static void main(String[] args) {

        try {
            File config1 = new File("./config.yml");
            File config2 = new File("./config-redis.yml");

            YamlMapping ConfigYml1;
            YamlMapping ConfigYml2;
            if (!config1.exists()){
                YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
                ConfigYml1 = builder.add(
                        "DiscordToken", "xxx"
                ).add(
                        "MySQLServerAddress", "localhost"
                ).add(
                        "MySQLServerPort", "3306"
                ).add(
                        "MySQLServerDatabase","nana-db"
                ).add(
                        "MySQLServerOption","?allowPublicKeyRetrieval=true&useSSL=false"
                ).add(
                        "MySQLServerUsername","user"
                ).add(
                        "MySQLServerPassword","pass"
                ).build();

                try {
                    PrintWriter writer = new PrintWriter(config1);
                    writer.print(ConfigYml1.toString());
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                return;
            }
            ConfigYml1 = Yaml.createYamlInput(config1).readYamlMapping();

            if (!config2.exists()){
                config2.createNewFile();

                YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
                ConfigYml2 = builder.add(
                        "RedisServer", "127.0.0.1"
                ).add(
                        "RedisPort", String.valueOf(Protocol.DEFAULT_PORT)
                ).add(
                        "RedisPass", ""
                ).build();

                try {
                    PrintWriter writer = new PrintWriter(config2);
                    writer.print(ConfigYml2.toString());
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                return;
            }

            while (true){
                Socket socket = null;
                try {
                    socket =new Socket("www.google.co.jp", 80);
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write("HEAD / HTTP/1.1\n\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();

                    byte[] bytes = new byte[100000000];
                    int i = inputStream.read(bytes);
                    if (i >= 0){
                        bytes = Arrays.copyOf(bytes, i);
                    }
                    //System.out.println(new String(bytes, StandardCharsets.UTF_8));
                    outputStream.close();
                    inputStream.close();
                } catch (Exception e){
                    continue;
                } finally {
                    if (socket != null){
                        socket.close();
                    }
                }

                break;
            }

            String token = ConfigYml1.string("DiscordToken");

            //DriverManager.getConnection("jdbc:mysql://");

            JDA build = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(new EventListener())
                    .enableCache(CacheFlag.VOICE_STATE)
                    .enableCache(CacheFlag.EMOJI)
                    .enableCache(CacheFlag.STICKER)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setActivity(Activity.playing("ななみちゃんbot v"+ ver.get()))
                    .setAutoReconnect(true)
                    .build();

            //System.out.println(build.getSelfUser().getAsTag());



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
