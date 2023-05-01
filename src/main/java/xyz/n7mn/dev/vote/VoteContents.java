package xyz.n7mn.dev.vote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class VoteContents {

    private UUID VoteID = UUID.randomUUID();
    private String MessageChannelId;
    private String Title;
    private String[] Vote;
    private Date EndDate;

    public VoteContents(String messageChannelId, String title, String[] vote, String endDate) throws ParseException {
        this.MessageChannelId = messageChannelId;
        this.Title = title;
        this.Vote = vote;

        EndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate);
    }

    public VoteContents(String messageId, String title, String[] vote, Date endDate){
        this.MessageChannelId = messageId;
        this.Title = title;
        this.Vote = vote;
        this.EndDate = endDate;
    }

    public UUID getVoteID() {
        return VoteID;
    }

    public String getMessageChannelId() {
        return MessageChannelId;
    }

    public void setMessageChannelId(String messageChannelId) {
        MessageChannelId = messageChannelId;
    }

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

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }
}
