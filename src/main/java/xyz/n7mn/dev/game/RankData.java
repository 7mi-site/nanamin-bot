package xyz.n7mn.dev.game;

public class RankData {
    private String MemberId;
    private long Money;

    public RankData(String memberId, long money){
        this.MemberId = memberId;
        this.Money = money;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public long getMoney() {
        return Money;
    }

    public void setMoney(long money) {
        Money = money;
    }
}
