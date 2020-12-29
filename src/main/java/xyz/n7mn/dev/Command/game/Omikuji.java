package xyz.n7mn.dev.Command.game;

public class Omikuji {

    private String ResultText;
    private String ResultComment;
    private int Coins;

    public Omikuji(String resultText, String resultComment, int coins){
        this.ResultText = resultText;
        this.ResultComment = resultComment;
        this.Coins = coins;
    }

    public String getResultText() {
        return ResultText;
    }

    public void setResultText(String resultText) {
        ResultText = resultText;
    }

    public String getResultComment() {
        return ResultComment;
    }

    public void setResultComment(String resultComment) {
        ResultComment = resultComment;
    }

    public int getCoins() {
        return Coins;
    }

    public void setCoins(int coins) {
        Coins = coins;
    }
}
