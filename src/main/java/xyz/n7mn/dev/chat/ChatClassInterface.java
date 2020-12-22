package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.data.Vote;
import xyz.n7mn.dev.data.VoteComparator;
import xyz.n7mn.dev.data.VoteReaction;
import xyz.n7mn.dev.data.VoteReactionList;
import xyz.n7mn.dev.game.MoneySystem;

import java.util.ArrayList;
import java.util.List;

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
