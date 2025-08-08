package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.exception.EmptyListException;
import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.model.MortonTileMatcher;
import com.tomtom.locator.map.map_locator.mok.repository.AccountRepository;
import com.tomtom.locator.map.map_locator.mom.repository.LocationMatchRepository;
import com.tomtom.locator.map.map_locator.mom.repository.MortonTileMatcherRepository;
import com.tomtom.locator.map.map_locator.mom.repository.PointOfInterestRepository;
import com.tomtom.tti.nida.morton.geom.GeoBox;
import com.tomtom.tti.nida.morton.geom.MortonTile;
import com.tomtom.tti.nida.morton.geom.MortonTileLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
@MethodCallLogged
class LocationMatchServiceImpl implements LocationMatchService {

    private final AccountRepository accountRepository;
    private final LocationMatchRepository locationMatchRepository;
    private final PointOfInterestRepository pointOfInterestRepository;
    private final MortonTileMatcherRepository mortonTileMatcherRepository;

    @Async
    @Override
    public CompletableFuture<List<LocationMatch>> addToAccount(String login, List<LocationMatch> locationMatches) {
        updateHeatMap(locationMatches);
        Account account = accountRepository.findByLogin(login)
                .orElseThrow();

        List<LocationMatch> accountLocationMatches = locationMatches
                .stream()
                .map(locationMatch -> {
                    locationMatch.setAccount(account);
                    return locationMatch;
                })
                .toList();

        List<LocationMatch> saved = locationMatchRepository.saveAllAndFlush(accountLocationMatches);
        return CompletableFuture.completedFuture(saved);
    }

    @Override
    public List<LocationMatch> getAccountLocations(String login) {
        return locationMatchRepository.findAllByAccount_login(login);
    }

    @Async
    @Override
    public void updateHeatMap(List<LocationMatch> locationMatches) {
        if (locationMatches == null || locationMatches.isEmpty()) {
            throw new EmptyListException();
        }
        Map<Long, MortonTileMatcher> matchesFromDB = mortonTileMatcherRepository.findAll()
                .stream()
                .collect(Collectors.toMap(MortonTileMatcher::getMortonCode, Function.identity()));

        for (LocationMatch locationMatch : locationMatches) {
            if (!locationMatch.getResponseRegion().getBoundary().isEmpty()) {

                MortonTileLevel<MortonTile.M16> mortonTileLevel = MortonTileLevel.M16;
                List<MortonTile<?>> mortonTiles = mortonTileLevel.getMortonTileCoverage(GeoBox.of(locationMatch.getResponseRegion().getBoundary())).toList();
                mortonTiles.forEach(
                        mortonTile -> {
                            findMatchOrAddNewMortonTileMatcher(mortonTile.getCode(), matchesFromDB);
                        }
                );
            }
        }
        mortonTileMatcherRepository.saveAllAndFlush(matchesFromDB.values().stream().toList());
    }

    private void findMatchOrAddNewMortonTileMatcher(long mortonCode, Map<Long, MortonTileMatcher> matchesFromDB) {
        MortonTileMatcher foundMatch = matchesFromDB.getOrDefault(getMortonCodeAsQuaternary(mortonCode), new MortonTileMatcher(mortonCode, 0));
        foundMatch.incrementOccurrences();
        matchesFromDB.put(foundMatch.getMortonCode(), foundMatch);
    }

    private Long getMortonCodeAsQuaternary(long mortonCode) {
        return Long.valueOf(Long.toString(mortonCode, 4));
    }
}
