package com.tomtom.locator.map.map_locator.mom.service.map;

import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.SearchApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface MapService {

    CalculatedRoute getRegionForPoint(PointOfInterest poi);

    SearchApiResponse getPlacesMatchingQuery(PointOfInterest poi);
}
