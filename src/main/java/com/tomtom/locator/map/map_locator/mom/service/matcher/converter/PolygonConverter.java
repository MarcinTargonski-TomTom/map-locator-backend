package com.tomtom.locator.map.map_locator.mom.service.matcher.converter;

import com.tomtom.locator.map.map_locator.model.Region;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

public interface PolygonConverter {
    Polygon toPolygon(Region region);

    Region toRegion(Polygon polygon);

    List<Region> toRegion(GeometryCollection multiPolygon);

    List<Region> toRegion(Geometry geometry);
}
