package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;

public class PingCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public PingCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        OffsetDateTime idLong = getMessage().getTimeCreated();

        Date date = Date.from(idLong.toInstant());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        getMessage().reply("応答したよっ\n(送信元メッセージの日時：" + sdf.format(date) + " (JST))\n(応答時間計測中...)").queue(message1 -> {
            OffsetDateTime time = message1.getTimeCreated();
            Date to = Date.from(time.toInstant());
            message1.editMessage("応答したよっ\n(送信元メッセージの日時：" + sdf.format(date) + " (JST))\n" +
                    "(応答時間 : " + (to.getTime() - date.getTime()) + " ms)").queue();
        });

    }
}
