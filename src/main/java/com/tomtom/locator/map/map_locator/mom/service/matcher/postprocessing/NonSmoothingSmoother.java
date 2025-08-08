package com.tomtom.locator.map.map_locator.mom.service.matcher.postprocessing;

import com.tomtom.locator.map.map_locator.model.Region;
import org.springframework.stereotype.Component;

@Component
public class NonSmoothingSmoother implements RegionSmoother {
    @Override
    public Region smoothRegion(Region region) {
        return region;
    }
}
