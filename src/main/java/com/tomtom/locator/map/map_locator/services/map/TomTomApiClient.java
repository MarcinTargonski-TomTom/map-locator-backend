package com.tomtom.locator.map.map_locator.services.map;

import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TomTomApiClient implements MapService {

    private static final String API_VERSION = "1";
    private final RestClient restClient = RestClient.builder().baseUrl("https://api.tomtom.com").build();
    @Value("${tomtom.api.key}")
    private String key;

    @Override
    public CalculatedRoute getRegionForPoint(PointOfInterest poi) {
        return restClient.get().uri(
                        uriBuilder -> uriBuilder
                                .path(String.format("/routing/%s/calculateReachableRange/%s,%s/json", API_VERSION, poi.getCenter().getLatitude(), poi.getCenter().getLongitude()))
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
