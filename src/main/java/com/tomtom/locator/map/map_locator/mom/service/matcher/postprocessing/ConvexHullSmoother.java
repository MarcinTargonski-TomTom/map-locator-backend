package com.tomtom.locator.map.map_locator.mom.service.matcher.postprocessing;

import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.mom.service.matcher.converter.PolygonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConvexHullSmoother implements RegionSmoother {
    private final PolygonConverter polygonConverter;

    @Override
    public Region smoothRegion(Region region) {
        return polygonConverter.toRegion(polygonConverter.toPolygon(region).convexHull()).getFirst();
    }
}
