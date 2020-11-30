package xyz.n7mn.dev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import xyz.n7mn.dev.api.Earthquake;

import java.util.List;

public class NanaminBot {



    public static void main(String[] args) {

        final Earthquake earthquake = new Earthquake();

        try {
            JDA discord = JDABuilder.createLight("NzgxMzIzMDg2NjI0NDU2NzM1.X7791A.swaLPRnfZzoL50ntQ7QmC-DqjxQ", GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                    .addEventListeners(new EventListener(earthquake))
                    .setActivity(Activity.watching("Discord"))
                    .build();



        } catch (Exception e){
            e.printStackTrace();
        }

    }



}
