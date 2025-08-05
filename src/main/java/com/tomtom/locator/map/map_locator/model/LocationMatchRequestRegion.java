package com.tomtom.locator.map.map_locator.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "location_match_request_regions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationMatchRequestRegion {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private LocationMatch locationMatch;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private PointOfInterest pointOfInterest;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Region region;
}