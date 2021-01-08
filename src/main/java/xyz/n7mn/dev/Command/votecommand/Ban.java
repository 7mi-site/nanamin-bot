package xyz.n7mn.dev.Command.votecommand;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.security.SecureRandom;

public class Ban extends Chat {
    public Ban(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {

        if (getGuild().getId().equals("517669763556704258")){

            String[] msgList = new String[]{
                    "ばーん",
                    "ばん！",
                    "ばーん！\n 相手に" + new SecureRandom().nextInt(100) + "のダメージっ！"
            };

            int i = new SecureRandom().nextInt(msgList.length - 1);

            getTextChannel().sendMessage(msgList[i]).queue();

        }

    }
}
