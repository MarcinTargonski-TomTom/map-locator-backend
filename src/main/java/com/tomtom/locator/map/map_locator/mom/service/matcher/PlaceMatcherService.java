package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;

import java.util.List;

public interface PlaceMatcherService {
    LocationMatch findRegionForPlaces(List<PointOfInterest> pois);
}
