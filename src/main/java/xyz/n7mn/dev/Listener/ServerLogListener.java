package xyz.n7mn.dev.Listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;


public class ServerLogListener {

    public void join(GuildJoinEvent event){
        Guild guild = event.getGuild();

    }


    public void leave(GuildLeaveEvent event){
        Guild guild = event.getGuild();

    }

}
