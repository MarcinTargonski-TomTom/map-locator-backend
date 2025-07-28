package com.tomtom.locator.map.map_locator.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "location_matches")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
public class LocationMatch extends AbstractEntity {

    @OneToMany
    private List<Region> requestRegions;

    @OneToOne
    private Region responseRegion;
}
