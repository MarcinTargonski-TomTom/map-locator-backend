package com.tomtom.locator.map.map_locator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "morton_tile_matches")
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Getter
@Setter
public class MortonTileMatcher {

    @Id
    private long mortonCode;
    @EqualsAndHashCode.Exclude
    private int occurrences;

    public void incrementOccurrences() {
        this.occurrences++;
    }

    public MortonTileMatcher(long mortonCode, int occurrences) {
        this.mortonCode = getMortonCodeAsQuaternary(mortonCode);
        this.occurrences = occurrences;
    }

    private Long getMortonCodeAsQuaternary(long mortonCode) {
        return Long.valueOf(Long.toString(mortonCode, 4));
    }
}
