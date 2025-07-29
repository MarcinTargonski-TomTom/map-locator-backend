package com.tomtom.locator.map.map_locator.mom.dto;

import com.tomtom.locator.map.map_locator.model.Region;

import java.util.List;

public record LocationMatchDTO(
        List<Region> requestRegion,
        List<Region> responseRegion
) {
}