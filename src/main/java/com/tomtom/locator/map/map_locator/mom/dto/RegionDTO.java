package com.tomtom.locator.map.map_locator.mom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Region")
public record RegionDTO(

        @Schema(description = "Point representing a center of our region", example = "Point(1,2)", requiredMode = Schema.RequiredMode.REQUIRED)
        PointDTO center,
        @Schema(description = "Boundary points", example = "[Point(1,2), Point(3,4), Point(8,3)]", requiredMode = Schema.RequiredMode.REQUIRED)
        List<PointDTO> boundary
) {
}
