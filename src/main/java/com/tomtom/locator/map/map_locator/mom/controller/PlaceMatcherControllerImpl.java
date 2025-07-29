package com.tomtom.locator.map.map_locator.mom.controller;


import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.mom.dto.LocationMatchDTO;
import com.tomtom.locator.map.map_locator.mom.dto.PointOfInterestDTO;
import com.tomtom.locator.map.map_locator.mom.dto.mappers.LocationMatchMapper;
import com.tomtom.locator.map.map_locator.mom.dto.mappers.PointOfInterestMapper;
import com.tomtom.locator.map.map_locator.mom.service.matcher.PlaceMatcherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/locations/v1")
@AllArgsConstructor
@MethodCallLogged
public class PlaceMatcherControllerImpl implements PlaceMatcherController {

    private final PlaceMatcherService placeMatcherService;
    private final PointOfInterestMapper pointOfInterestMapper;
    private final LocationMatchMapper locationMatchMapper;

    @Override
    @PostMapping(path = "/matchLocation", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public LocationMatchDTO matchLocations(@RequestBody List<PointOfInterestDTO> pois) {
        return locationMatchMapper.toDTO(placeMatcherService.findRegionForPlaces(pointOfInterestMapper.toModel(pois)));
    }

}
