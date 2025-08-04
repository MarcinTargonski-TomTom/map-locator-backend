package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.model.LocationMatch;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LocationMatchService {

    @Async
    CompletableFuture<List<LocationMatch>> addToAccount(String login, List<LocationMatch> locationMatches);

    List<LocationMatch> getAccountLocations(String login);
}
