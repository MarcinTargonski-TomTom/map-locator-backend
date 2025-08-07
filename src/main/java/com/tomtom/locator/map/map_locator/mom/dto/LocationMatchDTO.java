package com.tomtom.locator.map.map_locator.mom.dto;

import java.util.List;

public record LocationMatchDTO(
        String name,
        List<RequestRegionDTO> requestRegions,
        RegionDTO responseRegion
) {
}
