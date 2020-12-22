package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandTemple extends CommandClassInterface {
    // 必要に応じて上のextendsするクラスを変える
    // 通常コマンド : CommandClassInterface
    // 投票系コマンド : VoteCommandClassInterface
    // ゲーム系コマンド : GameCommandClassInterface

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public CommandTemple(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {


    }
}
