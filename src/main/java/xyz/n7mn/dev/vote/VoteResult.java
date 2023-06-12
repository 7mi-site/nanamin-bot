package xyz.n7mn.dev.vote;

import java.util.List;

public class VoteResult {

    private String Title;
    private String[] VoteResult;
    private long ValidityCount;
    private long TotalCount;
    private List<PersonalResult> PersonalResults;

    public VoteResult(String title, String[] voteResult, long validityCount, long totalCount, List<PersonalResult> personalResults){
        this.Title = title;
        this.VoteResult = voteResult;
        this.ValidityCount = validityCount;
        this.TotalCount = totalCount;
        this.PersonalResults = personalResults;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String[] getVoteResult() {
        return VoteResult;
    }

    public void setVoteResult(String[] voteResult) {
        VoteResult = voteResult;
    }

    public long getValidityCount() {
        return ValidityCount;
    }

    public void setValidityCount(long validityCount) {
        ValidityCount = validityCount;
    }

    public long getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(long totalCount) {
        TotalCount = totalCount;
    }

    public List<PersonalResult> getPersonalResults() {
        return PersonalResults;
    }

    public void setPersonalResults(List<PersonalResult> personalResults) {
        PersonalResults = personalResults;
    }
}

