package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.security.SecureRandom;

public class Nullpo extends Chat {
    public Nullpo(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return new Help(getTextChannel(), getMessage()).getHelpMessage(1004);
    }

    @Override
    public void run() {
        SecureRandom secureRandom = new SecureRandom();
        String[] msg = new String[]{
                "がっ！",
                "ガッ！",
                "`" +
                        "." +
                        "　　Λ＿Λ　　＼＼\n" +
                        "　 （　・∀・）　　　|　|　ｶﾞｯ\n" +
                        "　と　　　　）　 　 |　|\n" +
                        "　　 Ｙ　/ノ　　　 人\n" +
                        "　　　 /　）　 　 < 　>_Λ∩\n" +
                        "　 ＿/し'　／／. Ｖ｀Д´）/\n" +
                        "　（＿フ彡　　　　　 　　/　`"
        };

        getMessage().reply(msg[secureRandom.nextInt(msg.length - 1)]).queue();
    }
}
