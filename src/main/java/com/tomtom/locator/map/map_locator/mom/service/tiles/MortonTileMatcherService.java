package com.tomtom.locator.map.map_locator.mom.service.tiles;

import com.tomtom.locator.map.map_locator.model.Stat;
import com.tomtom.locator.map.map_locator.model.StatsPolygon;

import java.util.List;

public interface MortonTileMatcherService {

    long countOccurrencesByMortonCode(long mortonCode);

    List<Stat> getMortonTilesStats(StatsPolygon statsPolygon);
}
