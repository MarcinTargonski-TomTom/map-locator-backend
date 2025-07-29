package com.tomtom.locator.map.map_locator.mom.dto;

import java.util.Map;

public record LocationMatchDTO(
        Map<PointOfInterestDTO, RegionDTO> requestRegions,
        RegionDTO responseRegion
) {
}
