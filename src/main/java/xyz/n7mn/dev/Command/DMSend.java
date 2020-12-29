package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

public class DMSend extends Chat {
    public DMSend(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {
        getMessage().delete().queue();

        String[] split = getMessageText().split(" ", -1);

        if (split.length == 4){

            try {

                RestAction<User> user = getGuild().getJDA().retrieveUserById(split[1]);
                PrivateChannel dm = user.complete().openPrivateChannel().complete();

                int count = Integer.parseInt(split[2]);

                for (int i = 1; i <= count; i++){
                    dm.sendMessage(split[3]).queue();
                }

            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
