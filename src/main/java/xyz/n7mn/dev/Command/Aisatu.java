package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

public class Aisatu extends Chat {

    String[] ohaList = new String[]{
            "aisatsu1_ohayou.png",
            "nebokeru_man.png",
            "nebokeru_woman.png"
    };

    String[] oyaList = new String[]{
            "animal_hitsuji_sleep.png",
            "sleep_eakon.png",
            "sleep_futon_smartphone_man.png",
            "sleep_futon_smartphone_woman.png"
    };

    public Aisatu(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {
        if (getMessageText().startsWith("n.おは") || getMessageText().startsWith("n.oha")){

            int i = new SecureRandom().nextInt(ohaList.length - 1);

            EmbedBuilder builder = new EmbedBuilder();
            builder.setImage("https://nana-bot.n7mn.xyz/irasutoya/" + ohaList[i]);
            builder.setFooter("素材元：いらすとや");

            getTextChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        if (getMessageText().startsWith("n.おや") || getMessageText().startsWith("n.oya")){

            int i = new SecureRandom().nextInt(oyaList.length - 1);

            EmbedBuilder builder = new EmbedBuilder();
            builder.setImage("https://nana-bot.n7mn.xyz/irasutoya/" + oyaList[i]);
            builder.setFooter("素材元：いらすとや");

            getTextChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        final String img;
        final String msg;
        final String emoji;

        if ((hour >= 4 && minute >= 30) && hour < 10){
            img = "aisatsu1_ohayou.png";
            msg = "おはよっ！";
            emoji = "\uD83C\uDF05";
        } else if (hour >= 10 && hour < 19){
            img = "aisatsu2_konnichiwa.png";
            msg = "こんっ！";
            emoji = "\u2600";
        } else {
            img = "aisatsu3_konbanwa.png";
            msg = "こんばんわっ！";
            emoji = "\uD83C\uDF03";
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setImage("https://nana-bot.n7mn.xyz/irasutoya/" + img);
        builder.setFooter("素材元：いらすとや");

        getMessage().delete().queue();

        getTextChannel().sendMessage(msg).setEmbeds(builder.build()).queue(message -> {
            message.addReaction(emoji).queue();
        });

    }
}
