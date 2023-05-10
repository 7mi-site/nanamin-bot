package xyz.n7mn.dev.setting;

public class SettingJson {

    private int version = 1;
    private boolean eew;
    private String eewSendChannel;
    private boolean earthquake;
    private String earthquakeSendChannel;

    private SettingJson(){
        this.eew = false;
        this.eewSendChannel = "";
        this.earthquake = false;
        this.earthquakeSendChannel = "";
    }

    public SettingJson(boolean eew, String eewSendChannel, boolean earthquake, String earthquakeSendChannel){
        this.eew = eew;
        this.eewSendChannel = eewSendChannel;
        this.earthquake = earthquake;
        this.earthquakeSendChannel = earthquakeSendChannel;
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
}
