package net.guidogarcia.panceta.events;

import com.estimote.sdk.Region;

public class LeaveRegionEvent extends RegionEvent {
    public LeaveRegionEvent(Region region) {
        super(region);
    }
}
