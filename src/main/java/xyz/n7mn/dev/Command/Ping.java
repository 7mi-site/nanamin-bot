package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ping extends Chat {
    public Ping(TextChannel textChannel, Message message) {
        super(textChannel, message);

    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }


    @Override
    public void run() {
        getMessage().reply("応答したよっ\n(情報取得中...)").queue(
                message -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Date fromDate = Date.from(getMessage().getTimeCreated().toInstant());
                    Date toDate = Date.from(message.getTimeCreated().toInstant());

                    EmbedBuilder builder = new EmbedBuilder().setColor(Color.LIGHT_GRAY);
                    builder.addField("n.pingが送信された日時", sdf.format(fromDate),false);
                    builder.addField("ななみちゃんが送信した日時", sdf.format(toDate), false);
                    builder.addField("n.pingが送信されてから応答するまでの時間", (toDate.getTime() - fromDate.getTime()) + "ms", false);
                    message.editMessage("応答したよっ").setEmbeds(builder.build()).queue();

                }
        );

    }


}
