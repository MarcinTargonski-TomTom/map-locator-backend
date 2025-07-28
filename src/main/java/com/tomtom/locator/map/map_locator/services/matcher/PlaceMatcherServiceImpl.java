package com.tomtom.locator.map.map_locator.services.matcher;

import com.tomtom.locator.map.map_locator.loggers.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import com.tomtom.locator.map.map_locator.model.Point;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.services.map.MapService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
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

    @Override
    public Region findRegionForPlaces(List<PointOfInterest> pois) {
        List<CalculatedRoute> calculatedRoutes = pois.stream().map(mapService::getRegionForPoint).toList();

        return getOverlapingRegion(calculatedRoutes);
    }

    private Region getOverlapingRegion(List<CalculatedRoute> calculatedRoutes) {
        if (calculatedRoutes == null || calculatedRoutes.isEmpty()) {
            throw new IllegalArgumentException("Lista tras jest pusta!");
        }

        Geometry actualPolygon = convertRegionToJTSPolygon(calculatedRoutes.getFirst().getReachableRange());

        for (int i = 1; i < calculatedRoutes.size(); i++) {
            Polygon polygon = convertRegionToJTSPolygon(calculatedRoutes.get(i).getReachableRange());
            actualPolygon = actualPolygon.intersection(polygon);
        }

        return convertPolygonToRegion((Polygon) actualPolygon);
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
