package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

public class DMSendCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public DMSendCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        getMessage().delete().queue();

        String[] split = getMessageText().split(" ", -1);

        if (split.length == 4){

            try {

                RestAction<User> user = getJda().retrieveUserById(split[1]);
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
