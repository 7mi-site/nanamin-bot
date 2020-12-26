package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.data.VoteReactionList;
import xyz.n7mn.dev.game.MoneySystem;

public abstract class GameCommandClassInterface implements Chat {

    private final JDA jda;
    private final Guild guild;
    private final TextChannel textChannel;
    private final Member member;
    private final User user;
    private final Message message;
    private final String MessageText;

    private final MoneySystem moneySystem;

    public GameCommandClassInterface(MoneySystem moneySystem, TextChannel textChannel, Message message){

        this.jda = textChannel.getJDA();
        this.guild = textChannel.getGuild();
        this.textChannel = textChannel;
        this.member = message.getMember();
        this.user = message.getAuthor();
        this.message = message;
        this.MessageText = message.getContentRaw();

        this.moneySystem = moneySystem;

    }

    @Override
    public JDA getJda() {
        return jda;
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public TextChannel getTextChannel() {
        return textChannel;
    }

    @Override
    public Member getMember() {
        return member;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String getMessageText() {
        return MessageText;
    }

    @Override
    public VoteReactionList getVoteReactionList() {
        return null;
    }

    @Override
    public MoneySystem getMoneySystem() {
        return moneySystem;
    }
}
