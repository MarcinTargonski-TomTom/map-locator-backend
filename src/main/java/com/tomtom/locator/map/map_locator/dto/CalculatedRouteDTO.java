package com.tomtom.locator.map.map_locator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Calculated Route")
public record CalculatedRouteDTO(
        @Schema(description = "Version of the format", example = "0.0.1", requiredMode = Schema.RequiredMode.REQUIRED)
        String formatVersion,
        @Schema(description = "Region of a reachable range", example = "0.0.1", requiredMode = Schema.RequiredMode.REQUIRED)
        RegionDTO reachableRange
) {
}
