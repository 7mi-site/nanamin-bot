package xyz.n7mn.dev.api.data.eq.body;

import java.util.Date;

public class Earthquake {

    private Date OriginTime;
    private Date ArrivalTime;
    private Hypocenter Hypocenter;
    private String Magnitude;
    private String Magnitude_description;

    public Date getOriginTime() {
        return OriginTime;
    }

    public Date getArrivalTime() {
        return ArrivalTime;
    }

    public xyz.n7mn.dev.api.data.eq.body.Hypocenter getHypocenter() {
        return Hypocenter;
    }

    public String getMagnitude() {
        return Magnitude;
    }

    public String getMagnitude_description() {
        return Magnitude_description;
    }
}
