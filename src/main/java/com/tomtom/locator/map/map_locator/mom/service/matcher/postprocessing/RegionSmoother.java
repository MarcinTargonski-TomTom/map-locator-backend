package com.tomtom.locator.map.map_locator.mom.service.matcher.postprocessing;

import com.tomtom.locator.map.map_locator.model.Region;

import java.util.List;

public interface RegionSmoother {
    default List<Region> smoothRegions(List<Region> regions) {
        return regions.stream().map(this::smoothRegion).toList();
    }

    Region smoothRegion(Region region);
}
