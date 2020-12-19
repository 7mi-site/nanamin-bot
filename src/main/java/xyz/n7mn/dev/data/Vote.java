package xyz.n7mn.dev.data;

import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.List;

public class Vote {

    private String Emoji;
    private int Count;
    private List<String> NameList;


    public Vote(String emoji, int count, List<String> nameList){
        this.Emoji = emoji;
        this.Count = count;
        this.NameList = nameList;
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

    public List<String> getNameList() {
        return NameList;
    }
}
