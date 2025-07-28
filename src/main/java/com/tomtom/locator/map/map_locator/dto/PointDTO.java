package com.tomtom.locator.map.map_locator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "Point")
public record PointDTO(
        @Min(value = -90, message = "Latitude must be minimum -90 degrees")
        @Max(value = 90, message = "Latitude must be maximum 90 degrees")
        @Schema(description = "Latitude coordinate for a point", minimum = "-90", maximum = "90", example = "21",
                requiredMode = Schema.RequiredMode.REQUIRED)
        double latitude,
        @Min(value = -180, message = "Longitude must be minimum -180 degrees")
        @Max(value = 180, message = "Longitude must be maximum 180 degrees")
        @Schema(description = "Longitude coordinate for a point", minimum = "-180", maximum = "180", example = "37",
                requiredMode = Schema.RequiredMode.REQUIRED)
        double longitude
) {
}
