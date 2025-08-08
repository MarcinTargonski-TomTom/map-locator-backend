package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.exception.AnyCoordinatesGivenException;
import com.tomtom.locator.map.map_locator.exception.EmptyListException;
import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.model.SearchApiResponse;
import com.tomtom.locator.map.map_locator.mom.dto.mapper.PointOfInterestMapper;
import com.tomtom.locator.map.map_locator.mom.service.map.MapService;
import com.tomtom.locator.map.map_locator.mom.service.matcher.converter.PolygonConverter;
import com.tomtom.locator.map.map_locator.mom.service.matcher.postprocessing.NonSmoothingSmoother;
import com.tomtom.locator.map.map_locator.mom.service.matcher.postprocessing.RegionSmoother;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@MethodCallLogged
public class PlaceMatcherServiceImpl implements PlaceMatcherService {

    private final ThreadLocal<RegionSmoother> regionSmoother = ThreadLocal.withInitial(NonSmoothingSmoother::new);
    private final MapService mapService;
    private final PointOfInterestMapper pointOfInterestMapper;
    private final PolygonConverter polygonConverter;

    @Override
    public List<LocationMatch> findRegionForPlaces(List<PointOfInterest> pois) {
        List<PointOfInterest> poisWithCoordinates = pois.stream()
                .filter(poi -> poi.getCenter() != null)
                .toList();
        List<PointOfInterest> poisByName = pois.stream()
                .filter(poi -> poi.getCenter() == null)
                .toList();

        Map<PointOfInterest, Region> requestRegionsByCoordinates = poisWithCoordinates.stream()
                .collect(Collectors.toMap(
                        poi -> poi,
                        poi -> regionSmoother.get().smoothRegion(mapService.getRegionForPoint(poi).getReachableRange()))
                );

        if (requestRegionsByCoordinates.isEmpty()) {
            throw new AnyCoordinatesGivenException();
        }
        List<Region> baseOverlappingRegions = getOverlappingRegions(
                requestRegionsByCoordinates.values().stream().toList()
        );

        List<LocationMatch> allMatches = new ArrayList<>();
        for (Region baseOverlappingRegion : baseOverlappingRegions) {

            try {

                if (poisByName.isEmpty()) {
                    LocationMatch baseMatch = new LocationMatch(requestRegionsByCoordinates, baseOverlappingRegion);
                    allMatches.add(baseMatch);
                    continue;
                }

                for (PointOfInterest poiByName : poisByName) {
                    PointOfInterest searchPoi = new PointOfInterest(
                            baseOverlappingRegion.getCenter(),
                            poiByName.getBudgetType(),
                            poiByName.getValue(),
                            poiByName.getTravelMode(),
                            poiByName.getName()
                    );

                    SearchApiResponse searchResponse = mapService.getPlacesMatchingQuery(searchPoi);
                    if (searchResponse == null || searchResponse.getResults() == null) {
                        continue;
                    }

                    searchResponse.getResults().forEach(searchResult -> {
                        PointOfInterest foundPoi = pointOfInterestMapper.fromSearchApiResult(searchResult, poiByName);

                        Region foundRegion = regionSmoother.get().smoothRegion(mapService.getRegionForPoint(foundPoi).getReachableRange());

                        Map<PointOfInterest, Region> newRequestRegions = new HashMap<>(requestRegionsByCoordinates);
                        newRequestRegions.put(foundPoi, foundRegion);

                        List<Region> regionsForIntersection = new ArrayList<>(requestRegionsByCoordinates.values());
                        regionsForIntersection.add(foundRegion);

                        List<Region> newOverlappingRegions = getOverlappingRegions(regionsForIntersection);

                        newOverlappingRegions.forEach(newOverlappingRegion -> {
                            LocationMatch locationMatch = new LocationMatch(newRequestRegions, newOverlappingRegion);
                            allMatches.add(locationMatch);
                        });
                    });


                }
            } catch (Exception e) {
                log.error("Unexpected exception while matching places: {}", e.getMessage(), e);
            }
        }

        return allMatches.stream().filter(match -> match.getResponseRegion().getCenter() != null).sorted(
                (match1, match2) -> (int) (polygonConverter.toPolygon(match1.getResponseRegion()).getArea() -
                        polygonConverter.toPolygon(match2.getResponseRegion()).getArea())
        ).toList();
    }

    @Override
    public void setMatchingSmoother(RegionSmoother regionSmoother) {
        this.regionSmoother.set(regionSmoother);
    }

    List<Region> getOverlappingRegions(List<Region> regions) {
        if (regions == null || regions.isEmpty()) {
            throw new EmptyListException();
        }

        Geometry actualPolygon = polygonConverter.toPolygon(regions.getFirst());

        for (int i = 1; i < regions.size(); i++) {
            Polygon polygon = polygonConverter.toPolygon(regions.get(i));
            actualPolygon = actualPolygon.intersection(polygon);
        }

        return polygonConverter.toRegion(actualPolygon);
    }
}
