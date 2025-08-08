package com.tomtom.locator.map.map_locator.model;

import com.tomtom.locator.map.map_locator.mom.dto.PointOfInterestDTO;

import java.util.List;

public record MatchingRequest(
        List<PointOfInterestDTO> pois,
        MatchingSmootherType matchingSmootherType
) {
}
