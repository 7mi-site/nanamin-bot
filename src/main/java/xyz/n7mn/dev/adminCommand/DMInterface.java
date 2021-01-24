package xyz.n7mn.dev.adminCommand;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import xyz.n7mn.dev.i.SystemRun;

public abstract class DMInterface implements SystemRun {

    private final JDA jda;
    private final Member member;
    private final Message message;
    private final String messageText;

    public DMInterface(Message message){

        this.jda = message.getJDA();

        this.member = message.getMember();
        this.message = message;
        this.messageText = message.getContentRaw();

    }

    public JDA getJda() {
        return jda;
    }

    public Member getMember() {
        return member;
    }

    public Message getMessage() {
        return message;
    }

    public String getMessageText(){
        return messageText;
    }

    public abstract void run();
}
