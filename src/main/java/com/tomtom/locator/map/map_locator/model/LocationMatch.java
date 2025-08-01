package com.tomtom.locator.map.map_locator.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Entity
@Table(name = "location_matches")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
public class LocationMatch extends AbstractEntity {

    @OneToMany(cascade = CascadeType.PERSIST)
    private Map<PointOfInterest, Region> requestRegions;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Region responseRegion;
}
