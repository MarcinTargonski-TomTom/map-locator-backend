package com.tomtom.locator.map.map_locator.mom.service.map;

import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.SearchApiResponse;
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

    @Override
    public SearchApiResponse getPlacesMatchingQuery(PointOfInterest poi) {
        return RestClient.builder().baseUrl(baseUrl).build().get().uri(
                        uriBuilder -> uriBuilder
                                .path(String.format("/search/2/search/%s.json", poi.unifyQuery()))
                                .queryParam("lat", poi.getCenter().getLatitude())
                                .queryParam("lon", poi.getCenter().getLongitude())
                                .queryParam("limit", 3)
                                .queryParam("language", "pl-PL") //TODO: make it configurable
                                .queryParam("key", key)
                                .build()
                )
                .retrieve().body(SearchApiResponse.class);
    }
}
