package com.tomtom.locator.map.map_locator.mom.controller;

import com.tomtom.locator.map.map_locator.model.MatchingRequest;
import com.tomtom.locator.map.map_locator.mom.dto.LocationMatchDTO;
import com.tomtom.locator.map.map_locator.mom.dto.StatsPolygonDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface PlaceMatcherController {

    List<LocationMatchDTO> matchLocations(MatchingRequest points, Authentication authentication);

    List<LocationMatchDTO> getAccountLocations(Authentication authentication);

    ResponseEntity<?> getStats(long mortonCode);

    ResponseEntity<?> getStatsForPolygon(StatsPolygonDTO statsPolygon);
}
