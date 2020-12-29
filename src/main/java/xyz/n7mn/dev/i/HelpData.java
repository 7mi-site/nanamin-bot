package xyz.n7mn.dev.i;

public class HelpData {
    private long HelpNo;
    private String HelpTitle;
    private String HelpMessage;

    public HelpData(long helpNo, String helpTitle, String helpMessage){
        this.HelpNo = helpNo;
        this.HelpTitle = helpTitle;
        this.HelpMessage = helpMessage;
    }

    public long getHelpNo() {
        return HelpNo;
    }

    public String getHelpTitle() {
        return HelpTitle;
    }

    public String getHelpMessage() {
        return HelpMessage;
    }
}
