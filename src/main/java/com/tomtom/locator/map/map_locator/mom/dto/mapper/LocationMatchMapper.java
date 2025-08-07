package com.tomtom.locator.map.map_locator.mom.dto.mapper;

import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.mom.dto.LocationMatchDTO;
import com.tomtom.locator.map.map_locator.mom.dto.RequestRegionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LocationMatchMapper {

    private final RegionMapper regionMapper;
    private final PointOfInterestMapper pointOfInterestMapper;

    public List<LocationMatchDTO> toDTO(List<LocationMatch> locationMatches) {
        if (locationMatches == null) return new ArrayList<>();
        List<LocationMatchDTO> list = new ArrayList<>();
        for (LocationMatch locationMatch : locationMatches) {
            list.add(toDTO(locationMatch));
        }
        return list;
    }

    public LocationMatchDTO toDTO(LocationMatch locationMatch) {
        return new LocationMatchDTO(locationMatch.getName(), map(locationMatch.getRequestRegions()), regionMapper.toDTO(locationMatch.getResponseRegion()));
    }

    private List<RequestRegionDTO> map(Map<PointOfInterest, Region> map) {
        if (map == null) return new ArrayList<>();
        List<RequestRegionDTO> list = new ArrayList<>();
        for (Map.Entry<PointOfInterest, Region> entry : map.entrySet()) {
            list.add(new RequestRegionDTO(
                    pointOfInterestMapper.toDto(entry.getKey()),
                    regionMapper.toDTO(entry.getValue())
            ));
        }
        return list;
    }

}
