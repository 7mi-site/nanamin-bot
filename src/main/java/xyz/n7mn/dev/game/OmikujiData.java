package xyz.n7mn.dev.game;

public class OmikujiData {

    private String Value;
    private Long AddMoney;

    public OmikujiData(String value, Long addMoney){
        this.Value = value;
        this.AddMoney = addMoney;
    }

    public String getValue() {
        return Value;
    }

    public Long getAddMoney() {
        return AddMoney;
    }

    public void setValue(String value) {
        Value = value;
    }

    public void setAddMoney(Long addMoney) {
        AddMoney = addMoney;
    }
}
