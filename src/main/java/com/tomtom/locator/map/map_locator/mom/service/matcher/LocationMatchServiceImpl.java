package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.mok.repository.AccountRepository;
import com.tomtom.locator.map.map_locator.mom.repository.LocationMatchRepository;
import com.tomtom.locator.map.map_locator.mom.repository.PointOfInterestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
@Slf4j
class LocationMatchServiceImpl implements LocationMatchService {

    private final AccountRepository accountRepository;
    private final LocationMatchRepository locationMatchRepository;
    private final PointOfInterestRepository pointOfInterestRepository;

    @Override
    public List<LocationMatch> addToAccount(List<LocationMatch> locationMatches) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String login = authentication.getName();
            List<LocationMatch> accountLocationMatches = accountRepository
                    .findByLogin(login)
                    .map(addLocationMatchesToAccount(locationMatches))
                    .orElse(List.of());

            return locationMatchRepository.saveAllAndFlush(accountLocationMatches);

        } catch (DataAccessException e) {
            log.error("Couldn't save location matches for account", e);
            return List.of();
        }
    }

    private Function<Account, List<LocationMatch>> addLocationMatchesToAccount(List<LocationMatch> locationMatches) {
        return account -> locationMatches
                .stream()
                .map(locationMatch -> {
                    locationMatch.setAccount(account);
                    locationMatch.getRequestRegions().keySet().forEach(pointOfInterestRepository::save);
                    return locationMatch;
                })
                .toList();
    }

    @Override
    public List<LocationMatch> getAccountLocations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String login = authentication.getName();
        return locationMatchRepository.findAllByAccount(login);
    }
}
