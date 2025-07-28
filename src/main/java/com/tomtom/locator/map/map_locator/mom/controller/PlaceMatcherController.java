package com.tomtom.locator.map.map_locator.mom.controller;

import com.tomtom.locator.map.map_locator.mom.dto.PointOfInterestDTO;
import com.tomtom.locator.map.map_locator.mom.dto.RegionDTO;

import java.util.List;

public interface PlaceMatcherController {

    RegionDTO matchLocations(List<PointOfInterestDTO> points);
}
