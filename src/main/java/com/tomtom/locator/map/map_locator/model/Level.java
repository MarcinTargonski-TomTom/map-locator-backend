package com.tomtom.locator.map.map_locator.model;

import com.tomtom.tti.nida.morton.geom.MortonTileLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Level {
    LEVEL_0(0, MortonTileLevel.M0),
    LEVEL_1(1, MortonTileLevel.M1),
    LEVEL_2(2, MortonTileLevel.M2),
    LEVEL_3(3, MortonTileLevel.M3),
    LEVEL_4(4, MortonTileLevel.M4),
    LEVEL_5(5, MortonTileLevel.M5),
    LEVEL_6(6, MortonTileLevel.M6),
    LEVEL_7(7, MortonTileLevel.M7),
    LEVEL_8(8, MortonTileLevel.M8),
    LEVEL_9(9, MortonTileLevel.M9),
    LEVEL_10(10, MortonTileLevel.M10),
    LEVEL_11(11, MortonTileLevel.M11),
    LEVEL_12(12, MortonTileLevel.M12),
    LEVEL_13(13, MortonTileLevel.M13),
    LEVEL_14(14, MortonTileLevel.M14),
    LEVEL_15(15, MortonTileLevel.M15),
    LEVEL_16(16, MortonTileLevel.M16),
    LEVEL_17(17, MortonTileLevel.M17),
    ;

    private final int level;
    private final MortonTileLevel<?> mortonTileLevel;
}
