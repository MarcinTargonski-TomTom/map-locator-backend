package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.model.LocationMatch;

import java.util.List;

public interface LocationMatchService {

    List<LocationMatch> addToAccount(List<LocationMatch> locationMatches);

    List<LocationMatch> getAccountLocations();
}
