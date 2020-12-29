package xyz.n7mn.dev.Command.votecommand;

public class VoteRegionalData {
    private String Name;
    private String Emoji;

    public VoteRegionalData(String name, String emoji){
        this.Name = name;
        this.Emoji = emoji;
    }

    public String getName() {
        return Name;
    }

    public String getEmoji() {
        return Emoji;
    }
}
