package com.tomtom.locator.map.map_locator.controllers;

import com.tomtom.locator.map.map_locator.dto.PointOfInterestDTO;
import com.tomtom.locator.map.map_locator.dto.RegionDTO;

import java.util.List;

public interface PlaceMatcherController {

    RegionDTO matchLocations(List<PointOfInterestDTO> points);
}
