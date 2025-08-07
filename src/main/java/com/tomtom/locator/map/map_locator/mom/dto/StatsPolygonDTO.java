package com.tomtom.locator.map.map_locator.mom.dto;

import com.tomtom.locator.map.map_locator.model.Level;

import java.util.List;

public record StatsPolygonDTO(
        List<PointDTO> bounds,
        Level layer
) {
}
