package xyz.n7mn.dev.data;

import java.util.Date;

public class Vote {

    private String Title;
    private String[] Vote;
    private Date Closing;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String[] getVote() {
        return Vote;
    }

    public void setVote(String[] vote) {
        Vote = vote;
    }

    public Date getClosing() {
        return Closing;
    }

    public void setClosing(Date closing) {
        Closing = closing;
    }
}
