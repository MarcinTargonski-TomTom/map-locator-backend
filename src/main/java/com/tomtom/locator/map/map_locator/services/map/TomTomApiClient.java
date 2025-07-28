package com.tomtom.locator.map.map_locator.services.map;

import com.tomtom.locator.map.map_locator.loggers.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@MethodCallLogged
public class TomTomApiClient implements MapService {

    @Value("${tomtom.api.base-url}")
    private String baseUrl;

    @Value("${tomtom.api.key}")
    private String key;

    @Override
    public CalculatedRoute getRegionForPoint(PointOfInterest poi) {
        return RestClient.builder().baseUrl(baseUrl).build().get().uri(
                        uriBuilder -> uriBuilder
                                .path(String.format("/routing/1/calculateReachableRange/%s,%s/json", poi.getCenter().getLatitude(), poi.getCenter().getLongitude()))
                                .queryParam(poi.getBudgetType().getQueryParamName(), poi.getValue())
                                .queryParam("avoid", "unpavedRoads")
                                .queryParam("avoid", "ferries")
                                .queryParam("traffic", true)
                                .queryParam("routeType", "fastest")
                                .queryParam("travelMode", poi.getTravelMode().name().toLowerCase())
                                .queryParam("key", key)
                                .build()
                )
                .retrieve().body(CalculatedRoute.class);
    }
}
