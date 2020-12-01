package xyz.n7mn.dev.api.data.eq;

import xyz.n7mn.dev.api.data.eq.body.Comments;
import xyz.n7mn.dev.api.data.eq.body.Earthquake;
import xyz.n7mn.dev.api.data.eq.intensity.Intensity;

public class Body {

    private Earthquake Earthquake;
    private Intensity Intensity;
    private Comments Comments;

    public Earthquake getEarthquake() {
        return Earthquake;
    }

    public Intensity getIntensity() {
        return Intensity;
    }

    public Comments getComments() {
        return Comments;
    }
}
