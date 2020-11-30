package xyz.n7mn.dev.api.data;

import xyz.n7mn.dev.api.data.eq.Body;
import xyz.n7mn.dev.api.data.eq.Control;
import xyz.n7mn.dev.api.data.eq.Head;

public class EarthquakeResult {

    private Control Control;
    private Head Head;
    private Body Body;

    public Control getControl() {
        return Control;
    }

    public Head getHead() {
        return Head;
    }

    public Body getBody() {
        return Body;
    }
}
