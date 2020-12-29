package xyz.n7mn.dev.quiz;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class QuizInterface implements Quiz {

    private final Guild guild;
    private final TextChannel textChannel;
    private final Member member;
    private final Message message;

    private final QuizSystem quizSystem;

    public QuizInterface(QuizSystem quizSystem, TextChannel textChannel, Message message){
        this.quizSystem = quizSystem;
        this.guild = textChannel.getGuild();
        this.textChannel = textChannel;
        this.member = message.getMember();
        this.message = message;
    }

    QuizSystem getQuizSystem(){
        return quizSystem;
    }

    Guild getGuild() {
        return guild;
    }

    TextChannel getTextChannel() {
        return textChannel;
    }

    Member getMember(){
        return member;
    }

    Message getMessage() {
        return message;
    }
}
