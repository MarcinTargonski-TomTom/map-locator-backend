package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.mok.repository.AccountRepository;
import com.tomtom.locator.map.map_locator.mom.repository.LocationMatchRepository;
import com.tomtom.locator.map.map_locator.mom.repository.PointOfInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
@MethodCallLogged
class LocationMatchServiceImpl implements LocationMatchService {

    private final AccountRepository accountRepository;
    private final LocationMatchRepository locationMatchRepository;
    private final PointOfInterestRepository pointOfInterestRepository;

    @Async
    @Override
    public CompletableFuture<List<LocationMatch>> addToAccount(String login, List<LocationMatch> locationMatches) {
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
}
