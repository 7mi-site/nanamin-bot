package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.shuffle;

public class Random extends Chat {
    public Random(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return new Help(getTextChannel(), getMessage()).getHelpMessage(1007);
    }

    @Override
    public void run() {
        SecureRandom secureRandom = new SecureRandom();

        String[] split = getMessageText().split(" ", -1);
        List<String> wordList = new ArrayList<>(Arrays.asList(split).subList(1, split.length));

        shuffle(wordList);


        int i = (secureRandom.nextInt(wordList.size()));
        getTextChannel().sendMessage("選ばれたのは「" + wordList.get(i)+"」だよっ！").queue();
    }
}
