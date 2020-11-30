package xyz.n7mn.dev.api.data.eq.intensity;

public class City {

    private String Name;
    private int Code;
    private int MaxInt;
    private IntensityStation[] IntensityStation;

    public String getName() {
        return Name;
    }

    public int getCode() {
        return Code;
    }

    public int getMaxInt() {
        return MaxInt;
    }

    public IntensityStation[] getIntensityStation() {
        return IntensityStation;
    }

}
