package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.model.Point;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.mom.repository.LocationMatchRepository;
import com.tomtom.locator.map.map_locator.mom.service.map.MapService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@MethodCallLogged
public class PlaceMatcherServiceImpl implements PlaceMatcherService {

    private final MapService mapService;
    private final LocationMatchRepository locationMatchRepository;

    @Override
    public LocationMatch findRegionForPlaces(List<PointOfInterest> pois) {
        List<CalculatedRoute> calculatedRoutes = pois.stream().map(mapService::getRegionForPoint).toList();
        List<Region> overlapingRegion = getOverlapingRegion(calculatedRoutes);

        List<Region> requestRegions = calculatedRoutes.stream()
                .map(CalculatedRoute::getReachableRange)
                .toList();

        return new LocationMatch(requestRegions, overlapingRegion);
    }

    private List<Region> getOverlapingRegion(List<CalculatedRoute> calculatedRoutes) {
        if (calculatedRoutes == null || calculatedRoutes.isEmpty()) {
            throw new IllegalArgumentException("Lista tras jest pusta!");
        }

        Geometry actualPolygon = convertRegionToJTSPolygon(calculatedRoutes.getFirst().getReachableRange());

        for (int i = 1; i < calculatedRoutes.size(); i++) {
            Polygon polygon = convertRegionToJTSPolygon(calculatedRoutes.get(i).getReachableRange());
            actualPolygon = actualPolygon.intersection(polygon);
        }

        if (actualPolygon instanceof Polygon) {
            return List.of(convertPolygonToRegion((Polygon) actualPolygon));
        } else if (actualPolygon instanceof MultiPolygon multiPolygon) {
            List<Region> regions = new ArrayList<>();
            for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                Polygon poly = (Polygon) multiPolygon.getGeometryN(i);
                regions.add(convertPolygonToRegion(poly));
            }
            return regions;
        } else {
            throw new IllegalArgumentException("Unsupported geometry type: " + actualPolygon.getGeometryType());
        }
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

        return region;
    }

}
