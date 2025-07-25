package com.tomtom.locator.map.map_locator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Schema(description = "Calculated Route")
public record CalculatedRouteDTO(
        @Null
        UUID id,
        @Null
        long version,
        @NotNull
        @NotBlank
        @Pattern(regexp = "^\\d{1,3}\\.\\d{1,3}.\\d{1,3}$", message = "Version must meet the pattern.")
        @Schema(description = "Version of the format", example = "0.0.1", requiredMode = Schema.RequiredMode.REQUIRED)
        String formatVersion,
        @NotNull
        @Schema(description = "Region of a reachable range", example = "0.0.1", requiredMode = Schema.RequiredMode.REQUIRED)
        RegionDTO reachableRange
) {
}
