package xyz.n7mn.dev.api.data.eq.intensity;

public class City {

    private String Name;
    private int Code;
    private String MaxInt;
    private IntensityStation[] IntensityStation;

    public String getName() {
        return Name;
    }

    public int getCode() {
        return Code;
    }

    public String getMaxInt() {
        return MaxInt;
    }

    public IntensityStation[] getIntensityStation() {
        return IntensityStation;
    }

}
