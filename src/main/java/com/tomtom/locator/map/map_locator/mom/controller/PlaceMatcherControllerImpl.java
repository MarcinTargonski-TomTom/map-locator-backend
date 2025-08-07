package com.tomtom.locator.map.map_locator.mom.controller;

import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.mom.dto.LocationMatchDTO;
import com.tomtom.locator.map.map_locator.mom.dto.PointOfInterestDTO;
import com.tomtom.locator.map.map_locator.mom.dto.StatDTO;
import com.tomtom.locator.map.map_locator.mom.dto.StatsPolygonDTO;
import com.tomtom.locator.map.map_locator.mom.dto.mapper.LocationMatchMapper;
import com.tomtom.locator.map.map_locator.mom.dto.mapper.PointOfInterestMapper;
import com.tomtom.locator.map.map_locator.mom.service.ai.LlmService;
import com.tomtom.locator.map.map_locator.mom.dto.mapper.StatMapper;
import com.tomtom.locator.map.map_locator.mom.dto.mapper.StatsPolygonMapper;
import com.tomtom.locator.map.map_locator.mom.service.matcher.LocationMatchService;
import com.tomtom.locator.map.map_locator.mom.service.matcher.PlaceMatcherService;
import com.tomtom.locator.map.map_locator.mom.service.tiles.MortonTileMatcherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final LlmService llmService;

    private final PlaceMatcherService placeMatcherService;
    private final LocationMatchService locationMatchService;
    private final MortonTileMatcherService mortonTileMatcherService;
    private final PointOfInterestMapper pointOfInterestMapper;
    private final LocationMatchMapper locationMatchMapper;
    private final StatsPolygonMapper statsPolygonMapper;
    private final StatMapper statMapper;

    @Override
    @PostMapping(path = "/matchLocation", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<LocationMatchDTO> matchLocations(@RequestBody List<PointOfInterestDTO> pois, Authentication authentication) {
        List<LocationMatch> regionForPlaces = placeMatcherService.findRegionForPlaces(pointOfInterestMapper.toModel(pois));
        regionForPlaces = regionForPlaces.stream().map(match -> {
            match.setName(llmService.getNameForPoiNames(match.getRequestRegions().keySet().stream().map(PointOfInterest::getName).toList()));
            return match;
        }).toList();
        locationMatchService.addToAccount(authentication.getName(), regionForPlaces);

        return locationMatchMapper.toDTO(regionForPlaces);
    }

    @Override
    @GetMapping(path = "/accountLocations", produces = APPLICATION_JSON_VALUE)
    public List<LocationMatchDTO> getAccountLocations(Authentication authentication) {
        List<LocationMatch> accountLocations = locationMatchService.getAccountLocations(authentication.getName());
        return locationMatchMapper.toDTO(accountLocations);
    }

    @Override
    @GetMapping(path = "/stats/{mortonCode}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStats(@PathVariable long mortonCode) {
        return ResponseEntity.ok(
                mortonTileMatcherService.countOccurrencesByMortonCode(mortonCode)
        );
    }

    @Override
    @PostMapping(path = "/stats", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatDTO>> getStatsForPolygon(@RequestBody StatsPolygonDTO statsPolygon) {
        return ResponseEntity.ok(
                statMapper.toDTOList(
                        mortonTileMatcherService.getMortonTilesStats(statsPolygonMapper.toModel(statsPolygon))
                )
        );
    }
}
