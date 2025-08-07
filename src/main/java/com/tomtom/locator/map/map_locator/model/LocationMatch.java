package com.tomtom.locator.map.map_locator.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Entity
@Table(name = "location_matches")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
public class LocationMatch extends AbstractEntity {

    @OneToMany(mappedBy = "locationMatch", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<LocationMatchRequestRegion> requestRegions;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Region responseRegion;

    @ManyToOne
    @Setter
    private Account account;

    @Setter
    @Transient
    private String name;

    public LocationMatch(Map<PointOfInterest, Region> requestRegions, Region responseRegion) {
        this.responseRegion = responseRegion;
        this.requestRegions = requestRegions
                .entrySet()
                .stream()
                .map(entry -> {
                    LocationMatchRequestRegion reqRegion = new LocationMatchRequestRegion();
                    reqRegion.setLocationMatch(this);
                    reqRegion.setPointOfInterest(entry.getKey());
                    reqRegion.setRegion(entry.getValue());
                    return reqRegion;
                })
                .collect(toSet());
    }

    public Map<PointOfInterest, Region> getRequestRegions() {
        return requestRegions.stream()
                .collect(toMap(
                        LocationMatchRequestRegion::getPointOfInterest,
                        LocationMatchRequestRegion::getRegion
                ));
    }
}
