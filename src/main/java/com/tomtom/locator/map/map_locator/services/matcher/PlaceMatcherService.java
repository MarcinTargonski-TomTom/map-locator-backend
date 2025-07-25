package com.tomtom.locator.map.map_locator.services.matcher;

import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.Region;

import java.util.List;

public interface PlaceMatcherService {
    Region findRegionForPlaces(List<PointOfInterest> pois);
}
