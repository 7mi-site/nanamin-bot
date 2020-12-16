package xyz.n7mn.dev;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class NanaminBot {

    public static void main(String[] args) {

        try {

            File file = new File("./token.json");
            if (!file.isFile()){
                System.out.println("存在しないファイル");
                return;
            }

            BufferedReader buffer = null;
            String json;
            try {
                FileInputStream input = new FileInputStream(file);
                InputStreamReader stream = new InputStreamReader(input, StandardCharsets.UTF_8);
                buffer = new BufferedReader(stream);
                StringBuffer sb = new StringBuffer();

                int ch = buffer.read();
                while (ch != -1){
                    sb.append((char) ch);
                    ch = buffer.read();
                }

                json = sb.toString();

            } catch (FileNotFoundException e) {
                json = "[]";
            } catch (IOException e) {
                e.printStackTrace();
                json = "[]";
            } finally {
                try {
                    if (buffer != null){
                        buffer.close();
                    }
                } catch (IOException e) {
                    json = "[]";
                }
            }

            String token = new Gson().fromJson(json, String.class);

            JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_EMOJIS)
                    .addEventListeners(new EventListener())
                    .enableCache(CacheFlag.VOICE_STATE)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setActivity(Activity.playing("ななみちゃんbot v1.1"))
                    .build();



        } catch (Exception e){
            e.printStackTrace();
        }

    }



}
