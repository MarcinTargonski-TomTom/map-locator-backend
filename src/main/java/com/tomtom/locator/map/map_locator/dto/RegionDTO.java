package com.tomtom.locator.map.map_locator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.List;
import java.util.UUID;

public record RegionDTO(
        @Null
        UUID id,
        @Null
        long version,
        @NotNull
        @Schema(name = "Point representing a center of our region", example = "Point(1,2)", requiredMode = Schema.RequiredMode.REQUIRED)
        PointDTO center,
        @NotNull
        @Schema(name = "Boundary points", example = "[Point(1,2), Point(3,4), Point(8,3)]", requiredMode = Schema.RequiredMode.REQUIRED)
        List<PointDTO> boundary
) {
}
