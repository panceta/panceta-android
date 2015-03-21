package net.guidogarcia.panceta.events;

import com.estimote.sdk.Region;

public abstract class RegionEvent {
    private Region region;

    public RegionEvent(Region region) {
        this.region = region;
    }

    public Region getRegion() {
        return this.region;
    }
}
