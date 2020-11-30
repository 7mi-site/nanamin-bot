package xyz.n7mn.dev.api.data.eq;

import xyz.n7mn.dev.api.data.eq.body.Earthquake;
import xyz.n7mn.dev.api.data.eq.intensity.Intensity;

public class Body {

    private Earthquake Earthquake;
    private Intensity Intensity;

    public Earthquake getEarthquake() {
        return Earthquake;
    }

    public Intensity getIntensity() {
        return Intensity;
    }
}
