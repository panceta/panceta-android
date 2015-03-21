package net.guidogarcia.panceta.events;

import com.estimote.sdk.Region;

public class EnterRegionEvent extends RegionEvent {
    public EnterRegionEvent(Region region) {
        super(region);
    }
}
