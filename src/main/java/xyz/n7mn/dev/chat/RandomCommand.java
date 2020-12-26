package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.shuffle;

public class RandomCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public RandomCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        SecureRandom secureRandom = new SecureRandom();

        String[] split = getMessageText().split(" ", -1);
        List<String> wordList = new ArrayList<>();
        wordList.addAll(Arrays.asList(split).subList(1, split.length));

        shuffle(wordList);


        int i = (secureRandom.nextInt(wordList.size()));
        getTextChannel().sendMessage("選ばれたのは「" + wordList.get(i)+"」だよっ！").queue();

    }
}
