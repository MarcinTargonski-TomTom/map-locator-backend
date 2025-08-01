package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.model.LocationMatch;

import java.util.List;

public interface LocationMatchService {

    void addToAccount(List<LocationMatch> locationMatches);
}
