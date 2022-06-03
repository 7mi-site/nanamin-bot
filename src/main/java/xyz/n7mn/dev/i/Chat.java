package xyz.n7mn.dev.i;

import net.dv8tion.jda.api.entities.*;

public abstract class Chat implements SystemRun {

    private Guild guild;
    private TextChannel textChannel;
    private Member member;
    private User user;
    private Message message;
    private String messageText;

    public Chat(TextChannel textChannel, Message message){

        this.guild = textChannel.getGuild();
        this.textChannel = textChannel;
        this.member = message.getMember();
        this.user = message.getAuthor();
        this.message = message;
        this.messageText = message.getContentRaw();

    }

    public abstract HelpData getHelpMessage();

    protected Guild getGuild() {
        return guild;
    }

    protected TextChannel getTextChannel() {
        return textChannel;
    }

    protected Member getMember() {
        return member;
    }

    protected User getUser() {
        return user;
    }

    protected Message getMessage() {
        return message;
    }

    protected String getMessageText() {
        return messageText.replaceAll("@","`@`");
    }
}
