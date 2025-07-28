package com.tomtom.locator.map.map_locator.services.matcher;

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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceMatcherServiceImpl implements PlaceMatcherService {

    private final MapService mapService;

    @Override
    public Region findRegionForPlaces(List<PointOfInterest> pois) {
        List<CalculatedRoute> calculatedRoutes = pois.stream().map(mapService::getRegionForPoint).toList();

        return getOverlapingRegion(calculatedRoutes);
    }

    private Region getOverlapingRegion(List<CalculatedRoute> calculatedRoutes) {
        Geometry actualPolygon = convertRegionToJTSPolygon(calculatedRoutes.getFirst().getReachableRange());
        for (var calculatedRoute : calculatedRoutes) {
            Polygon polygon = convertRegionToJTSPolygon(calculatedRoute.getReachableRange());
            actualPolygon = polygon.union(actualPolygon);
        }
        return convertPolygonToRegion((Polygon) actualPolygon);
    }


    private Polygon convertRegionToJTSPolygon(Region region) {
        GeometryFactory geometryFactory = new GeometryFactory();

        Coordinate[] coordinates = region.getBoundary().stream()
                .map(point -> new Coordinate(point.getLongitude(), point.getLatitude()))
                .toArray(Coordinate[]::new);

        if (!coordinates[0].equals(coordinates[coordinates.length - 1])) {
            coordinates = java.util.Arrays.copyOf(coordinates, coordinates.length + 1);
            coordinates[coordinates.length - 1] = coordinates[0];
        }

        LinearRing shell = geometryFactory.createLinearRing(coordinates);
        return geometryFactory.createPolygon(shell, null);
    }

    private Region convertPolygonToRegion(Polygon polygon) {
        Region region = new Region(null, List.of());
        Coordinate[] coordinates = polygon.getCoordinates();
        for (var coordinate : coordinates) {
            region.getBoundary().add(new Point(coordinate.x, coordinate.y));
        }
        return region;
    }
}
