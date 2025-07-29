package com.tomtom.locator.map.map_locator.mom.controller;

import com.tomtom.locator.map.map_locator.mom.dto.LocationMatchDTO;
import com.tomtom.locator.map.map_locator.mom.dto.PointOfInterestDTO;

import java.util.List;


public interface PlaceMatcherController {

    List<LocationMatchDTO> matchLocations(List<PointOfInterestDTO> points);
}
