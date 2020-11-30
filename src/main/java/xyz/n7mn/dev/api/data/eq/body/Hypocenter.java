package xyz.n7mn.dev.api.data.eq.body;

public class Hypocenter {

    private String Name;
    private int Code;
    private int Depth;
    private double Longitude;
    private double Latitude;
    private String Coordinate;

    public String getName() {
        return Name;
    }

    public int getCode() {
        return Code;
    }

    public int getDepth() {
        return Depth;
    }

    public double getLongitude() {
        return Longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public String getCoordinate() {
        return Coordinate;
    }
}
