package xyz.n7mn.dev.setting;

public class SettingJson {

    private int version = 1;
    private boolean eew;
    private String eewSendChannel;
    private boolean earthquake;
    private String earthquakeSendChannel;
    private String[] SettingOKRoleList;
    private boolean join;
    private String joinSendChannel;
    private boolean leave;
    private String leaveSendChannel;

    private SettingJson(){
        this.eew = false;
        this.eewSendChannel = "";
        this.earthquake = false;
        this.earthquakeSendChannel = "";
        this.SettingOKRoleList = new String[]{""};
        this.join = false;
        this.joinSendChannel = "";
        this.leave = false;
        this.leaveSendChannel = "";
    }

    public SettingJson(boolean eew, String eewSendChannel, boolean earthquake, String earthquakeSendChannel, String[] settingOKRoleList, boolean join, String joinSendChannel, boolean leave, String leaveSendChannel){
        this.eew = eew;
        this.eewSendChannel = eewSendChannel;
        this.earthquake = earthquake;
        this.earthquakeSendChannel = earthquakeSendChannel;
        this.SettingOKRoleList = settingOKRoleList;
        this.join = join;
        this.joinSendChannel = joinSendChannel;
        this.leave = leave;
        this.leaveSendChannel = leaveSendChannel;
    }

    public int getVersion() {
        return version;
    }

    public boolean isEEW() {
        return eew;
    }

    public void setEEW(boolean eew) {
        this.eew = eew;
    }

    public String getEEWSendChannel() {
        return eewSendChannel;
    }

    public void setEEWSendChannel(String eewSendChannel) {
        this.eewSendChannel = eewSendChannel;
    }

    public boolean isEarthquake() {
        return earthquake;
    }

    public void setEarthquake(boolean earthquake) {
        this.earthquake = earthquake;
    }

    public String getEarthquakeSendChannel() {
        return earthquakeSendChannel;
    }

    public void setEarthquakeSendChannel(String earthquakeSendChannel) {
        this.earthquakeSendChannel = earthquakeSendChannel;
    }

    public String[] getSettingOKRoleList() {
        return SettingOKRoleList;
    }

    public void setSettingOKRoleList(String[] settingOKRoleList) {
        SettingOKRoleList = settingOKRoleList;
    }

    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }

    public String getJoinSendChannel() {
        return joinSendChannel;
    }

    public void setJoinSendChannel(String joinSendChannel) {
        this.joinSendChannel = joinSendChannel;
    }

    public boolean isLeave() {
        return leave;
    }

    public void setLeave(boolean leave) {
        this.leave = leave;
    }

    public String getLeaveSendChannel() {
        return leaveSendChannel;
    }

    public void setLeaveSendChannel(String leaveSendChannel) {
        this.leaveSendChannel = leaveSendChannel;
    }
}
