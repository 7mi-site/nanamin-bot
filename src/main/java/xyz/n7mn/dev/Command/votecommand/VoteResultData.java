package xyz.n7mn.dev.Command.votecommand;

public class VoteResultData {
    private final String emoji;
    private long count;

    public VoteResultData(String emoji, long count){
        this.emoji = emoji;
        this.count = count;
    }

    public String getEmoji() {
        return emoji;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
