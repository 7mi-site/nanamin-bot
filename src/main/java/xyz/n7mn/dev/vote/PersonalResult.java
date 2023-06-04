package xyz.n7mn.dev.vote;

public class PersonalResult {

    private String SelectReaction;
    private String UserID;
    private String DiscordNameTag;
    private String Username;
    private String Nickname;
    private Boolean Active;

    public PersonalResult(String selectReaction, String userID, String discordNameTag, String username, String nickname, boolean active){
        this.SelectReaction = selectReaction;
        this.UserID = userID;
        this.DiscordNameTag = discordNameTag;
        this.Username = username;
        this.Nickname = nickname;
        this.Active = active;
    }

    public String getSelectReaction() {
        return SelectReaction;
    }

    public void setSelectReaction(String selectReaction) {
        SelectReaction = selectReaction;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getDiscordNameTag() {
        return DiscordNameTag;
    }

    public void setDiscordNameTag(String discordNameTag) {
        DiscordNameTag = discordNameTag;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public boolean isActive() {
        if (Active == null){
            return false;
        }

        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }
}
