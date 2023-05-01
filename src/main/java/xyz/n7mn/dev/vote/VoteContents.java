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
    private String VoteType = "default";
    private boolean EndFlag = false;

    public VoteContents(String messageChannelId, String title, String[] vote, String endDate, String voteType) throws ParseException {
        this.MessageChannelId = messageChannelId;
        this.Title = title;
        this.Vote = vote;
        this.VoteType = voteType;

        EndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate);
    }

    public VoteContents(String messageId, String title, String[] vote, Date endDate, String voteType){
        this.MessageChannelId = messageId;
        this.Title = title;
        this.Vote = vote;
        this.EndDate = endDate;
        this.VoteType = voteType;
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

    public String getVoteType() {
        return VoteType;
    }

    public void setVoteType(String voteType) {
        VoteType = voteType;
    }

    public boolean isEndFlag() {
        return EndFlag;
    }

    public void setEndFlag(boolean endFlag) {
        EndFlag = endFlag;
    }
}
