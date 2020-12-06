package xyz.n7mn.dev;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class NanaminBot {

    public static void main(String[] args) {

        try {
            JDABuilder.createLight("NzgxMzIzMDg2NjI0NDU2NzM1.X7791A.swaLPRnfZzoL50ntQ7QmC-DqjxQ", GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                    .addEventListeners(new EventListener())
                    .enableCache(CacheFlag.VOICE_STATE)
                    .setActivity(Activity.watching("Discord"))
                    .build();



        } catch (Exception e){
            e.printStackTrace();
        }

    }



}
