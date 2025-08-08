package com.tomtom.locator.map.map_locator.mom.service.matcher.converter;

import com.tomtom.locator.map.map_locator.exception.UnsupportedGeometryTypeException;
import com.tomtom.locator.map.map_locator.model.Point;
import com.tomtom.locator.map.map_locator.model.Region;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DefaultConverter implements PolygonConverter {
    @Override
    public Polygon toPolygon(Region region) {
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


    @Override
    public Region toRegion(Polygon polygon) {
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

    @Override
    public List<Region> toRegion(GeometryCollection multiPolygon) {
        List<Region> overlappingRegions = new ArrayList<>();
        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
            if (multiPolygon.getGeometryN(i) instanceof Polygon poly) {
                overlappingRegions.add(toRegion(poly));
            }
        }
        return overlappingRegions;
    }

    @Override
    public List<Region> toRegion(Geometry geometry) {
        return switch (geometry) {
            case Polygon polygon -> List.of(toRegion(polygon));
            case MultiPolygon multiPolygon -> toRegion(multiPolygon);
            case GeometryCollection geometryCollection -> toRegion(geometryCollection);
            default -> throw new UnsupportedGeometryTypeException(geometry.getGeometryType());
        };
    }
}
