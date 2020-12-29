package xyz.n7mn.dev.quiz;

public class QuizData {
    private final String DiscordUserID;
    private final long rightAnswersCount;
    private final long AnswersCount;

    public QuizData(String discordUserID, long rightAnswersCount, long answersCount){
        this.DiscordUserID = discordUserID;
        this.rightAnswersCount = rightAnswersCount;
        this.AnswersCount = answersCount;
    }

    public String getDiscordUserID() {
        return DiscordUserID;
    }

    public long getRightAnswersCount() {
        return rightAnswersCount;
    }

    public long getAnswersCount() {
        return AnswersCount;
    }

}
