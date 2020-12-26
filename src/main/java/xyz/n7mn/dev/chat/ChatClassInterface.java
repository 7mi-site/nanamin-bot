package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.data.VoteReactionList;
import xyz.n7mn.dev.game.MoneySystem;

public abstract interface ChatClassInterface {


    void run();
    JDA getJda();
    Guild getGuild();
    TextChannel getTextChannel();
    Member getMember();
    User getUser();
    Message getMessage();
    String getMessageText();
    VoteReactionList getVoteReactionList();
    MoneySystem getMoneySystem();

}
