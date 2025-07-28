package com.tomtom.locator.map.map_locator.mom.service.map;

import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import org.springframework.stereotype.Service;

@Service
public interface MapService {

    CalculatedRoute getRegionForPoint(PointOfInterest poi);
}
