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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.DriverManager;

import static com.sun.deploy.cache.Cache.exists;

public class NanamiMain {

    public static void main(String[] args) {

        try {
            File file = new File("./config.yml");

            YamlMapping ConfigYml;
            if (!file.exists()){
                YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
                ConfigYml = builder.add(
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
                        "MySQLServerUsername","a"
                ).add(
                        "MySQLServerPassword","a"
                ).build();

                try {
                    PrintWriter writer = new PrintWriter(file);
                    writer.print(ConfigYml.toString());
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                return;
            }

            ConfigYml = Yaml.createYamlInput(file).readYamlMapping();

            String token = ConfigYml.string("DiscordToken");

            DriverManager.getConnection("jdbc:mysql://");

            JDA build = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_EMOJIS)
                    .addEventListeners(new EventListener())
                    .enableCache(CacheFlag.VOICE_STATE)
                    .enableCache(CacheFlag.EMOTE)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setActivity(Activity.playing("ななみちゃんbot Ver 2.0"))
                    .build();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
