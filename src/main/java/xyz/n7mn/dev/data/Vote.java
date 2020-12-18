package xyz.n7mn.dev.data;

public class Vote {

    private String Emoji;
    private int Count;

    public Vote(String emoji, int count){
        this.Emoji = emoji;
        this.Count = count;
    }

    public String getEmoji() {
        return Emoji;
    }

    public void setEmoji(String emoji) {
        Emoji = emoji;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
