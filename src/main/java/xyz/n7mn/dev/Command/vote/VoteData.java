package xyz.n7mn.dev.Command.vote;


public class VoteData {
    private final String messageId;
    private final String userId;
    private final String Emoji;

    public VoteData(String messageId, String userId, String emoji){
        this.messageId = messageId;
        this.userId = userId;
        this.Emoji = emoji;
    }


    public String getMessageId() {
        return messageId;
    }

    public String getUserId(){
        return userId;
    }

    public String getEmoji() {
        return Emoji;
    }
}
