package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.model.Point;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.model.SearchApiResponse;
import com.tomtom.locator.map.map_locator.model.SearchApiResult;
import com.tomtom.locator.map.map_locator.mom.dto.mapper.PointOfInterestMapper;
import com.tomtom.locator.map.map_locator.mom.repository.LocationMatchRepository;
import com.tomtom.locator.map.map_locator.mom.service.map.MapService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@MethodCallLogged
public class PlaceMatcherServiceImpl implements PlaceMatcherService {

    private final MapService mapService;
    private final LocationMatchRepository locationMatchRepository;
    private final PointOfInterestMapper pointOfInterestMapper;

    @Override
    public List<Region> getPlacesMatchingQuery(PointOfInterest poi) {
        SearchApiResponse response = mapService.getPlacesMatchingQuery(poi);
        if (response == null || response.getResults() == null) {
            return new ArrayList<>();
        }
        List<Region> regions = new ArrayList<>();
        for (SearchApiResult result : response.getResults()) {
            PointOfInterest resultPoi = pointOfInterestMapper.fromSearchApiResult(result, poi);
            Region region = mapService.getRegionForPoint(resultPoi).getReachableRange();
            regions.add(region);
        }
        return regions;
    }

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
                        poi -> mapService.getRegionForPoint(poi).getReachableRange()
                ));

        List<Region> baseOverlappingRegions = getOverlappingRegions(
                requestRegionsByCoordinates.values().stream().toList()
        );

        List<LocationMatch> allMatches = new ArrayList<>();

        for (Region baseOverlappingRegion : baseOverlappingRegions) {

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

                    Region foundRegion = mapService.getRegionForPoint(foundPoi).getReachableRange();

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
        }

        return allMatches;
    }

    List<Region> getOverlappingRegions(List<Region> regions) {
        if (regions == null || regions.isEmpty()) {
            throw new IllegalArgumentException("Lista tras jest pusta!");
        }

        Geometry actualPolygon = convertRegionToJTSPolygon(regions.getFirst());

        for (int i = 1; i < regions.size(); i++) {
            Polygon polygon = convertRegionToJTSPolygon(regions.get(i));
            actualPolygon = actualPolygon.intersection(polygon);
        }

        return switch (actualPolygon) {
            case Polygon polygon -> List.of(convertPolygonToRegion(polygon));
            case MultiPolygon multiPolygon -> convertGeometryCollectionToRegion(multiPolygon);
            case GeometryCollection geometryCollection -> convertGeometryCollectionToRegion(geometryCollection);
            default ->
                    throw new IllegalArgumentException("Unsupported geometry type: " + actualPolygon.getGeometryType());
        };
    }


    private Polygon convertRegionToJTSPolygon(Region region) {
        GeometryFactory geometryFactory = new GeometryFactory();

        List<Coordinate> uniqueCoordinates = region.getBoundary().stream()
                .map(point -> new Coordinate(point.getLatitude(), point.getLongitude()))
                .distinct()
                .toList();
        Coordinate[] coordinates = uniqueCoordinates.toArray(new Coordinate[0]);

        if (!coordinates[0].equals(coordinates[coordinates.length - 1])) {
            coordinates = Arrays.copyOf(coordinates, coordinates.length + 1);
            coordinates[coordinates.length - 1] = coordinates[0];
        }

        LinearRing shell = geometryFactory.createLinearRing(coordinates);
        return geometryFactory.createPolygon(shell, null);
    }


    private Region convertPolygonToRegion(Polygon polygon) {
        Region region = new Region(null, new ArrayList<>());
        Coordinate[] coordinates = polygon.getCoordinates();

        Arrays.stream(coordinates).forEach(coordinate ->
                region.getBoundary().add(new Point(coordinate.x, coordinate.y))
        );

        if (polygon.getCentroid().getCoordinate() != null) {
            region.setCenter(new Point(polygon.getCentroid().getCoordinate().x, polygon.getCentroid().getCoordinate().y));
        }

        return region;
    }

    private List<Region> convertGeometryCollectionToRegion(GeometryCollection multiPolygon) {
        List<Region> overlappingRegions = new ArrayList<>();
        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
            if (multiPolygon.getGeometryN(i) instanceof Polygon poly) {
                overlappingRegions.add(convertPolygonToRegion(poly));
            }
        }
        return overlappingRegions;
    }

}
