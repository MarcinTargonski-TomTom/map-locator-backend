package com.tomtom.locator.map.map_locator.mom.service.matcher.postprocessing;

import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.mom.service.matcher.converter.PolygonConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EnvelopeSmoother implements RegionSmoother {

    PolygonConverter polygonConverter;

    @Override
    public Region smoothRegion(Region region) {
        return polygonConverter.toRegion(polygonConverter.toPolygon(region).getEnvelope()).getFirst();
    }
}
