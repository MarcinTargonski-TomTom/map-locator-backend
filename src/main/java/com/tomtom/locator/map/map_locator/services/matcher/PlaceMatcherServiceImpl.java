package com.tomtom.locator.map.map_locator.services.matcher;

import com.tomtom.locator.map.map_locator.loggers.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.services.map.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        throw new UnsupportedOperationException("Not implemented yet");
    }


}
