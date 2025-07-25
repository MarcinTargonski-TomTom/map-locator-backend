package com.tomtom.locator.map.map_locator.controllers;


import com.tomtom.locator.map.map_locator.dto.PointOfInterestDTO;
import com.tomtom.locator.map.map_locator.dto.RegionDTO;
import com.tomtom.locator.map.map_locator.dto.mappers.PointOfInterestMapper;
import com.tomtom.locator.map.map_locator.dto.mappers.RegionMapper;
import com.tomtom.locator.map.map_locator.services.matcher.PlaceMatcherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/location/v1")
@AllArgsConstructor
public class PlaceMatcherControllerImpl implements PlaceMatcherController {

    private final PlaceMatcherService placeMatcherService;
    private final PointOfInterestMapper pointOfInterestMapper;
    private final RegionMapper regionMapper;

    @Override
    @PostMapping(path = "/matchLocation", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public RegionDTO matchLocations(@RequestBody List<PointOfInterestDTO> pois) {
        return regionMapper.toDTO(placeMatcherService.findRegionForPlaces(pois.stream().map(pointOfInterestMapper::toModel).toList()));
    }

}
