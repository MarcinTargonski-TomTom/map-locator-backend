package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.Region;

import java.util.List;

public interface PlaceMatcherService {
    List<LocationMatch> findRegionForPlaces(List<PointOfInterest> pois);
    List<Region> getPlacesMatchingQuery(PointOfInterest poi);
}
