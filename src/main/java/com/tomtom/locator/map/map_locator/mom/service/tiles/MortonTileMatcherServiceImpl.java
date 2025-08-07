package com.tomtom.locator.map.map_locator.mom.service.tiles;

import com.tomtom.locator.map.map_locator.model.Stat;
import com.tomtom.locator.map.map_locator.model.StatsPolygon;
import com.tomtom.locator.map.map_locator.mom.repository.MortonTileMatcherRepository;
import com.tomtom.tti.nida.morton.geom.GeoBox;
import com.tomtom.tti.nida.morton.geom.MortonTile;
import com.tomtom.tti.nida.morton.geom.MortonTileLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class MortonTileMatcherServiceImpl implements MortonTileMatcherService {

    private final MortonTileMatcherRepository mortonTileMatcherRepository;

    @Override
    public long countOccurrencesByMortonCode(long mortonCode) {
        return mortonTileMatcherRepository.countOccurrencesAboveByMortonCode(mortonCode) +
                mortonTileMatcherRepository.countOccurrencesBelowByMortonCode(mortonCode);
    }

    @Override
    public List<Stat> getMortonTilesStats(StatsPolygon statsPolygon) {
        MortonTileLevel<?> mortonTileLevel = statsPolygon.getLayer().getMortonTileLevel();
        List<MortonTile<?>> mortonTiles = mortonTileLevel.getMortonTileCoverage(GeoBox.of(statsPolygon.getBounds())).toList();

        List<MortonTile<?>> oneLevelTiles = new ArrayList<>();
        for (MortonTile<?> tile : mortonTiles) {
            if (tile.getLevel() == mortonTileLevel) {
                oneLevelTiles.add(tile);
            } else {
                var childrenTiles = tile.getChildren(mortonTileLevel);
                if (!childrenTiles.isEmpty()) {
                    oneLevelTiles.addAll(childrenTiles);
                }
            }
        }

        List<Long> quaternaryCodes = oneLevelTiles.stream()
                .map(tile -> getMortonCodeAsQuaternary(tile.getCode()))
                .toList();
        List<Stat> stats = new ArrayList<>();
        for (Long quaternaryCode : quaternaryCodes) {
            stats.add(new Stat(getMortonCodeFromQuaternary(quaternaryCode),
                    mortonTileMatcherRepository.countOccurrencesAboveByMortonCode(quaternaryCode) +
                            mortonTileMatcherRepository.countOccurrencesBelowByMortonCode(quaternaryCode)));
        }
        return stats;
    }

    private Long getMortonCodeAsQuaternary(long mortonCode) {
        return Long.valueOf(Long.toString(mortonCode, 4));
    }

    private long getMortonCodeFromQuaternary(long quaternaryCode) {
        return Long.parseLong(String.valueOf(quaternaryCode), 4);
    }
}
