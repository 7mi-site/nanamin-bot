package xyz.n7mn.dev.setting;

public class SettingData {

    private String guildId;
    private int settingVer;
    private SettingJson json;

    public SettingData(String guildId, int settingVer, SettingJson json){
        this.guildId = guildId;
        this.settingVer = settingVer;
        this.json = json;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public int getSettingVer() {
        return settingVer;
    }

    public void setSettingVer(int settingVer) {
        this.settingVer = settingVer;
    }

    public SettingJson getJson() {
        return json;
    }

    public void setJson(SettingJson json) {
        this.json = json;
    }
}
